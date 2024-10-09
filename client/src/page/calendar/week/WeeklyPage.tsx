import React, { useRef, useEffect, useState } from 'react';
import {WeekInfoInterface} from "./WeeklyInterface";
import {useCalendar} from "../context/CalendarContext";
import {getEventDayIndex, getWeeklyEvents, initializeWeekInfo } from "./WeeklyUtils";
import {EventSummary} from "../../../api/event/EventsTypes";
import { WeeklyHeader } from './WeeklyHeader';
import { WeeklyAllDayRow } from './WeeklyAllDayRow';
import { WeeklyColumns } from './WeeklyColumns';
import {TimeTableLayout} from "../common/TimeTableLayout";

export const WeeklyPage: React.FC = () => {
    const { currentDate, splitEvents } = useCalendar();
    const [weekInfo, setWeekInfo] = useState<WeekInfoInterface>(initializeWeekInfo(currentDate));
    const [under24hoursEvents, setUnder24hoursEvents] = useState<EventSummary[][]>([[], [], [], [], [], [], []]);
    const [over24hoursEvents, setOver24hoursEvents] = useState<EventSummary[][]>([[], [], [], [], [], [], []]);
    const scrollContainerRef = useRef<HTMLDivElement>(null);
    const [scrollBarWidth, setScrollBarWidth] = useState(0);

    useEffect(() => {
        setWeekInfo(initializeWeekInfo(currentDate));
    }, [currentDate]);

    useEffect(() => {
        const startingTimeOfWeek = new Date(weekInfo.startSunday);
        const endingTimeOfWeek = new Date(weekInfo.startSunday);
        endingTimeOfWeek.setDate(endingTimeOfWeek.getDate() + 6);
        endingTimeOfWeek.setHours(23, 59, 59);


        const newUnder24hoursEvents: EventSummary[][] = [[], [], [], [], [], [], []];
        getWeeklyEvents(splitEvents.eventsUnder24, weekInfo.startSunday).forEach((event: EventSummary) => {
            const eventStart = new Date(event.startDateTime);
            const eventEnd = new Date(event.dueDateTime);

            if (eventEnd >= startingTimeOfWeek && eventStart <= endingTimeOfWeek) {
                newUnder24hoursEvents[getEventDayIndex(eventStart, startingTimeOfWeek)].push(event);
            }
        });
        setUnder24hoursEvents(newUnder24hoursEvents);

        const newOver24hoursEvents: EventSummary[][] = [[], [], [], [], [], [], []];

        getWeeklyEvents(splitEvents.eventsOver24, weekInfo.startSunday).forEach((event: EventSummary) => {
            const eventStart: Date = new Date(event.startDateTime);
            const eventEnd: Date = new Date(event.dueDateTime);

            if (eventEnd >= startingTimeOfWeek && eventStart <= endingTimeOfWeek) {
                let currentDay: Date = new Date(eventStart);
                while (currentDay <= eventEnd && currentDay <= endingTimeOfWeek) {
                    if (currentDay >= startingTimeOfWeek) {
                        const dayIndex: number = getEventDayIndex(currentDay, startingTimeOfWeek);
                        newOver24hoursEvents[dayIndex].push(event);
                    }
                    currentDay.setDate(currentDay.getDate() + 1);
                }
            }
        });
        setOver24hoursEvents(newOver24hoursEvents);

    }, [splitEvents, weekInfo.startSunday]);


    useEffect(() => {
        if (scrollContainerRef.current) {
            const scrollbarWidth = scrollContainerRef.current.offsetWidth - scrollContainerRef.current.clientWidth;
            setScrollBarWidth(scrollbarWidth);
        }
    }, []);

    return (
        <div className="w-full flex-grow flex flex-col">
            {/* Header Section */}
            <WeeklyHeader scrollBarWidth={scrollBarWidth} startDate={weekInfo.startSunday}/>

            {/* All day events row */}
            <WeeklyAllDayRow scrollBarWidth={scrollBarWidth} allDayEvents={over24hoursEvents} />

            {/* Main Content: Time Table */}
            <TimeTableLayout containerRef={scrollContainerRef}>
                <WeeklyColumns under24hoursEvents={under24hoursEvents} />
            </TimeTableLayout>
        </div>
    );
};