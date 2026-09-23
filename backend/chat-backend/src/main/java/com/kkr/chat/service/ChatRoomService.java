package com.kkr.chat.service;

import com.kkr.chat.entity.ChatRoom;
import com.kkr.chat.dto.ChatRoomCreateRequest;
import com.kkr.chat.dto.ChatRoomResponse;
import com.kkr.chat.dto.ChatRoomUpdateRequest;
import com.kkr.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public ChatRoomResponse create(ChatRoomCreateRequest request) {
        ChatRoom chatRoom = new ChatRoom(request.getName());
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.from(savedChatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAll() {
        return chatRoomRepository.findAll()
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse findById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        return ChatRoomResponse.from(chatRoom);
    }

    @Transactional
    public ChatRoomResponse update(Long id, ChatRoomUpdateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        chatRoom.updateName(request.getName());
        return ChatRoomResponse.from(chatRoom);
    }

    @Transactional
    public void delete(Long id) {
        chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        chatRoomRepository.deleteById(id);
    }
}