package com.kkr.chat.dto;

import com.kkr.chat.entity.ChatRoom;

public class ChatRoomResponse {
    private Long id;
    private String name;

    public ChatRoomResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}