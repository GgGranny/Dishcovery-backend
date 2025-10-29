package com.dishcovery.backend.controller;


import ch.qos.logback.core.util.StringUtil;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.response.MyResponseHandler;
import com.dishcovery.backend.service.VideoServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/videos")
public class VideoController {

    private static final int CHUNK_SIZE = 1024 * 1024;

    @Autowired
    private VideoServiceImplementation videoServiceImplementation;

    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("description") String description
    ) {
        Video video = videoServiceImplementation.upload(title, description, file);
        if(video != null) {
            return MyResponseHandler.responseBuilder(HttpStatus.OK, "Video Uploaded Successfully", video);
        }
        return MyResponseHandler.responseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, "Video Upload Failed", null  );

    }

    @GetMapping
    public List<Video> getAllVideos() {
        return videoServiceImplementation.getAllVideos();
    }


    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable("videoId") String videoId){
        Video video = videoServiceImplementation.getVideo(videoId);
        if(video != null ){
            String contentType = video.getContentType();
            String path = video.getPath();

            Resource resource = new FileSystemResource(path);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(
            @PathVariable("videoId") String videoId,
            @RequestHeader(value = "Range", required = false) String range
    ){
        Video video = videoServiceImplementation.getVideo(videoId);

        Path path = Paths.get(video.getPath());

        Resource resource = new FileSystemResource(path);

        String contentType = video.getContentType();

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        long fileLength = path.toFile().length();

        if(range == null ) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }

        long startRange;
        long endRange;

        System.out.println(range);
        String[] ranges = range.replace("bytes=","").split("-");

        startRange = Long.parseLong(ranges[0]);

        endRange = startRange + CHUNK_SIZE -1;

//        if(ranges.length > 1) {
//            endRange = Long.parseLong(ranges[1]);
//        }else {
//            endRange = fileLength - 1;
//        }

        if(endRange > fileLength -1) {
            endRange = fileLength -1;
        }
        System.out.println("startRange:" + startRange + " endRange:" + endRange);

        try {
            File file = new File(String.valueOf(path));

            InputStream inputStream = new FileInputStream(file);
            inputStream.skip(startRange);
            long contentLength = endRange - startRange + 1;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + startRange + "-" + endRange + "/" + fileLength);
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Length", String.valueOf(contentLength));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");

            byte[] data = new byte[(int) contentLength];
            int read = inputStream.read(data, 0 , data.length);


            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType(contentType))
                    .headers(headers)
                    .body(new ByteArrayResource(data));
//                    .body(new InputStreamResource(inputStream));

        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Value("${file.hls_videos}")
    private String HLS_DIR;

    @GetMapping("/stream/segment/{videoId}/master.m3u8")
    public ResponseEntity<Resource> streamVideoSegment(@PathVariable("videoId") String videoId) {

        try {
        Path path = Paths.get(HLS_DIR, videoId, "master.m3u8");

        Resource resource = new FileSystemResource(path);

        if(!resource.exists()) {
            throw new FileNotFoundException("No master file found");
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE,"application/vnd.apple.mpegurl")
                .body(resource);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/stream/segment/{videoId}/{segment}.ts")
    public ResponseEntity<Resource> getVideoSegment(@PathVariable("videoId") String videoId, @PathVariable("segment") String segment) {

        try {
            Path path = Paths.get(HLS_DIR, videoId, segment +".ts");

            Resource resource = new FileSystemResource(path);
            if(!resource.exists()) {
                throw new RuntimeException("No segment file found");
            }

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                    .body(resource);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
