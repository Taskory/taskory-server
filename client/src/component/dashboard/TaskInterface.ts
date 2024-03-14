export interface Task {
  id: number | null;
  title: string;
  description: string;
  status: string;
  tags: string[];
  startTime: number[];
  endTime: number[];
}