import type { ChatMessage } from "../../types/websocket";

interface MessageListProps {
  messages: ChatMessage[];
}

export default function MessageList({ messages }: MessageListProps) {
  return (
    <div className="message-list">
      {messages.length === 0 ? (
        <div className="empty-message">아직 메시지가 없습니다.</div>
      ) : (
        messages.map((message) => (
          <div
            key={message.id}
            className={`message ${
              message.sender === "ME" ? "message-me" : "message-other"
            }`}
          >
            <div className="message-content">{message.content}</div>

            <div className="message-time">{message.timestamp}</div>
          </div>
        ))
      )}
    </div>
  );
}
