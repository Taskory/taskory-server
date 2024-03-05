export interface Task {
  id: number | null;
  title: string;
  description: string;
  status: string;
  tag: string;
  startTime: number[];
  endTime: number[];
}