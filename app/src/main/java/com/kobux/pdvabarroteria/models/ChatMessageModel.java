package com.kobux.pdvabarroteria.models;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageModel {

    private String messageId;
    private String userId;
    private String userName;
    private String message;
    private long timestamp;
    private Map<String, Boolean> readBy;

    // Campos para respuesta (reply)
    private String replyToMessageId;
    private String replyToUserName;
    private String replyToText;

    public ChatMessageModel() {
        // Requerido por Firebase
    }

    public ChatMessageModel(String messageId,
                            String userId,
                            String userName,
                            String message,
                            long timestamp,
                            String replyToMessageId,
                            String replyToUserName,
                            String replyToText) {

        this.messageId = messageId;
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.replyToMessageId = replyToMessageId;
        this.replyToUserName = replyToUserName;
        this.replyToText = replyToText;
        this.readBy = new HashMap<>();
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Map<String, Boolean> getReadBy() { return readBy; }
    public void setReadBy(Map<String, Boolean> readBy) { this.readBy = readBy; }

    public String getReplyToMessageId() { return replyToMessageId; }
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }

    public String getReplyToUserName() { return replyToUserName; }
    public void setReplyToUserName(String replyToUserName) { this.replyToUserName = replyToUserName; }

    public String getReplyToText() { return replyToText; }
    public void setReplyToText(String replyToText) { this.replyToText = replyToText; }
}