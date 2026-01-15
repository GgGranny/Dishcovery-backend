package com.dishcovery.backend.controller;

import com.dishcovery.backend.components.Pagination;
import com.dishcovery.backend.dto.ChatFilesRequestDto;
import com.dishcovery.backend.dto.ChatMessage;
import com.dishcovery.backend.dto.ChatResponseDto;
import com.dishcovery.backend.model.Chat;
import com.dishcovery.backend.repo.ChatRepo;
import com.dishcovery.backend.service.ChatServiceImple;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final ChatRepo chatRepo;
    private ChatServiceImple chatServiceImple;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(ChatServiceImple chatServiceImple, SimpMessagingTemplate simpMessagingTemplate, ChatRepo chatRepo) {
        this.chatServiceImple = chatServiceImple;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepo = chatRepo;
    }

    @MessageMapping("/sendMessage/{communityId}")
    public void send(@Payload ChatMessage msg,@DestinationVariable("communityId") Long id) throws Exception {
        System.out.println("communityId: "+ id);
        System.out.println("msg: "+ msg.toString());
        Chat chat = chatServiceImple.saveChats(msg, id);
        Map<String,Object> response = new HashMap<>();
        System.out.println(id);
        if(chat != null) {
            ChatResponseDto responseDto = new ChatResponseDto();
            responseDto.setContent(chat.getContent());
            responseDto.setSender(chat.getSender().getUsername());
            responseDto.setChatId(chat.getId());
            responseDto.setStatus(chat.getMessageStatus());
            responseDto.setSenderId(chat.getSender().getId());
            responseDto.setSentAt(chat.getSentAt());
            responseDto.setType(chat.getMessageType());
            responseDto.setUser_profile(chat.getSender().getProfilePicture());
            response.put("data", responseDto);
            response.put("status", "OK");
            simpMessagingTemplate.convertAndSend("/topic/public/"+id, response);
            return;
        }
        response.put("message", "Failed Sending Message");
        response.put("status", "ERROR");
        simpMessagingTemplate.convertAndSend("/topic/public/"+id, response);
    }

    @GetMapping("/api/{communityId}/chat")
    public ResponseEntity<List<ChatResponseDto>> getChats(@PathVariable("communityId") Long id, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize,Principal principal) throws IOException {
        List<Chat> chats = chatRepo.findAllByCommunity_Id(id)
                .orElseThrow(()-> new RuntimeException("No Chats Found For Community:" + id));
        List<Chat> chatsChunk = Pagination.paginate(chats, pageNumber, pageSize);
        List<ChatResponseDto> response = new ArrayList<>();
        for(Chat chat: chatsChunk) {
            ChatResponseDto dto = new ChatResponseDto();
            dto.setContent(chat.getContent());
            dto.setSender(chat.getSender().getUsername());
            dto.setChatId(chat.getId());
            dto.setStatus(chat.getMessageStatus());
            dto.setSenderId(chat.getSender().getId());
            dto.setSentAt(chat.getSentAt());
            dto.setUser_profile(chat.getSender().getProfilePicture());
            dto.setType(chat.getMessageType());
            if(chat.getUrl() != null) {
                dto.setFileType(chat.getFileType());
                dto.setFileName(chat.getFileName());
                dto.setFileSize(chat.getFileSize());
                dto.setFileData(chatServiceImple.fetchFiles(chat.getUrl()));
            }
            response.add(dto);
        }
        return ResponseEntity.ok(response);
    }

    @MessageMapping("/sendFiles/{communityId}")
    public void storeFiles(@Payload ChatFilesRequestDto payload, @DestinationVariable("communityId") Long id, Principal principal) {
        try {
            System.out.println(payload);
            System.out.println("communityId: "+ id);
            System.out.println("payload: "+ payload.toString());
            Chat chat = chatServiceImple.saveChatFiles(payload, id, principal);
            if(chat == null) {
                throw new RuntimeException("No Chat Found For Community:" + id);
            }
            Map<String , Object> response = new HashMap<>();
            ChatResponseDto responseDto = new ChatResponseDto();
            responseDto.setContent(chat.getContent());
            responseDto.setSender(chat.getSender().getUsername());
            responseDto.setChatId(chat.getId());
            responseDto.setStatus(chat.getMessageStatus());
            responseDto.setSenderId(chat.getSender().getId());
            responseDto.setSentAt(chat.getSentAt());
            responseDto.setUser_profile(chat.getSender().getProfilePicture());
            responseDto.setType(chat.getMessageType());
            responseDto.setFileType(chat.getFileType());
            responseDto.setFileName(chat.getFileName());
            responseDto.setFileSize(chat.getFileSize());
            responseDto.setFileData(chatServiceImple.fetchFiles(chat.getUrl()));

            response.put("data", responseDto);
            response.put("status", "OK");
            simpMessagingTemplate.convertAndSend("/topic/public/"+id, response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
