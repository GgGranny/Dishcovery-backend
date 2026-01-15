package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.*;
import com.dishcovery.backend.interfaces.ChatService;
import com.dishcovery.backend.model.Chat;
import com.dishcovery.backend.model.Community;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.enums.MessageStatus;
import com.dishcovery.backend.model.enums.MessageType;
import com.dishcovery.backend.repo.ChatRepo;
import com.dishcovery.backend.repo.CommunityRepo;
import com.dishcovery.backend.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatServiceImple implements ChatService {
    @Value("${file.chat.files}")
    private String chatFilesPath;

    private CommunityRepo communityRepo;
    private ChatRepo chatRepo;
    private UserRepo   userRepo;

    public ChatServiceImple(CommunityRepo communityRepo, ChatRepo chatRepo, UserRepo userRepo) {
        this.communityRepo = communityRepo;
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    @PostConstruct
    private void init() {
        File chat_files = new File(chatFilesPath);
        if(!chat_files.exists()) {
            chat_files.mkdir();
        }
    }

    @Override
    public Community createCommunity(CommunityRequestDto communityRequestDto) {
        Community community = new Community();
        community.setCommunityName(communityRequestDto.getCommunityName());
        community.setDescription(communityRequestDto.getDescription());
        community.setCategory(communityRequestDto.getCategory());
        community.setPrivate(communityRequestDto.isPrivate());
        community.setCreatedAt(LocalDateTime.now());
        Users user = userRepo.findByUsername(communityRequestDto.getUsername());
        community.setOwner(user);
        community.setTags(communityRequestDto.getTags());
        return communityRepo.save(community);
    }

    @Override
    public Chat saveChats(ChatMessage chatMessage, Long communityId) {
        Users user = userRepo.findByUsername(chatMessage.getSender());
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new RuntimeException("no such community"));
        Chat chat = new Chat();
        chat.setContent(chatMessage.getContent());
        chat.setCommunity(community);
        chat.setMessageStatus(MessageStatus.SENT);
        chat.setMessageType(MessageType.CHAT);
        chat.setSender(user);
        chat.setSentAt(LocalDateTime.now());
        return chatRepo.save(chat);
    }

    @Override
    public List<ChatResponseDto> getChatsByCommunity(Long communityId) {
        return List.of();
    }

    @Override
    public List<CommunityResponseDto> getCommunities(String title) {
        List<Community> communities = communityRepo.findAll();
        List<CommunityResponseDto> responseData = new ArrayList<>();
        for(Community community: communities){
            CommunityResponseDto communityResponseDto = new CommunityResponseDto();
            communityResponseDto.setId(community.getId());
            communityResponseDto.setCommunityName(community.getCommunityName());
            communityResponseDto.setDescription(community.getDescription());
            communityResponseDto.setCategory(community.getCategory());
            communityResponseDto.setPrivate(community.isPrivate());
            communityResponseDto.setCreatedAt(community.getCreatedAt());
            communityResponseDto.setTags(community.getTags());
            communityResponseDto.setUsername(community.getOwner().getUsername());
            communityResponseDto.setUserId(community.getOwner().getId());
            communityResponseDto.setUserProfile(community.getOwner().getProfilePicture());
            responseData.add(communityResponseDto);
        }
        return responseData;
    }

    @Override
    public List<Community> searchCommunity(String str) {
        return List.of();
    }

    @Override
    public Chat saveChatFiles(ChatFilesRequestDto chatFilesRequestDto, Long communityId, Principal principal) throws IOException {
        if(chatFilesRequestDto == null){
            throw new RuntimeException("chatFilesRequestDto can not be null");
        }
        Community community = communityRepo.findById(communityId)
                .orElseThrow(()-> new RuntimeException("no such community"));
        String uname = principal.getName();
        Users user = userRepo.findByUsername(uname);
        Chat chat = new Chat();
        chat.setContent(chatFilesRequestDto.getContent());
        chat.setCommunity(community);
        chat.setMessageStatus(MessageStatus.SENT);
        chat.setMessageType(MessageType.FILE);
        chat.setSender(user);
        chat.setSentAt(LocalDateTime.now());
        String randomUUID = UUID.randomUUID().toString();
        String fileName = randomUUID.concat("_").concat(chatFilesRequestDto.getFileName());
        Path path = Path.of(chatFilesPath.concat(fileName));
        copyChatFile(chatFilesRequestDto, path);
        chat.setFileName(fileName);
        chat.setFileType(chatFilesRequestDto.getFileType());
        chat.setUrl(path.toString());
        chat.setFileSize(chatFilesRequestDto.getFileSize());
        return chatRepo.save(chat);
    }
    private void copyChatFile(ChatFilesRequestDto chatFilesRequestDto, Path path) throws IOException {
        byte[] data = Base64.getDecoder().decode(chatFilesRequestDto.getFile());
        InputStream inputStream = new ByteArrayInputStream(data);
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public String fetchFiles(String path) throws IOException {
        Resource resource = new FileSystemResource(path);
        String encodedData = encodeBase64(resource);
        return encodedData;
    }

    private String encodeBase64(Resource resource) throws IOException {
        if(resource == null) {
            throw new RuntimeException("resource can not be null");
        }
        return Base64.getMimeEncoder().encodeToString(resource.getInputStream().readAllBytes());
    }


}
