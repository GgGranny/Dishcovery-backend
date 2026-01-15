package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.MessageType;
import org.springframework.web.multipart.MultipartFile;

public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private Long communityId;


    public ChatMessage() {
    }

    public ChatMessage(MessageType type, String content, String sender, Long communityId) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.communityId = communityId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", communityId=" + communityId +
                '}';
    }
}
