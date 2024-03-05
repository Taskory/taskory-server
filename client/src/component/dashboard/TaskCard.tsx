import React from 'react';
import {Task} from "./TaskInterface";

interface TaskCardProps {
  task: Task;
}

export const TaskCard: React.FC<TaskCardProps> = ({ task }) => {
  const getStatusColor = (status: string): string => {
    switch (status) {
      case 'TODO':
        return 'bg-blue-200 text-blue-800';
      case 'PROGRESS':
        return 'bg-yellow-200 text-yellow-800';
      case 'DONE':
        return 'bg-green-200 text-green-800';
      default:
        return 'bg-gray-200 text-gray-800';
    }
  };

  return (
    <div className="border rounded p-4 mb-4">
      <h2 className="text-lg font-semibold">{task.title}</h2>
      <p className="text-gray-600">{task.description}</p>
      <div className="flex items-center mt-2">
        <span className={`px-2 py-1 rounded text-sm ${getStatusColor(task.status)}`}>
          {task.status}
        </span>
        <span className="text-gray-500 ml-2">{task.tag}</span>
      </div>
      <div className="mt-4">
        <p className="text-sm text-gray-500">Start Time: {task.startTime}</p>
        <p className="text-sm text-gray-500">End Time: {task.endTime}</p>
      </div>
    </div>
  );
};
