import React, {useEffect} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [cookies, setCookie] = useCookies();

  useEffect(() => {
    if (!cookies.token) {
      alert("로그인 정보가 잘못되었습니다");
      navigate("/");
    }
    const data = {
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
  }, []);


  return (
    <></>
  );
};