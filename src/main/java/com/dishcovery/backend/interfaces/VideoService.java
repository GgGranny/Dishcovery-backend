package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.model.Video;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    // upload metadata and store video
    public Video upload( String title, String description, MultipartFile file);

    // get all videos
    public List<Video> getAllVideos();

    // get video by id
    public Video getVideo(String id);

}
