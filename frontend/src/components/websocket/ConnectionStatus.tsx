import type { ConnectionStatus } from "../../types/websocket";

interface ConnectionStatusProps {
  status: ConnectionStatus;
}

const statusText: Record<ConnectionStatus, string> = {
  DISCONNECTED: "연결되지 않음",
  CONNECTING: "연결 중...",
  CONNECTED: "연결됨",
  ERROR: "연결 오류",
};

export default function ConnectionStatus({ status }: ConnectionStatusProps) {
  return (
    <div className={`connection-status ${status.toLowerCase()}`}>
      <span className="status-dot" />
      <span>{statusText[status]}</span>
    </div>
  );
}
