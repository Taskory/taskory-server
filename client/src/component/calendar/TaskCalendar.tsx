import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import { TaskInterface } from "../../interface/TaskInterface";
import { useNavigate } from "react-router-dom";
import { useCookies } from "react-cookie";

interface EventInterface {
  id: number;
  title: string;
  start: Date;
  end: Date;
}

// Define localizer using moment
const localizer = momentLocalizer(moment);

export const TaskCalendar: React.FC = () => {
  const [events, setEvents] = useState<EventInterface[]>([]);
  const navigate = useNavigate();
  const [cookies] = useCookies();

  useEffect(() => {
    // Check if token exists in cookies
    if (!cookies.token) {
      alert("로그인 정보가 잘못되었습니다");
      navigate("/");
      return; // Return early if token is not present
    }

    // Fetch tasks data
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
        // Map tasks to events format required by react-big-calendar
        console.log(data);
        const mappedEvents = data
          .filter((task: TaskInterface) => task.id !== null) // Filter out tasks with null id
          .map((task: TaskInterface) => ({
            id: task.id!,
            title: task.title,
            start: new Date(task.startTime[0], task.startTime[1], task.startTime[2], task.startTime[3], task.startTime[4]), // Convert startTime to Date object
            end: new Date(task.endTime[0], task.endTime[1], task.endTime[2], task.endTime[3], task.endTime[4]), // Convert endTime to Date object
          }));
        setEvents(mappedEvents);
        console.log(mappedEvents);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }, [cookies.token, navigate]);

  return (
    <div style={{ height: 500 }}>
      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="start"
        endAccessor="end"
        style={{ margin: '50px' }}
      />
    </div>
  );
};
