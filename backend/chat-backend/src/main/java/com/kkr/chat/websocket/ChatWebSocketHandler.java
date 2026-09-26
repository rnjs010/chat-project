package com.kkr.chat.websocket;

import com.kkr.chat.websocket.dto.ChatWebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long roomId = extractRoomId(session);

        WebSocketSession safeSession = new ConcurrentWebSocketSessionDecorator(session, 10_000, 512 * 1024);
        rooms.computeIfAbsent(roomId, key -> new ConcurrentHashMap<>()).put(session.getId(), safeSession);

        System.out.println("WebSocket 연결 - sessionId: " + session.getId() + ", roomId: " + roomId);
        printRoomStatus(roomId);
    }


//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//        System.out.println("WebSocket 연결: " + session.getId());
//        System.out.println("현재 연결 수: " + sessions.size());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//        String payload = message.getPayload();
//        System.out.println("메시지 수신 - " + session.getId() + ": " + payload);
//
//        for (WebSocketSession connectedSession : sessions) {
//            if (connectedSession.isOpen()) {
//                connectedSession.sendMessage(new TextMessage(payload));
//            }
//        }
//    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long connectedRoomId = extractRoomId(session);
        String payload = message.getPayload();

        System.out.println("메시지 수신 - sessionId: " + session.getId() + ", roomId: " + connectedRoomId
                + ", payload: " + payload);

        ChatWebSocketMessage chatMessage = objectMapper.readValue(payload, ChatWebSocketMessage.class);

        if (!connectedRoomId.equals(chatMessage.getRoomId())) {
            System.out.println("잘못된 roomId 요청 - sessionId: " + session.getId());
            return;
        }

        if ("CHAT".equals(chatMessage.getType())) {
            broadcastToRoom(connectedRoomId, chatMessage);
        }
    }

//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.remove(session);
//        System.out.println("WebSocket 연결 종료: " + session.getId());
//        System.out.println("현재 연결 수: " + sessions.size());
//    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long roomId = extractRoomId(session);
        Map<String, WebSocketSession> room = rooms.get(roomId);
        if (room == null) {return;}

        room.remove(session.getId());
        if (room.isEmpty()) {rooms.remove(roomId);}

        System.out.println("WebSocket 연결 종료 - sessionId: " + session.getId() + ", roomId: " + roomId);
        printRoomStatus(roomId);
    }

    private void broadcastToRoom(Long roomId, ChatWebSocketMessage message) throws Exception {
        Map<String, WebSocketSession> room = rooms.get(roomId);

        if (room == null) {return;}
        String json = objectMapper.writeValueAsString(message);

        for (WebSocketSession session : room.values()) {
            if (!session.isOpen()) {continue;}
            session.sendMessage(new TextMessage(json));
        }
    }

    private Long extractRoomId(WebSocketSession session) {
        URI uri = session.getUri();

        if (uri == null) {
            throw new IllegalStateException("WebSocket URI가 없습니다.");
        }

        String path = uri.getPath();
        String[] parts = path.split("/");
        String roomId = parts[parts.length - 1];
        return Long.parseLong(roomId);
    }

    private void printRoomStatus(Long roomId) {
        Map<String, WebSocketSession> room = rooms.get(roomId);
        int count = room == null ? 0 : room.size();

        System.out.println("현재 room " + roomId + " 연결 수: " + count);
    }
}