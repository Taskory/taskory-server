import React, {useState} from "react";
import {Task} from "./TaskInterface";

interface TaskModalProps {
  closeModal: () => void
  inputChange: () => void
  saveTask: () => void
}

export const TaskModal: React.FC<TaskModalProps> = ({closeModal, inputChange, saveTask}) => {
  const [task, setTask] = useState<Task>({
    id: null,
    title: '',
    description: '',
    status: 'TODO',
    tag: '',
    startTime: [2024, 2, 15, 10, 0],
    endTime: [2024, 2, 15, 10, 0],
  });

  const handleCloseModal = () => {
    closeModal();
  };

  const handleInputChange = () => {
    inputChange();
  };

  const handleSaveTask = () => {
    saveTask();
  };



  const handleTimeChange = (index: number, value: number, timeType: 'startTime' | 'endTime') => {
    setTask(prevTask => {
      const newTask = { ...prevTask };
      if (timeType === 'startTime') {
        const newStartTime = [...newTask.startTime];
        newStartTime[index] = value;
        newTask.startTime = newStartTime;
      } else {
        const newEndTime = [...newTask.endTime];
        newEndTime[index] = value;
        newTask.endTime = newEndTime;
      }
      return newTask;
    });
  };

  return (
    <>
      <div className={`fixed inset-0 z-50 overflow-y-auto`}>
        <div className="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
          <div className="fixed inset-0 transition-opacity" aria-hidden="true">
            <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>

          <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

          <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              {/*<div className="sm:flex sm:items-start">*/}
                <div className="">
                <div className="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-indigo-100 sm:mx-0 sm:h-10 sm:w-10">
                  <svg className="h-6 w-6 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7"></path>
                  </svg>
                </div>
                <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                  <h3 className="text-lg leading-6 font-medium text-gray-900">Task detail</h3>
                  <div className="mt-2">
                    <div className="mb-4">
                      <label htmlFor="title" className="block text-sm font-medium text-gray-700">Title</label>
                      <input type="text" name="title" id="title" value={task.title} onChange={handleInputChange} className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border" />
                    </div>
                    <div className="mb-4">
                      <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea name="description" id="description" value={task.description} onChange={handleInputChange} className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border" />
                    </div>
                    <div className="mb-4 flex w-full space-x-4">
                      <div className="w-full">
                        <label htmlFor="status" className="block text-sm font-medium text-gray-700">Status</label>
                        <select name="status" id="status" value={task.status} onChange={handleInputChange} className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border">
                          <option value="TODO">TODO</option>
                          <option value="PROGRESS">PROGRESS</option>
                          <option value="DONE">DONE</option>
                        </select>
                      </div>
                      <div className="w-full">
                        <label htmlFor="tag" className="block text-sm font-medium text-gray-700">Tag</label>
                        <input type="text" name="tag" id="tag" value={task.tag} onChange={handleInputChange} className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border" />
                      </div>
                    </div>
                    <div className="mb-4">
                      <div>
                        <label htmlFor="startTime" className="block text-sm font-medium text-gray-700">Start Time</label>
                        <TimeField time={task.startTime} onTimeChange={(index, value) => handleTimeChange(index, value, 'startTime')} timeType={"startTime"} />
                      </div>
                      <div>
                        <label htmlFor="endTime" className="block text-sm font-medium text-gray-700">End Time</label>
                        <TimeField time={task.endTime} onTimeChange={(index, value) => handleTimeChange(index, value, 'endTime')} timeType="endTime" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button type="button" onClick={handleSaveTask} className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
                Save Task
              </button>
              <button type="button" onClick={handleCloseModal} className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm">
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};


interface TimeFieldProps {
  time: number[];
  onTimeChange: (index: number, value: number) => void;
  timeType: 'startTime' | 'endTime';
}

const TimeField: React.FC<TimeFieldProps> = ({ time, onTimeChange, timeType }) => {
  return (
    <div className="flex w-full space-x-1">
      <div className="mb-4 flex w-1/6">
        <input
          type="number"
          name={`${timeType}-year`}
          id={`${timeType}-year`}
          value={time[0]}
          onChange={(e) => onTimeChange(0, parseInt(e.target.value))}
          className="mt-1 p-1 border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 text-sm w-full"
        />
      </div>
      <div className="mb-4 flex w-1/12">
        <input
          type="number"
          name={`${timeType}-month`}
          id={`${timeType}-month`}
          value={time[1]}
          onChange={(e) => onTimeChange(1, parseInt(e.target.value))}
          className="mt-1 p-1 border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 text-sm w-full"
        />
      </div>
      <div className="mb-4 flex w-1/12">
        <input
          type="number"
          name={`${timeType}-day`}
          id={`${timeType}-day`}
          value={time[2]}
          onChange={(e) => onTimeChange(2, parseInt(e.target.value))}
          className="mt-1 p-1 border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 text-sm w-full"
        />
      </div>
      <div className="mb-4 flex w-1/12">
        <input
          type="number"
          name={`${timeType}-hour`}
          id={`${timeType}-hour`}
          value={time[3]}
          onChange={(e) => onTimeChange(3, parseInt(e.target.value))}
          className="mt-1 p-1 border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 text-sm w-full"
        />
      </div>
      <div className="mb-4 flex w-1/12">
        <input
          type="number"
          name={`${timeType}-minute`}
          id={`${timeType}-minute`}
          value={time[4]}
          onChange={(e) => onTimeChange(4, parseInt(e.target.value))}
          className="mt-1 p-1 border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 text-sm w-full"
        />
      </div>
    </div>
  );
};