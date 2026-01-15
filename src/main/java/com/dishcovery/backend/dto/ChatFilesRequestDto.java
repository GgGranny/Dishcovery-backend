package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.MessageType;
import org.springframework.web.multipart.MultipartFile;

public class ChatFilesRequestDto {
    private MessageType type;
    private String content;
    private String sender;
    private String file;
    private String fileName;
    private String fileSize;
    private String fileType;

    public ChatFilesRequestDto() {
    }

    public ChatFilesRequestDto(MessageType type, String content, String sender, String file, String fileName, String fileSize, String fileType) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.file = file;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ChatFilesRequestDto{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                 +
                '}';
    }
}
