import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {TaskCard} from "./TaskCard";
import {TaskModal} from "./modal/TaskModal/TaskModal";
import {Task} from "./TaskInterface";
import {TagModal} from "./modal/TagModal/TagModal";

export const TaskBoard: React.FC = () => {
  const navigate = useNavigate();

  const [tasks, setTasks] = useState<Task[]>([]);
  const [currentTaskId, setCurrentTaskId] = useState<number | null>(null);
  const [isTaskModalOpen, setIsTaskModalOpen] = useState<boolean>(false);
  const [isTagModalOpen, setIsTagModalOpen] = useState<boolean>(false);

  const [cookies] = useCookies();

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

  }, [cookies.token, isTaskModalOpen]);



  const handleOpenTaskModal = (id?: number | null) => {
    if (id) {
      setCurrentTaskId(id);
    } else {
      setCurrentTaskId(null);
    }
    setIsTaskModalOpen(true);
  }

  const handleCloseModal = () => {
    setIsTaskModalOpen(false);
  }

  const [currentPage, setCurrentPage] = useState(1);
  const tasksPerPage = 5;
  const indexOfLastTask = currentPage * tasksPerPage;
  const indexOfFirstTask = indexOfLastTask - tasksPerPage;
  const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  }

  const handleTagModal = () => {
    if (isTagModalOpen) {
      setIsTagModalOpen(false);
    } else {
      setIsTagModalOpen(true);
    }
  }


  return (
    <>
      <div>
        <div className="flex">
          <button className="btn btn-sm" onClick={() => handleOpenTaskModal(null)}>
            Create Task
          </button>
          <button className="btn btn-sm" onClick={() => handleTagModal()}>
            Tag manage
          </button>
        </div>
        <div>
          {currentTasks.map((task) => (
            <TaskCard key={task.id} task={task} onClick={() => handleOpenTaskModal(task.id)} />
          ))}
        </div>
        {/* Pagination */}
        <div className="w-full flex flex-row justify-center space-x-4">
          {Array.from({ length: Math.ceil(tasks.length / tasksPerPage) }).map((_, index) => (
            <button className={`btn ${index + 1 === currentPage ? 'font-black' : ''}`} key={index} onClick={() => handlePageChange(index + 1)}>{index + 1}</button>
          ))}
        </div>
      </div>
      {isTaskModalOpen && (
        <TaskModal closeModal={handleCloseModal} taskId={currentTaskId} />
      )}
      {isTagModalOpen && (
        <TagModal closeModal={handleTagModal} />
      )}
    </>
  );
};