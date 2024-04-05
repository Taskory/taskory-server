import React from "react";
import {Route, Routes, useLocation} from "react-router-dom";
import {Home} from "../component/Home";
import {Login} from "../component/Login";
import {Signup} from "../component/Signup";
import {Footer} from "./component/Footer";
import {Profile} from "../component/Profile";
import {TaskBoard} from "../component/task/TaskBoard";
import {NavBar} from "./component/NavBar";
import {TaskCalendar} from "../component/calendar/TaskCalendar";
import {OAuth2RedirectHandler} from "../component/handler/OAuth2RedirectHandler";

export const MainLayout: React.FC = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  if (currentPath !== '/login' && currentPath !== '/signup') {
    return (
      <>
        <div className="w-full">
          <NavBar />
        </div>
        <div className="flex">
          <div className="flex flex-col flex-1 min-h-screen">
            <main className="flex-1">
              <div className="container mx-auto py-8">
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/profile" element={<Profile />} />
                  <Route path="/task" element={<TaskBoard />} />
                  <Route path="/calendar" element={<TaskCalendar />} />
                  <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
                </Routes>
              </div>
            </main>
          </div>
        </div>
        <Footer />
      </>
    );
  } else {
    return (
      <>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
      </>
    )

  }
};