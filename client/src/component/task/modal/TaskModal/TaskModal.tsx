import React, {useEffect, useState} from "react";
import {TaskInterface} from "../../../../interface/TaskInterface";
import '../../calendar.css';
import {useCookies} from "react-cookie";
import { TimeField } from "./component/TimeField";
import {TagField} from "./component/TagField";
import {TagInterface} from "../../../../interface/TagInterface";

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
  const [tags, setTags] = useState<TagInterface[]>([]);

  const [task, setTask] = useState<TaskInterface>({
    id: null,
    title: '',
    description: '',
    status: 'TODO',
    tags: [],
    startTime: startDateArray,
    endTime: endDateArray
  });

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
    }
  }, [cookies.token, taskId]);

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
    setTask(prevTask => ({
      ...prevTask,
      tags: tags
    }));
  }, [tags]);



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

  const handleUpdateTask = () => {
    try {
      if (taskId) {

        fetch(`http://localhost:8080/api/v1/task/` + taskId.toString(), {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + cookies.token,
          },
          body: JSON.stringify(task),
        }).then(response => {
          response.json().then(result => {
            console.log(result);
          })
        });
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
          <button className="btn btn-sm btn-circle absolute right-4 top-4 text-lg font-bold bg-red-400" onClick={handleCloseModal}>X</button>
          <div>
            <p className="text-lg font-bold">Task detail</p>
          </div>
          <form method="dialog">
            <div className="mb-4 my-4 flex justify-start">
              <div>
                <label htmlFor="startTime" className="block text-sm font-medium text-gray-700">Start Time</label>
                <TimeField date={task.startTime} setDate={(startTime) => setStartDateArray(startTime)}/>
              </div>
              <div>
                <label htmlFor="endTime" className="block text-sm font-medium text-gray-700">End Time</label>
                <TimeField date={task.endTime} setDate={(endTime) => setEndDateArray(endTime)}/>
              </div>
            </div>
            <div className="mb-4">
              <label htmlFor="title" className="block text-sm font-medium text-gray-700">Title</label>
              <input type="text" name="title" id="title" value={task.title}
                     onChange={(e) => setTask({...task, title: e.target.value})}
                     className="input mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 "/>
            </div>
            <div className="mb-4">
              <label htmlFor="description"
                     className="block text-sm font-medium text-gray-700">Description</label>
              <textarea name="description" id="description" value={task.description}
                        onChange={(e) => setTask(({...task, description: e.target.value}))}
                        className="textarea mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"/>
            </div>
            <div className="mb-4">
              <div className="w-full">
                <label htmlFor="status" className="block text-sm font-medium text-gray-700">Status</label>
                <select name="status" id="status" value={task.status}
                        onChange={(e) => setTask({...task, status: e.target.value})}
                        className="select mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border">
                  <option value="TODO">TODO</option>
                  <option value="PROGRESS">PROGRESS</option>
                  <option value="DONE">DONE</option>
                </select>
              </div>
            </div>
            <div className="mb-4">
              <TagField selectedTags={task.tags} setSelectedTags={(tags) => setTags(tags)}/>
            </div>
          </form>
          {taskId ? (
            <button type="button" onClick={handleUpdateTask}
                    className="btn absolute right-4 bottom-4">
              Update Task
            </button>
          ) : (
            <button type="button" onClick={handleSaveTask}
                    className="btn absolute right-4 bottom-4">
              Save Task
            </button>
          )}
        </div>
      </div>
    </>
  );
};


