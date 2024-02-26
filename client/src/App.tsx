import React from 'react';
import './App.css';
import {Home} from "./page/Home";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {Login} from "./page/Login";
import {Signup} from "./page/Signup";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
