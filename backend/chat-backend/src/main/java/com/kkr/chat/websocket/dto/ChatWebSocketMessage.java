package com.kkr.chat.websocket.dto;

public class ChatWebSocketMessage {
    private Long roomId;
    private String type;
    private String content;

    public ChatWebSocketMessage() {
    }

    public ChatWebSocketMessage(Long roomId, String type, String content) {
        this.roomId = roomId;
        this.type = type;
        this.content = content;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}