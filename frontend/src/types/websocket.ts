export type ConnectionStatus =
  | "DISCONNECTED"
  | "CONNECTING"
  | "CONNECTED"
  | "ERROR";

// 화면 표시용
export interface ChatMessage {
  id: number;
  content: string;
  sender: "ME" | "OTHER";
  timestamp: string;
}

// 서버 통신용
export type WebSocketMessage = {
  roomId: number;
  type: "CHAT" | "JOIN" | "LEAVE";
  content: string;
};
