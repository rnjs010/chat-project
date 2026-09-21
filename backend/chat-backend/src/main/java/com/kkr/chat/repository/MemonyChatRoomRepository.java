package com.kkr.chat.repository;

import com.kkr.chat.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemonyChatRoomRepository implements ChatRoomRepository {
    private final List<ChatRoom> chatRooms = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        Long id = idGenerator.incrementAndGet();

        ChatRoom savedChatRoom = new ChatRoom(id, chatRoom.getName());
        chatRooms.add(savedChatRoom);

        return savedChatRoom;
    }

    @Override
    public List<ChatRoom> findAll() {
        return new ArrayList<>(chatRooms);
    }

    @Override
    public Optional<ChatRoom> findById(Long id) {
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        chatRooms.removeIf(chatRoom -> chatRoom.getId().equals(id));
    }
}
