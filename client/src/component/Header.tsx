import React, {useEffect, useState} from "react";
import {useAuth} from "../context/AuthContext";

export const Header: React.FC = () => {
  const {token, login, logout} = useAuth();

  const handleLogin = () => {

  }

  const handleLogout = () => {
    logout();
  }

  return (
    <>
      <header className="bg-info text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-semibold">TaskFlower </h1>
          {token ? (
            <button onClick={handleLogout} className="px-4 py-2 bg-white text-info font-semibold rounded hover:bg-opacity-75">
              Logout
            </button>
          ) : (
            <button onClick={handleLogin} className="px-4 py-2 bg-white text-info font-semibold rounded hover:bg-opacity-75">
              Login
            </button>
          )}

        </div>
      </header>
    </>
  );
};