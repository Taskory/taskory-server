import React, {useEffect, useState} from "react";
import {DayPicker} from "react-day-picker";

interface TimeFieldProps {
  date: number[];
  setDate: (date: number[]) => void
}

export const TimeField: React.FC<TimeFieldProps> = ({ date, setDate }) => {

  const dateFormat = (date: number[]) => {
    const formattedMonth = (date[1] + 1).toString().padStart(2, '0');
    const formattedDay = date[2].toString().padStart(2, '0');
    const formattedHour = date[3].toString().padStart(2, '0');
    const formattedMinute = date[4].toString().padStart(2, '0');

    return `${date[0]}.${formattedMonth}.${formattedDay} - ${formattedHour}:${formattedMinute}`;
  };

  const handleDayClick = (tempDate: Date) => {
    const newDate = [...date];
    newDate[0] = tempDate.getFullYear();
    newDate[1] = tempDate.getMonth();
    newDate[2] = tempDate.getDate();
    setDate(newDate);
  }

  const onChangeHours = (value: string) => {
    const newDate = [...date];
    newDate[3] = parseInt(value);
    setDate(newDate);
  }

  const onChangeMinutes = (value: string) => {
    const newDate = [...date];
    newDate[4] = parseInt(value);
    setDate(newDate);
  }

  return (
    <div className="">
      <details className="dropdown dropdown-bottom">
        <summary className="m-1 btn btn-sm">{dateFormat(date)}</summary>
        <ul className="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52">
          <DayPicker
            mode={"single"}
            selected={new Date(date[0],date[1], date[2])}
            onDayClick={(tempDate) => handleDayClick(tempDate)}
          />
          <div className="flex">
            <p className="mr-4 font-bold">Time: </p>
            <input
              type="number"
              value={date[3]}
              onChange={(e) => onChangeHours(e.target.value)}
              min={0}
              max={23}
            />
            <span className="mx-1">:</span>
            <input
              type="number"
              value={date[4]}
              onChange={(e) => onChangeMinutes(e.target.value)}
              min={0}
              max={59}
            />
          </div>
        </ul>
      </details>
    </div>
  );
};
