import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export const Header: React.FC = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const navigate = useNavigate()

  useEffect(() => {
    if (sessionStorage.getItem("token")) {
      setIsLoggedIn(true);
    }
  }, [isLoggedIn]);

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };


  const handleLogout = () => {
    sessionStorage.removeItem("token");
    setIsLoggedIn(false);
  };

  return (
    <>
      <header className="bg-info text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-semibold">TaskFlower </h1>
          {sessionStorage.getItem("token") ? (
            <button onClick={handleLogout}
                    className="px-4 py-2 bg-white text-info font-semibold rounded hover:bg-opacity-75">
              Logout
            </button>
          ) : (
            <div className="flex space-x-2">
              <button onClick={handleLogin}
                      className="px-4 py-2 bg-white text-info font-semibold rounded hover:bg-opacity-75">
                Login
              </button>
              <button onClick={handleSignup}
                      className="px-4 py-2 bg-white text-info font-semibold rounded hover:bg-opacity-75">
                Signup
              </button>
            </div>

          )}

        </div>
      </header>
    </>
  );
};