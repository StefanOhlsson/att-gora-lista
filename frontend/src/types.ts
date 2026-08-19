export type Priority = 'LAG' | 'NORMAL' | 'HOG' | 'KRITISK';
export type TodoStatus = 'REGISTRERAD' | 'STARTAD' | 'AVSLUTAD';

export interface StatusHistory {
  id: number;
  fromStatus: TodoStatus | null;
  fromStatusText: string | null;
  toStatus: TodoStatus;
  toStatusText: string;
  changedAt: string;
  message: string;
}

export interface TodoItem {
  id: number;
  title: string;
  description: string;
  priority: Priority;
  priorityText: string;
  status: TodoStatus;
  statusText: string;
  createdAt: string;
  history: StatusHistory[];
}

export interface TodoRequest {
  title: string;
  description: string;
  priority: Priority;
  status?: TodoStatus;
}
