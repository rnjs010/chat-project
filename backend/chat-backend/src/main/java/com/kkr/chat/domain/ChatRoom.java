package com.kkr.chat.domain;

public class ChatRoom {
    private Long id;
    private String name;

    public ChatRoom(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatRoom(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
