import React from "react";

export const SideBar: React.FC = () => {
  return (
    <div className="flex flex-col justify-between mr-4 w-auto">
      <ul className="menu border rounded-box h-full w-52 py-4">
        <li><a href="/dashboard" className="justify-center text-lg">DashBoard</a></li>
      </ul>
    </div>
  );
};