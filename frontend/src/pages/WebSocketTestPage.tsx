import { useRef, useState } from "react";
import ConnectionStatus from "../components/websocket/ConnectionStatus";
import MessageList from "../components/websocket/MessageList";
import type {
  ChatMessage,
  ConnectionStatus as ConnectionStatusType,
} from "../types/websocket";

const WS_URL = "ws://192.168.45.20:8080/ws/chat";

export default function WebSocketTestPage() {
  const socketRef = useRef<WebSocket | null>(null);

  const [status, setStatus] = useState<ConnectionStatusType>("DISCONNECTED");

  const [message, setMessage] = useState("");

  const [messages, setMessages] = useState<ChatMessage[]>([]);

  const connect = () => {
    if (socketRef.current?.readyState === WebSocket.OPEN) {
      return;
    }

    setStatus("CONNECTING");

    const socket = new WebSocket(WS_URL);

    socketRef.current = socket;

    socket.onopen = () => {
      setStatus("CONNECTED");
    };

    socket.onmessage = (event) => {
      const receivedMessage: ChatMessage = {
        id: Date.now() + Math.random(),
        content: event.data,
        sender: "OTHER",
        timestamp: new Date().toLocaleTimeString(),
      };

      setMessages((prev) => [...prev, receivedMessage]);
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

    socketRef.current.send(message);

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

            <p>Raw WebSocket 기반의 실시간 통신을 테스트합니다.</p>
          </div>

          <ConnectionStatus status={status} />
        </header>

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
