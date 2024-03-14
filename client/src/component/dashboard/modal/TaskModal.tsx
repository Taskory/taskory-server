import React, {useEffect, useState} from "react";
import {Task} from "../TaskInterface";
import '../calendar.css';
import {useCookies} from "react-cookie";
import { TimeField } from "./component/TimeField";
import {TagField} from "./component/TagField";

interface TaskModalProps {
  closeModal: () => void,
  taskId: number | null
}

export const TaskModal: React.FC<TaskModalProps> = ({ closeModal, taskId }) => {
  const [cookies] = useCookies(['token']);
  const date = new Date();
  const currentDate = [
    date.getFullYear(),
    date.getMonth(),
    date.getDate(),
    date.getHours(),
    date.getMinutes()
  ];
  const [startDateArray, setStartDateArray] = useState<number[]>(currentDate);
  const [endDateArray, setEndDateArray] = useState<number[]>(currentDate);
  const [task, setTask] = useState<Task>({
    id: null,
    title: '',
    description: '',
    status: 'TODO',
    tags: [],
    startTime: startDateArray,
    endTime: endDateArray
  });

  useEffect(() => {
    setTask(prevTask => ({
      ...prevTask,
      startTime: startDateArray
    }));
  }, [startDateArray]);

  useEffect(() => {
    setTask(prevTask => ({
      ...prevTask,
      endTime: endDateArray
    }));
  }, [endDateArray]);

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

          ////////////////////////////////////////////////////////////////////////
          // test 용
          console.log(data);
          ////////////////////////////////////////////////////////////////////////
          setTask(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    }
  }, [cookies.token, taskId]);

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

  const handleUpdateTask = async () => {
    try {
      if (taskId) {

        const response = await fetch(`http://localhost:8080/api/v1/task/` + taskId.toString(), {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + cookies.token,
          },
          body: JSON.stringify(task),
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        alert("Success Save");
        handleCloseModal();
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }


  return (
    <>
      <div className="modal modal-open">
        <div className="modal-box">
          <p className="text-lg font-bold">Task detail</p>
          <button className="btn btn-sm btn-circle absolute right-4 top-4 text-lg font-bold bg-red-400" onClick={handleCloseModal}>X</button>
          <form method="dialog">
            <div className="mb-4">
              <label htmlFor="title" className="block text-sm font-medium text-gray-700">Title</label>
              <input type="text" name="title" id="title" value={task.title}
                     onChange={(e) => setTask({...task, title: e.target.value})}
                     className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
            </div>
            <div className="mb-4">
              <label htmlFor="description"
                     className="block text-sm font-medium text-gray-700">Description</label>
              <textarea name="description" id="description" value={task.description}
                        onChange={(e) => setTask(({...task, description: e.target.value}))}
                        className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
            </div>
            <div className="mb-4">
              <div className="w-full">
                <label htmlFor="status" className="block text-sm font-medium text-gray-700">Status</label>
                <select name="status" id="status" value={task.status}
                        onChange={(e) => setTask({...task, status: e.target.value})}
                        className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border">
                  <option value="TODO">TODO</option>
                  <option value="PROGRESS">PROGRESS</option>
                  <option value="DONE">DONE</option>
                </select>
              </div>
            </div>
            <div className="mb-4">
              <TagField />
            </div>
            <div className="mb-4 space-y-4">
              <div>
                <label htmlFor="startTime" className="block text-sm font-medium text-gray-700">Start Time</label>
                <TimeField date={task.startTime} setDate={(startTime) => setStartDateArray(startTime)}/>
              </div>
              <div>
                <label htmlFor="endTime" className="block text-sm font-medium text-gray-700">End Time</label>
                <TimeField date={task.endTime} setDate={(endTime) => setEndDateArray(endTime)}/>
              </div>
            </div>
          </form>
          {taskId ? (
            <button type="button" onClick={handleUpdateTask}
                    className="btn absolute right-4 bottom-4">
              Update Task
            </button>
          ) : (
            <button type="button" onClick={handleSaveTask}
                    className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
              Save Task
            </button>
          )}
        </div>
      </div>
    </>
  );
};


