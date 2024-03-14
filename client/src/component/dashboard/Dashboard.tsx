import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {TaskCard} from "./TaskCard";
import {TaskModal} from "./modal/TaskModal";
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
      })
      .catch(error => {
        console.error('Error:', error);
      });

  }, [cookies.token, isModalOpen]);



  const handleOpenModal = (id?: number | null) => {
    if (id) {
      setCurrentTaskId(id);
    } else {
      setCurrentTaskId(null);
    }
    setIsModalOpen(true);
  }

  const handleCloseModal = () => {
    setIsModalOpen(false);
  }

  const [currentPage, setCurrentPage] = useState(1);
  const tasksPerPage = 5;
  const indexOfLastTask = currentPage * tasksPerPage;
  const indexOfFirstTask = indexOfLastTask - tasksPerPage;
  const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  }


  return (
    <>
      <div>
        <button className="btn btn-sm" onClick={() => handleOpenModal(null)}>
          Create Task
        </button>
        <div>
          {currentTasks.map((task) => (
            <TaskCard key={task.id} task={task} onClick={() => handleOpenModal(task.id)} />
          ))}
        </div>
        {/* Pagination */}
        <div className="w-full flex flex-row justify-center space-x-4">
          {Array.from({ length: Math.ceil(tasks.length / tasksPerPage) }).map((_, index) => (
            <button className={`btn ${index + 1 === currentPage ? 'font-black' : ''}`} key={index} onClick={() => handlePageChange(index + 1)}>{index + 1}</button>
          ))}
        </div>
      </div>
      {isModalOpen && (
        <TaskModal closeModal={handleCloseModal} taskId={currentTaskId} />
      )}
    </>
  );
};