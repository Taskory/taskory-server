import React from "react";
import {getAuthCookie} from "../util/CookieUtil";
import {API_URL} from "../constants";

export const Home: React.FC = () => {
  const googleCalendarRequestOptions = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + getAuthCookie(),
    },
  }

  const requestGoogleCalendar = () => {
    try {
      fetch(API_URL + "/google/calendar", googleCalendarRequestOptions)
        // .then(res => {
        //   if (res.ok) {
        //     res.json()
        //       .then(result => {
        //         console.log(result);
        //       });
        //   }
        // });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <div>
        {/* Home Content Here */}
        <h2 className="text-3xl font-semibold text-center mb-4">Welcome to Our Website!</h2>
        <p className="text-center text-gray-700">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vel commodo lorem.
        </p>
        <button className="btn" onClick={requestGoogleCalendar}>google calendar</button>
      </div>
    </>
  );
};