import {TagInterface} from "./TagInterface";

export interface TaskInterface {
  id: number | null;
  title: string;
  description: string;
  status: string;
  tags: TagInterface[];
  startTime: number[];
  endTime: number[];
}