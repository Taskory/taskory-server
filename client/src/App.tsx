import React from 'react';
import './App.css';
import {Home} from "./page/Home";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {Login} from "./page/Login";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
