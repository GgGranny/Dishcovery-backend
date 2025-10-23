package com.dishcovery.backend.service;

import com.dishcovery.backend.interfaces.VideoService;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.repo.VideoRepo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class VideoServiceImplementation implements VideoService {

    public static final Logger logger  = LoggerFactory.getLogger(VideoServiceImplementation.class);
    private final UserRepo userRepo;
    private final VideoRepo videoRepo;

    @Value("${files.video}")
    private String DIR;

    public VideoServiceImplementation(UserRepo userRepo, VideoRepo videoRepo) {
        this.userRepo = userRepo;
        this.videoRepo = videoRepo;
    }

    @PostConstruct
    public void init() {
        try {
            File file = new File(DIR);
            if(!file.exists()) {
                file.mkdir();
            }
        }catch (Exception e) {
            logger.info("dir already initialized", e);
        }
    }

    @Override
    public Video upload(String title, String description, MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // generate ID for video
                String randomUUID = UUID.randomUUID().toString();

                // get the file metadata
                String fileName = randomUUID + "_"+ file.getOriginalFilename();
                String contentType = file.getContentType();
                InputStream inputStream = file.getInputStream();

                String cleanedFileName = StringUtils.cleanPath(fileName);
                String cleanedFolderName = StringUtils.cleanPath(DIR);

                // create a video path
                Path path = Paths.get(cleanedFolderName, cleanedFileName);

                // save the meta data in database
                Video video = new Video();
                video.setVideoId(randomUUID);
                video.setDescription(description);
                video.setTitle(title);
                video.setPath(path.toString());
                video.setUser(userRepo.findByUsername(userDetails.getUsername()));
                video.setContentType(contentType);
                if(contentType.isEmpty()) {
                    video.setContentType("video/mp4");
                }
                video.setUploadedAt(LocalDateTime.now());
                videoRepo.save(video);

                // save the file in dir
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

                logger.info("Video Uploaded Successfully");
                return video;
            }
        }catch(Exception e) {
            logger.info("Video upload error", e);
        }
        return null;
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepo.findAll();
    }

    @Override
    public Video getVideo(String id) {
        Optional<Video> optVideo =  videoRepo.findByVideoId(id);
        if(optVideo.isEmpty()) {
            throw new RuntimeException("Video not found");
        }
        return optVideo.get();
    }

}
