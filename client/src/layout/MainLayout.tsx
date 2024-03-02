import React, {useEffect, useState} from "react";
import {Header} from "./Header";
import {Route, Routes, useLocation} from "react-router-dom";
import {Home} from "../component/Home";
import {Login} from "../component/Login";
import {Signup} from "../component/Signup";
import {Footer} from "./Footer";
import {Profile} from "../component/Profile";

export const MainLayout: React.FC = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  if (currentPath !== '/login' && currentPath !== '/signup') {
    return (
      <>
        <Header/>
<<<<<<< HEAD
        <main className="flex-1">
=======
        <main className="flex-1 bg-blue-50">
>>>>>>> feat/task
          <div className="container mx-auto py-8 min-h-screen">
            <Routes>
              <Route path="/" element={<Home/>}/>
              <Route path="/profile" element={<Profile/>}/>
            </Routes>
          </div>
        </main>
        <Footer/>
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