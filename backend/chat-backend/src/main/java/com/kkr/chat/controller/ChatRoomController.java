package com.kkr.chat.controller;

import com.kkr.chat.dto.ChatRoomCreateRequest;
import com.kkr.chat.dto.ChatRoomResponse;
import com.kkr.chat.dto.ChatRoomUpdateRequest;
import com.kkr.chat.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping
    public List<ChatRoomResponse> findAll() {
        return chatRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ChatRoomResponse findById(@PathVariable Long id) {
        return chatRoomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomResponse create(@RequestBody ChatRoomCreateRequest request) {
        return chatRoomService.create(request);
    }

    @PutMapping("/{id}")
    public ChatRoomResponse update(@PathVariable Long id, @RequestBody ChatRoomUpdateRequest request) {
        return chatRoomService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        chatRoomService.delete(id);
    }
}
