package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    // upload metadata and store video
    Video upload( String title, String description, MultipartFile file);

    // get all videos
    List<Video> getAllVideos();

    // get video by id
    Video getVideo(String id);

    // Process the video
    void transcribeVideo(String videoId);

    // Transcribe the video in multiple quality
    void transcribeVideoInMultipleQuality(String videoId);

    // Delete Video
    String deleteVideoById(String videoId);


}
