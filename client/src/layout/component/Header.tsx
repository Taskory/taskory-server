import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";

export const Header: React.FC = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [cookies, setCookie] = useCookies(['token']);

  const navigate = useNavigate()

  useEffect(() => {
    if (cookies.token) {
      setIsLoggedIn(true);
    }
  }, [isLoggedIn, cookies.token]);

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };


  const handleLogout = () => {
    setCookie('token', null);
    setIsLoggedIn(false);
    navigate("/");
  };

  const handleProfile = () => {
    navigate('/profile');
  };

  const handleHome = () => {
    navigate('/');
  }


  return (
    <>
      <div className="navbar bg-base-100">
        <div className="flex-none">
          <button className="btn btn-square btn-ghost">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                 className="inline-block w-5 h-5 stroke-current">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
          </button>
        </div>
        <div className="flex-1" onClick={handleHome}>
          <p className="btn btn-ghost text-xl">Task Flower</p>
        </div>
        <div className="flex-none">
          <div className="join">
            {cookies.token ? (
              <>
                <button onClick={handleProfile}
                        className="join-item px-4 py-4 font-semibold btn-ghost">
                  Profile
                </button>
                <button onClick={handleLogout}
                        className="join-item px-4 py-4 font-semibold btn-ghost">
                  Logout
                </button>
                <button className="join-item btn  btn-ghost">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                       className="inline-block w-5 h-5 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                          d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
                  </svg>
                </button>
              </>
            ) : (
              <>
                <button onClick={handleLogin}
                        className="join-item px-4 py-4 font-semibold btn-ghost">
                  Login
                </button>
                <button onClick={handleSignup}
                        className="join-item px-4 py-4 font-semibold btn-ghost">
                  Signup
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
};