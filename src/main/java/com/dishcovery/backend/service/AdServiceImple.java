package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.AdResponse;
import com.dishcovery.backend.interfaces.AdService;
import com.dishcovery.backend.model.Ad;
import com.dishcovery.backend.model.Subscription;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.enums.SubscriptionStatus;
import com.dishcovery.backend.repo.AdRepo;
import com.dishcovery.backend.repo.SubscriptionRepo;
import com.dishcovery.backend.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.*;

@Service
public class AdServiceImple implements AdService {

    private AdRepo adRepo;

    private SubscriptionRepo subscriptionRepo;

    private UserRepo userRepo;

    private SubscriptionImple subscriptionImple;

    public AdServiceImple(AdRepo adRepo, SubscriptionRepo subscriptionRepo, UserRepo userRepo, SubscriptionImple subscriptionImple){
        this.adRepo = adRepo;
        this.subscriptionRepo = subscriptionRepo;
        this.userRepo = userRepo;
        this.subscriptionImple = subscriptionImple;
    }


    @Value("${file.ad.path}")
    private String adPath;
    //Create a Folder to store files
    @PostConstruct
    void init() throws IOException {
        File f = new File(adPath);
        if(!f.exists()){
            Files.createDirectory(Paths.get(adPath));
        }
    }

    @Override
    public AdResponse uploadAd(MultipartFile file) {
        try {

            if(file == null) {
                throw new FileNotFoundException("file Not found");
            }
            String randomUUID = UUID.randomUUID().toString();
            String fileName = randomUUID.concat(Objects.requireNonNull(file.getOriginalFilename()));

            Path path = Paths.get(adPath,fileName);
            System.out.println(path);
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);

            double duration = getDuration(file, path.toString()) * 60;
            Ad adFile = Ad.builder()
                    .contentType(file.getContentType())
                    .duration(duration)
                    .clickCount(0)
                    .adUrl(path.toString())
                    .adName(fileName)
                    .build();
            Ad savedAd =  adRepo.save(adFile);

            // if not saved to db delete the file
            if(savedAd == null){
                Files.delete(path);
            }
            if(!Files.exists(path) && adRepo.existsById(savedAd.getAd_id())) {
                adRepo.delete(savedAd);
            }

            AdResponse adResponse = AdResponse.builder()
                    .ad_id(savedAd.getAd_id())
                    .adUrl(savedAd.getAdUrl())
                    .contentType(savedAd.getContentType())
                    .adName(savedAd.getAdName())
                    .clickCount(savedAd.getClickCount())
                    .duration(savedAd.getDuration())
                    .build();
            return adResponse;

        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double getDuration(MultipartFile file, String path) throws IOException {
        double duration = 0;
        String contentType = file.getContentType();
        if(contentType.startsWith("image")) {
            duration = 30;
        }else if(contentType.startsWith("video")){
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    path
            );
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            duration = Double.parseDouble(reader.readLine());
        }
        return duration;
    }

    @Override
    public Map<String, Object> fetchAd() {
        if(!shouldShowAd()) {
            return null;
        }
        try {
            Map<String, Object> response = new HashMap<>();

            List<Ad> ads = adRepo.findAll();
            int max = ads.size();
            int min = 1;
            int range = max - min + 1;

            int randomId = (int) (Math.random() * range);
            System.out.println("random id: "+randomId);
            Ad ad = ads.get(randomId);

            Resource resource = new FileSystemResource(ad.getAdUrl());
            response.put("contentType", ad.getContentType());
            response.put("resource",resource);
            return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean shouldShowAd() {
        try{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users user = userRepo.findByUsername(username);
        Subscription subscription = subscriptionRepo.findByUser_Id(user.getId());

        if(subscription == null) {
            return true;
        }

        if(subscription.getSubscriptionStatus() == SubscriptionStatus.FREE) {
            return true;
        }

        if(subscriptionImple.isSubscriptionExpired(user.getId())) {
            return true;
        }

        return false;

        }catch(Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
