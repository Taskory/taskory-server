import React from "react";

export const SideBar: React.FC = () => {
  return (
    <div className="drawer">
      <input id="my-drawer" type="checkbox" className="drawer-toggle"/>
      <div className="drawer-content">
        {/* Page content here */}
        <label htmlFor="my-drawer" className="btn btn-ghost drawer-button">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
               className="inline-block w-5 h-5 stroke-current">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path>
          </svg>
        </label>
      </div>
      <div className="drawer-side">
        <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay"></label>
        <ul className="menu p-4 w-80 min-h-full bg-base-200 text-base-content">
          <li><a href="/" className="justify-center text-lg">Home</a></li>
          <li><a href="/dashboard" className="justify-center text-lg">Dashboard</a></li>
          <li><a href="/task" className="justify-center text-lg">Task</a></li>
          <li><a href="/calendar" className="justify-center text-lg">Calendar</a></li>
        </ul>
      </div>
    </div>
  );
};