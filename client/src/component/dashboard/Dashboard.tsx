import React from "react";
import {TaskBoard} from "../task/TaskBoard";
import {TaskCalendar} from "../calendar/TaskCalendar";

export const Dashboard: React.FC = () => {
  return (
    <>
      <div className="w-full h-screen flex space-x-2">
        <div className="w-1/2 p-2 h-1/2">
          <TaskCalendar />
        </div>
        <div className="w-1/2 border p-2 h-1/2">
          <TaskBoard />
        </div>
      </div>
    </>
  );
};