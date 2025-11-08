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
import java.util.*;


@Service
public class VideoServiceImplementation implements VideoService {

    public static final Logger logger  = LoggerFactory.getLogger(VideoServiceImplementation.class);
    private final UserRepo userRepo;
    private final VideoRepo videoRepo;

    @Value("${files.video}")
    private  String DIR;

    @Value("${file.hls_videos}")
    private  String HLS_DIR;

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
                Video video1 = videoRepo.save(video);

                // save the file in dir
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

                ///  transcribe the video
//                transcribeVideo(video.getVideoId());
                transcribeVideoInMultipleQuality(video.getVideoId());

                logger.info("Video Uploaded Successfully");
                return video1;
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
    public void transcribeVideo(String videoId) {

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
        }
    }

    @Override
    public void transcribeVideoInMultipleQuality(String videoId) {
        Optional<Video> videoOpt = videoRepo.findByVideoId(videoId);

        if(videoOpt.isEmpty()) {
            throw new RuntimeException("video not found");
        }

        Video video = videoOpt.get();

        // Create Directories to store the scaled videos
        String inputPath = video.getPath();
        Path outputPath360p = Paths.get(HLS_DIR, video.getVideoId(),"360p");
        Path outputPath540p = Paths.get(HLS_DIR, video.getVideoId(),"540p");
        Path outputPath720p = Paths.get(HLS_DIR, video.getVideoId(),"720p");

        try {

            File file = new File(outputPath360p.toString());
            File file2 = new File(outputPath540p.toString());
            File file3 = new File(outputPath720p.toString());
            if(!file.exists() || !file2.exists() || !file3.exists()) {
                try {
                    Files.createDirectories(outputPath360p);
                    Files.createDirectories(outputPath540p);
                    Files.createDirectories(outputPath720p);
                }catch(Exception e) {
                    logger.error("folder not created", e);
                }
            }

            // FFMPEG command to scale the video to 360p, 540p and 760p
            String ffmpeg_360pCmd = String
                    .format("ffmpeg -y -i \"%s\" -c:a aac -c:v libx264 -x264opts keyint=24:min-keyint=24:no-scenecut -b:v 400k -maxrate 400k -bufsize 800k -vf scale=-1:360 \"%s/%s_360p.mp4\"", inputPath, outputPath360p, video.getVideoId());
            String ffmpeg_540pCmd = String
                    .format("ffmpeg -y -i \"%s\" -c:a aac -c:v libx264 -x264opts keyint=24:min-keyint=24:no-scenecut -b:v 800k -maxrate 800k -bufsize 1600k -vf scale=-1:540 \"%s/%s_540p.mp4\"", inputPath, outputPath540p, video.getVideoId());
            String ffmpeg_720pCmd = String
                    .format("ffmpeg -y -i \"%s\" -c:a aac -c:v libx264 -x264opts keyint=24:min-keyint=24:no-scenecut -b:v 1500k -maxrate 1500k -bufsize 3000k -vf scale=-1:720 \"%s/%s_720p.mp4\"", inputPath, outputPath720p, video.getVideoId());

            executeFfmpegCMD(ffmpeg_540pCmd);
            executeFfmpegCMD(ffmpeg_720pCmd);
            executeFfmpegCMD(ffmpeg_360pCmd);


            // Segment the videos and generate manifest/master file
            Path pathInput_360p = Paths.get(HLS_DIR, video.getVideoId(),"360p",video.getVideoId() + "_360p.mp4");
            Path pathInput_540p = Paths.get(HLS_DIR, video.getVideoId(),"540p", video.getVideoId() +"_540p.mp4");
            Path pathInput_720p = Paths.get(HLS_DIR, video.getVideoId(),"720p", video.getVideoId() +"_720p.mp4");


            String segment_ffmpeg_360p_cmd = String
                    .format("ffmpeg -i \"%s\" -c:a aac -c:v libx264 -strict 2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/index.m3u8\"",pathInput_360p,outputPath360p,outputPath360p );
            String segment_ffmpeg_540p_cmd = String
                    .format("ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict 2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/index.m3u8\"", pathInput_540p, outputPath540p, outputPath540p );
            String segment_ffmpeg_720p_cmd = String
                    .format("ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict 2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/index.m3u8\"", pathInput_720p, outputPath720p, outputPath720p );

            executeFfmpegCMD(segment_ffmpeg_360p_cmd);
            executeFfmpegCMD(segment_ffmpeg_540p_cmd);
            executeFfmpegCMD(segment_ffmpeg_720p_cmd);


            // Create the main master file
            Path path = Paths.get(HLS_DIR, video.getVideoId(),"master.m3u8");

            String indexFile = """
                    #EXTM3U
                    #EXT-X-STREAM-INF:BANDWIDTH=1500000,RESOLUTION=1280x720
                    720p/index.m3u8
                    #EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=960x540
                    540p/index.m3u8
                    #EXT-X-STREAM-INF:BANDWIDTH=400000,RESOLUTION=640x360
                    360p/index.m3u8
                    """;
            Files.writeString(path, indexFile);

            // delete the unsegmented video
            deleteUnsegmentedVideo(pathInput_360p.toString());
            deleteUnsegmentedVideo(pathInput_540p.toString());
            deleteUnsegmentedVideo(pathInput_720p.toString());


        }catch(Exception e) {
            logger.error("Failed to transcribe the video ", e);
        }
    }

    @Override
    public String deleteVideoById(String videoId) {

        // Check If The Video Meta-Data Exists In The DB
        Optional<Video> videoOpt = videoRepo.findById(videoId);
        if(videoOpt.isEmpty()) {
            throw new RuntimeException("Video not found to delete");
        }

        // If Exists Delete The Meta-Data And Video
        Video video = videoOpt.get();
        Path path = Paths.get(video.getPath());

        try {
            Files.deleteIfExists(path);
            videoRepo.deleteById(videoId);

            // Delete The Segmented Video From VideoHls Folder
            Path hlsPath = Paths.get(HLS_DIR, video.getVideoId());
            if(Files.exists(hlsPath)) {
                Files.walk(hlsPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            }catch(Exception e) {
                                logger.error("Failed to delete the video from hls folder", e);
                            }
                        });
            }
        }catch(IOException e) {
            logger.error("File not found to delete", e);
        }

        return videoId;
    }


    // Takes FFMPEG Command and Executes it
    private static void executeFfmpegCMD(String cmd) throws InterruptedException, IOException {
        ProcessBuilder processBuilder =  new ProcessBuilder("cmd.exe","/c", cmd);
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        int wait= process.waitFor();
        if(wait != 0) {
            throw  new RuntimeException("Video transcribe not successful");
        }

    }

    // delete The unsegmented video from the respective folder 360p, 540p, 720p
    private static void deleteUnsegmentedVideo(String path) throws IOException {
        Files.delete(Path.of(path));
    }



}
