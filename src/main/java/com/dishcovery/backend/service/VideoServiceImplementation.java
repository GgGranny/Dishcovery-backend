package com.dishcovery.backend.service;

import com.dishcovery.backend.interfaces.VideoService;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.repo.VideoRepo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.hls_videos}")
    private String HLS_DIR;

    public VideoServiceImplementation(UserRepo userRepo, VideoRepo videoRepo) {
        this.userRepo = userRepo;
        this.videoRepo = videoRepo;
    }

    @PostConstruct
    public void init() {
        try {
            File file = new File(DIR);
            File file1 = new File(HLS_DIR);

            if(!file1.exists()) Files.createDirectory(Paths.get(HLS_DIR));

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

                ///  transcribe the video
                transcribeVideo(video.getVideoId());

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


    /// Video Processing or Segmentation
    @Override
    public String transcribeVideo(String videoId) {

        Optional<Video> videoOpt = videoRepo.findByVideoId(videoId);
        if(videoOpt.isEmpty()) {
            throw new RuntimeException("Video not found");
        }
        Video video = videoOpt.get();

        try {
            String inputPath = video.getPath();

            Path outputPath = Paths.get(HLS_DIR, video.getVideoId());
            File file = new File(outputPath.toString());
            if(!file.exists()) {
                Files.createDirectory(outputPath);
            }

            String ffmpegCmd = String
                    .format("ffmpeg -i \"%s\" -c:a aac -c:v libx264 -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/master.m3u8\"",inputPath,outputPath.toString(), outputPath.toString());

            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe","/c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();

            int wait = process.waitFor();
            if (wait != 0) {
                throw new RuntimeException("Video Transcription Failed");
            }

        }catch(Exception e) {
            logger.error("file not found", e);
            e.printStackTrace();
        }
        return null;
    }

}
