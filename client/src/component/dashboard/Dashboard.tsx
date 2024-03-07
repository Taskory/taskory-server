import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {TaskCard} from "./TaskCard";
import {TaskModal} from "./TaskModal";
import {Task} from "./TaskInterface";

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const [tasks, setTasks] = useState<Task[]>([]);
  const [currentTaskId, setCurrentTaskId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

  const [cookies, setCookie] = useCookies();

  useEffect(() => {
    if (!cookies.token) {
      alert("로그인 정보가 잘못되었습니다");
      navigate("/");
    }

    fetch('http://localhost:8080/api/v1/task', {
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
        setTasks(data);
        // console.log('Success:', data);
      })
      .catch(error => {
        console.error('Error:', error);
      });

  }, []);

  const handleSaveTask = () => {
    const data: Task = {
      id: null,
      title: "Task Title",
      description: "Task Description",
      status: "PROGRESS", // TODO, PROGRESS, DONE 중 하나 선택
      tag: "Task Tag",
      startTime: [2024, 2, 15, 10, 0],
      endTime: [2024, 2, 15, 12, 0],
    };

    fetch('http://localhost:8080/api/v1/task', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': "Bearer " + cookies.token,
      },
      body: JSON.stringify(data),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        console.log('Success:', data);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

  const handleOpenModal = (id?: number | null) => {
    if (id) {
      setCurrentTaskId(id);
    }
    setIsModalOpen(true);
  }

  const handleCloseModal = () => {
    setIsModalOpen(false);
  }

  const handleInputChange = () => {

  }

  return (
    <>
      <div>
        <button className="p-4 bg-gray-200 rounded-full" onClick={() => {handleOpenModal()}}>
          Create Task
        </button>
        <div>
          {tasks.map((task) =>
            <TaskCard key={task.id} task={task} onClick={() => {handleOpenModal(task.id)}}/>
          )}
        </div>
      </div>
      {isModalOpen && (
        <TaskModal closeModal={handleCloseModal} saveTask={handleSaveTask} taskId={currentTaskId}/>
      )}
    </>
  );
};