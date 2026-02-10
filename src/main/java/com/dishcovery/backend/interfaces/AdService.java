package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.AdResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AdService {

    AdResponse uploadAd(MultipartFile file);

    Map<String, Object> fetchAd();

}
