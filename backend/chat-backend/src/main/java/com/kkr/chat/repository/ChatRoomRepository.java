package com.kkr.chat.repository;

import com.kkr.chat.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    List<ChatRoom> findAll();
    Optional<ChatRoom> findById(Long id);
    void deleteById(Long id);
}
