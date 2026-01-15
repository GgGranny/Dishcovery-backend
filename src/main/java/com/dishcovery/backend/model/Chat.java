package com.dishcovery.backend.model;

import com.dishcovery.backend.model.enums.MessageStatus;
import com.dishcovery.backend.model.enums.MessageType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Community community;

    private String content;

    @ManyToOne
    @JoinColumn(name="sender" ,nullable = false)
    private Users sender;

    @ManyToOne
    private Users receiver;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    private LocalDateTime sentAt;

    // File Type
    private String url;

    private String fileName;

    private String fileType;

    private String fileSize;

    public Chat() {
    }

    public Chat(Community community, String content, Users sender, Users receiver, MessageType messageType, MessageStatus messageStatus, String url, String fileName, String fileType, String fileSize, LocalDateTime sentAt) {
        this.community = community;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
        this.messageStatus = messageStatus;
        this.url = url;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getReceiver() {
        return receiver;
    }

    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                ", messageStatus=" + messageStatus +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }
}
