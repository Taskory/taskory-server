import React from "react";
import {Header} from "./component/Header";
import {Route, Routes, useLocation} from "react-router-dom";
import {Home} from "../component/Home";
import {Login} from "../component/Login";
import {Signup} from "../component/Signup";
import {Footer} from "./component/Footer";
import {Profile} from "../component/Profile";
import {SideBar} from "./component/SideBar";
import {Dashboard} from "../component/dashboard/Dashboard";

export const MainLayout: React.FC = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  if (currentPath !== '/login' && currentPath !== '/signup') {
    return (
      <>
        <div className="w-full">
          <Header />
        </div>
        <div className="flex">
          <SideBar />
          <div className="flex flex-col flex-1 min-h-screen">
            <main className="flex-1">
              <div className="container mx-auto py-8">
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/profile" element={<Profile />} />
                  <Route path="/dashboard" element={<Dashboard />} />
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