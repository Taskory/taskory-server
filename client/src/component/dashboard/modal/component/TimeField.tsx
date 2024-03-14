import React, {useEffect, useState} from "react";
import {DayPicker} from "react-day-picker";

interface TimeFieldProps {
  date: number[];
  setDate: (date: number[]) => void
}

export const TimeField: React.FC<TimeFieldProps> = ({ date, setDate }) => {
  const [isCalendarOpend, setIsCalendarOpend] = useState<boolean>(false);
  const [tempDate, setTempDate] = useState<Date>(new Date(date[0], date[1], date[2]));
  const [hours, setHours] = useState<number>(date[3]);
  const [minutes, setMinutes] = useState<number>(date[4]);

  useEffect(() => {
    setTempDate(new Date(date[0], date[1], date[2]));
    setHours(date[3]);
    setMinutes(date[4]);
    console.log(date);
  }, []);

  useEffect(() => {
    setDate([
      tempDate.getFullYear(),
      tempDate.getMonth(),
      tempDate.getDate(),
      (hours) ? hours : 0,
      (minutes) ? minutes : 0
    ]);
  }, [tempDate, hours, minutes]);

  const dateFormat = (date: number[]) => {
    const formattedMonth = (date[1] + 1).toString().padStart(2, '0');
    const formattedDay = date[2].toString().padStart(2, '0');
    const formattedHour = date[3].toString().padStart(2, '0');
    const formattedMinute = date[4].toString().padStart(2, '0');

    return `${date[0]}.${formattedMonth}.${formattedDay} - ${formattedHour}:${formattedMinute}`;
  };

  const handleSaveDateTime = () => {
    setIsCalendarOpend(false);
  };

  return (
    <div className="">
      <div className="">
        <p onClick={() => setIsCalendarOpend(prev => !prev)}>{dateFormat(date)}</p>
      </div>
      {isCalendarOpend && (
        <div>
          <DayPicker
            mode={"single"}
            selected={tempDate}
            onDayClick={(tempDate) => setTempDate(tempDate)}
          />
          <div className="flex">
            <p className="mr-4 font-bold">Time: </p>
            <input
              type="number"
              value={date[3]}
              onChange={(e) => setHours(parseInt(e.target.value))}
              min={0}
              max={23}
            />
            <span className="mx-1">:</span>
            <input
              type="number"
              value={date[4]}
              onChange={(e) => setMinutes(parseInt(e.target.value))}
              min={0}
              max={59}
            />
          </div>
          <button className="border rounded-lg p-1" onClick={handleSaveDateTime}>save</button>
        </div>
      )}
    </div>
  );
};
