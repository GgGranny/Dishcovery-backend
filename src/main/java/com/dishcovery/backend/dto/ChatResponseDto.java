package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.MessageStatus;
import com.dishcovery.backend.model.enums.MessageType;

import java.time.LocalDateTime;

public class ChatResponseDto {
    private String content;
    private String sender;
    private Long chatId;
    private String user_profile;
    private int senderId;
    private MessageStatus status;
    private LocalDateTime sentAt;
    private MessageType type;

    private String fileName;
    private String fileType;
    private String fileSize;
    private String fileData;

    public ChatResponseDto() {
    }

    public ChatResponseDto(String content, String sender, Long chatId, String user_profile, int senderId, MessageStatus status, LocalDateTime sentAt, MessageType type, String fileName, String fileType, String fileSize, String fileData) {
        this.content = content;
        this.sender = sender;
        this.chatId = chatId;
        this.user_profile = user_profile;
        this.senderId = senderId;
        this.status = status;
        this.sentAt = sentAt;
        this.type = type;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileData = fileData;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "ChatResponseDto{" +
                "content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", chatId='" + chatId + '\'' +
                ", user_profile='" + user_profile + '\'' +
                ", senderId=" + senderId +
                ", status=" + status +
                ", sentAt=" + sentAt +
                '}';
    }
}
