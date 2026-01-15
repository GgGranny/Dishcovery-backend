package com.dishcovery.backend.controller;

import com.dishcovery.backend.dto.CommentResponseDto;
import com.dishcovery.backend.dto.CommunityRequestDto;
import com.dishcovery.backend.dto.CommunityResponseDto;
import com.dishcovery.backend.model.Community;
import com.dishcovery.backend.service.ChatServiceImple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    private ChatServiceImple chatServiceImple;

    public CommunityController(ChatServiceImple chatServiceImple) {
        this.chatServiceImple = chatServiceImple;
    }

    @GetMapping
    public ResponseEntity<List<CommunityResponseDto>> getCommunities() {
        List<CommunityResponseDto> response = chatServiceImple.getCommunities("hello");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody CommunityRequestDto CommunityRequestDto) {
        Community community = chatServiceImple.createCommunity(CommunityRequestDto);
        Map<String, String> response = new HashMap<>();
        if(community != null) {
            response.put("status", HttpStatus.CREATED.toString());
            response.put("message", community.getCommunityName() + " created successfully");
            return ResponseEntity.ok(response);
        }
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("message", "Community creation failed");
        return ResponseEntity.badRequest().body(response);
    }
}
