export type ConnectionStatus =
  | "DISCONNECTED"
  | "CONNECTING"
  | "CONNECTED"
  | "ERROR";

export interface ChatMessage {
  id: number;
  content: string;
  sender: "ME" | "OTHER";
  timestamp: string;
}
