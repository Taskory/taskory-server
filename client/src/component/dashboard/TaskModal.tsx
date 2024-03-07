import React, {useEffect, useState} from "react";
import { DayPicker } from "react-day-picker";
import {Task} from "./TaskInterface";
import './calendar.css';
import {useCookies} from "react-cookie";

interface TaskModalProps {
  closeModal: () => void,
  taskId: number | null
}

export const TaskModal: React.FC<TaskModalProps> = ({ closeModal, taskId }) => {
  const [cookies] = useCookies(['token']);
  const date = new Date();

  const initTask: Task = {
    id: null,
    title: '',
    description: '',
    status: 'TODO',
    tag: '',
    startTime: [
      date.getFullYear(),
      date.getMonth(),
      date.getDate(),
      date.getHours(),
      date.getMinutes()
    ],
    endTime: [
      date.getFullYear(),
      date.getMonth(),
      date.getDate(),
      date.getHours(),
      date.getMinutes()
    ]
  };
  const [task, setTask] = useState<Task>(initTask);

  useEffect(() => {
    if (taskId !== null) {
      fetch('http://localhost:8080/api/v1/task/' + taskId.toString(), {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': "Bearer " + cookies.token,
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          setTask(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    } else {
      setTask(initTask);
    }
  }, [taskId, task]);

  const handleCloseModal = () => {
    closeModal();
  };

  const handleSaveTask = () => {
    try {
      fetch('http://localhost:8080/api/v1/task', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': "Bearer " + cookies.token,
        },
        body: JSON.stringify(task),
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          alert("Success Save");
          return response.json();
        })
        .catch(error => {
          console.error('Error:', error);
        });
      handleCloseModal();
    } catch (e) {
      console.error(e);
    }
  }

  return (
    <>
      <div className={`fixed inset-0 z-50`}>
        <div className="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
          <div className="fixed inset-0 transition-opacity" aria-hidden="true">
            <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>

          <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

          <div
            className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              {/*<div className="sm:flex sm:items-start">*/}
              <div className="">
                <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                  <h3 className="text-lg leading-6 font-medium text-gray-900">Task detail</h3>
                  <div className="mt-2">
                    <div className="mb-4">
                      <label htmlFor="title" className="block text-sm font-medium text-gray-700">Title</label>
                      <input type="text" name="title" id="title" value={task.title} onChange={(e) => setTask({...task, title: e.target.value})}
                             className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
                    </div>
                    <div className="mb-4">
                      <label htmlFor="description"
                             className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea name="description" id="description" value={task.description} onChange={(e) => setTask(({...task, description: e.target.value}))}
                                className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
                    </div>
                    <div className="mb-4 flex w-full space-x-4">
                      <div className="w-full">
                        <label htmlFor="status" className="block text-sm font-medium text-gray-700">Status</label>
                        <select name="status" id="status" value={task.status} onChange={(e) => setTask({...task, status: e.target.value})}
                                className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border">
                          <option value="TODO">TODO</option>
                          <option value="PROGRESS">PROGRESS</option>
                          <option value="DONE">DONE</option>
                        </select>
                      </div>
                      <div className="w-full">
                        <label htmlFor="tag" className="block text-sm font-medium text-gray-700">Tag</label>
                        <input type="text" name="tag" id="tag" value={task.tag} onChange={(e) => setTask({...task, tag:e.target.value})}
                               className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
                      </div>
                    </div>
                    <div className="mb-4 space-y-4">
                      <div>
                        <label htmlFor="startTime" className="block text-sm font-medium text-gray-700">Start Time</label>
                        <TimeField date={task.startTime} setDate={(startTime) => setTask({...task, startTime})}/>
                      </div>
                      <div>
                        <label htmlFor="endTime" className="block text-sm font-medium text-gray-700">End Time</label>
                        <TimeField date={task.endTime} setDate={(endTime) => setTask({...task, endTime})}/>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button type="button" onClick={handleSaveTask}
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
                Save Task
              </button>
              <button type="button" onClick={handleCloseModal}
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm">
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
  date: number[];
  setDate: (date: number[]) => void
}

const TimeField: React.FC<TimeFieldProps> = ({date, setDate}) => {
  const [isCalendarOpend, setIsCalendarOpend] = useState<boolean>(false);
  const tempDate = new Date();
  const [hours, setHours] = useState<number>(date[3]);
  const [minutes, setMinutes] = useState<number>(date[4]);
  useEffect(() => {
    setDate([
      date ? date[0] : tempDate.getFullYear(),
      date ? date[1] : tempDate.getMonth(),
      date ? date[2] : tempDate.getDay(),
      date ? date[3] : tempDate.getHours(),
      date ? date[4] : tempDate.getMinutes()
    ]);
  }, [])

  const onDateChange = (tempDate: Date | undefined) => {
    if (tempDate) {
      setDate([
        tempDate.getFullYear(),
        tempDate.getMonth(),
        tempDate.getDay(),
        hours,
        minutes
      ]);
    }
  };

  const dateFormat = (date: number[]) => {
    const formattedMonth = date[1] < 10 ? `0${date[1]}` : date[1].toString();
    const formattedDay = date[2] < 10 ? `0${date[2]}` : date[2].toString();
    const formattedHour = date[3] < 10 ? `0${date[3]}` : date[3].toString();
    const formattedMinute = date[4] < 10 ? `0${date[4]}` : date[4].toString();

    return `${date[0]}.${formattedMonth}.${formattedDay} - ${formattedHour}:${formattedMinute}`;
  }


  const handleSaveDateTime = () => {
    setIsCalendarOpend(false);
  }

  return (
    <div className="" >
      <div className="">
        <p onClick={() => isCalendarOpend ? setIsCalendarOpend(false) : setIsCalendarOpend(true)}>{dateFormat(date)}</p>
      </div>
      <div>
        {isCalendarOpend && (
              <div>
                <DayPicker
                  selected={tempDate}
                  onDayClick={(tempDate) => {
                    onDateChange(tempDate)
                  }}
                />
                <div className="flex">
                  <p className="mr-4 font-bold">Time: </p>
                  <input
                    type="number"
                    value={hours}
                    onChange={(e) => setHours(parseInt(e.target.value))}
                    min={0}
                    max={23}
                  />
                  <span className="mx-1">:</span>
                  <input
                    type="number"
                    value={minutes}
                    onChange={(e) => setMinutes(parseInt(e.target.value))}
                    min={0}
                    max={59}
                  />
                </div>
                <button className="border rounded-lg p-1" onClick={() => handleSaveDateTime()}>save</button>
              </div>
        )}
      </div>
    </div>
  );
};
