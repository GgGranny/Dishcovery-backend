package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.AdResponse;
import com.dishcovery.backend.service.AdServiceImple;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ad/v1")
public class AdController {
    private AdServiceImple adServiceImple;

    public AdController(AdServiceImple adServiceImple){
        this.adServiceImple = adServiceImple;
    }

    @PostMapping
    public ResponseEntity<?> uploadAd(@RequestParam("file") MultipartFile file){
        AdResponse adResponse = adServiceImple.uploadAd(file);
        if(adResponse != null){
            return ResponseEntity.ok(adResponse);
        }
        return ResponseEntity.badRequest().body("Please upload a proper file");
    }

    @GetMapping
    public ResponseEntity<Resource> fetchAd(){
        Map<String, Object> response = adServiceImple.fetchAd();
        Resource resource = (Resource) response.get("resource");
        String contentType = (String) response.get("contentType");
        if(response.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body(null);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
