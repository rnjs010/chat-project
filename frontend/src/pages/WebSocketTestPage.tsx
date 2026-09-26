import { useRef, useState } from "react";
import ConnectionStatus from "../components/websocket/ConnectionStatus";
import MessageList from "../components/websocket/MessageList";
import type {
  ChatMessage,
  ConnectionStatus as ConnectionStatusType,
  WebSocketMessage,
} from "../types/websocket";

const WS_BASE_URL = "ws://192.168.45.20:8080/ws/chat";

export default function WebSocketTestPage() {
  const socketRef = useRef<WebSocket | null>(null);

  const [status, setStatus] = useState<ConnectionStatusType>("DISCONNECTED");
  const [roomId, setRoomId] = useState(1);
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);

  const connect = () => {
    if (socketRef.current?.readyState === WebSocket.OPEN) {
      return;
    }

    setStatus("CONNECTING");

    const socket = new WebSocket(WS_BASE_URL + `/${roomId}`);

    socketRef.current = socket;

    socket.onopen = () => {
      setStatus("CONNECTED");
    };

    socket.onmessage = (event) => {
      try {
        const receivedMessage: WebSocketMessage = JSON.parse(event.data);

        const receivedUiMessage: ChatMessage = {
          id: Date.now() + Math.random(),
          content: receivedMessage.content,
          sender: "OTHER",
          timestamp: new Date().toLocaleTimeString(),
        };

        setMessages((prev) => [...prev, receivedUiMessage]);
      } catch (error) {
        console.error("메시지 파싱 실패", error);
      }
    };

    socket.onerror = () => {
      setStatus("ERROR");
    };

    socket.onclose = () => {
      setStatus("DISCONNECTED");
      socketRef.current = null;
    };
  };

  const disconnect = () => {
    socketRef.current?.close();
  };

  const sendMessage = () => {
    if (!socketRef.current || socketRef.current.readyState !== WebSocket.OPEN) {
      return;
    }

    if (!message.trim()) {
      return;
    }

    const chatMessage: WebSocketMessage = {
      roomId,
      type: "CHAT",
      content: message,
    };

    socketRef.current.send(JSON.stringify(chatMessage));

    /*
     * Raw WebSocket에서는 내가 보낸 메시지를 즉시 화면에 표시
     */
    const sentMessage: ChatMessage = {
      id: Date.now(),
      content: message,
      sender: "ME",
      timestamp: new Date().toLocaleTimeString(),
    };

    setMessages((prev) => [...prev, sentMessage]);
    setMessage("");
  };

  return (
    <main className="websocket-page">
      <section className="chat-container">
        <header className="chat-header">
          <div>
            <span className="chat-label">WEBSOCKET TEST</span>
            <h1>실시간 메시지 테스트</h1>
            <p>Raw WebSocket 기반의 실시간 통신 테스트</p>
          </div>

          <ConnectionStatus status={status} />
        </header>

        <div className="room-selector">
          <span>채팅방</span>

          <select
            value={roomId}
            disabled={status === "CONNECTED"}
            onChange={(event) => setRoomId(Number(event.target.value))}
          >
            <option value={1}>Room 1</option>
            <option value={2}>Room 2</option>
          </select>
        </div>

        <div className="chat-body">
          <MessageList messages={messages} />
        </div>

        <div className="chat-footer">
          <div className="connection-buttons">
            <button onClick={connect} disabled={status === "CONNECTED"}>
              연결
            </button>

            <button onClick={disconnect} disabled={status !== "CONNECTED"}>
              연결 종료
            </button>
          </div>

          <div className="message-form">
            <input
              value={message}
              onChange={(event) => setMessage(event.target.value)}
              onKeyDown={(event) => {
                if (event.key === "Enter") {
                  sendMessage();
                }
              }}
              placeholder="메시지를 입력하세요"
              disabled={status !== "CONNECTED"}
            />

            <button onClick={sendMessage} disabled={status !== "CONNECTED"}>
              전송
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}
