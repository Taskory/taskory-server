import React from 'react';
import './App.css';
import {BrowserRouter} from "react-router-dom";
import {MainLayout} from "./layout/MainLayout";

function App() {

  return (
    <div className="App">
      <BrowserRouter>
        <MainLayout />
      </BrowserRouter>
    </div>
  );
}

export default App;
