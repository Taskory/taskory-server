import {tagInterface} from "./modal/TagModal/TagInterface";

export interface Task {
  id: number | null;
  title: string;
  description: string;
  status: string;
  tags: tagInterface[];
  startTime: number[];
  endTime: number[];
}