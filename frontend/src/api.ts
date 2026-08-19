import axios from 'axios';
import { TodoItem, TodoRequest, TodoStatus } from './types';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'
});

export async function getTodos(): Promise<TodoItem[]> {
  const response = await api.get<TodoItem[]>('/todos');
  return response.data;
}

export async function createTodo(payload: TodoRequest): Promise<TodoItem> {
  const response = await api.post<TodoItem>('/todos', payload);
  return response.data;
}

export async function updateTodo(id: number, payload: TodoRequest): Promise<TodoItem> {
  const response = await api.put<TodoItem>(`/todos/${id}`, payload);
  return response.data;
}

export async function deleteTodo(id: number): Promise<void> {
  await api.delete(`/todos/${id}`);
}

export async function updateStatus(id: number, status: TodoStatus): Promise<TodoItem> {
  const response = await api.patch<TodoItem>(`/todos/${id}/status`, { status });
  return response.data;
}
