import React from "react";
import {useNavigate} from "react-router-dom";

export const SideBar: React.FC = () => {
  const navigate = useNavigate();

  const handleDashboard = () => {
    navigate("/dashboard")
  }
  const handleHome = () => {
    navigate("/");
  };
  return (
    <div className="h-screen flex flex-col justify-between bg-gray-800 text-white p-4 min-h-screen">
      {/* Sidebar Header */}
      <div className="mb-8">
        <button onClick={handleHome}>
          <h1 className="text-2xl font-semibold">TaskFlower </h1>
        </button>
        <p className="text-sm mt-2">Welcome, User</p>
      </div>

      {/* Sidebar Navigation */}
      <div>
        <button onClick={handleDashboard}
                className="block p-2 rounded hover:bg-gray-700 w-full">
          Dashboard
        </button>
        <button
          className="block p-2 rounded hover:bg-gray-700 w-full">
         ??
        </button>
        <button
          className="block p-2 rounded hover:bg-gray-700 w-full">
         ??
        </button>
      </div>

      {/* Sidebar Footer */}
      <div>
        <p className="text-xs">© 2024 Your Company</p>
      </div>
    </div>
  );
};