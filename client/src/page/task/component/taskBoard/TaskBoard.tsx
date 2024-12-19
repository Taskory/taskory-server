import React from "react";
import { TaskDragDropProvider } from "../../context/TaskDragDropContext";
import { HorizonBoard } from "./board/HorizonBoard";
import { TaskStatus } from "../../../../api/task/TaskTypes";
import { VerticalBoard } from "./board/VerticalBoard";

export const TaskBoard: React.FC = () => {
    return (
        <TaskDragDropProvider>
            <div className="h-full overflow-y-auto p-4">
                {/* Top container: Vertical Boards */}
                <div className="flex gap-4 p-4 min-h-[50%]">
                    <div className="flex-1 flex items-stretch">
                        <VerticalBoard boardStatus={TaskStatus.TODO}/>
                    </div>
                    <div className="flex-[2] flex items-stretch ">
                        <VerticalBoard boardStatus={TaskStatus.PROGRESS}/>
                    </div>
                </div>

                {/* Bottom container: Horizon Boards */}
                <div className="grid gap-4 p-4">
                    <HorizonBoard boardStatus={TaskStatus.BACKLOG}/>
                    <HorizonBoard boardStatus={TaskStatus.DONE}/>
                </div>

                {/* Footer Blank*/}
                <div className="h-20"/>
            </div>

        </TaskDragDropProvider>
    );
};