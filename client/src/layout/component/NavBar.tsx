import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {SideBar} from "./SideBar";
import {useCookies} from "react-cookie";

export const NavBar: React.FC = () => {
  const navigate = useNavigate();
  const [cookies, setCookie] = useCookies(['token']);
  

  const handleHome = () => {
    navigate('/');
  }

  const handleLogin = () => {
    navigate('login');
  }

  const handleSignup = () => {
    navigate('signup')
  }

  const handleLogout = () => {
    setCookie('token', null);
    navigate("/");
  };

  const handleProfile = () => {
    navigate('/profile');
  };

  return (
    <>
      <div className="navbar bg-base-100">
        <div className="flex-none">
        <SideBar />
        </div>
        <div className="flex-1">
          <p className="btn btn-ghost text-xl" onClick={handleHome}>Task Flower</p>
        </div>
        <div className="flex-none">
            {cookies.token ? (
              <>
                <div className="dropdown dropdown-end">
                  <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar">
                    <div className="w-10 rounded-full">
                      <img alt="Tailwind CSS Navbar component" src="https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg" />
                    </div>
                  </div>
                  <ul tabIndex={0} className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52">
                    <li onClick={handleProfile}>
                      <p className="justify-between">
                        Profile
                        {/*<span className="badge">New</span>*/}
                      </p>
                    </li>
                    <li><p>Settings</p></li>
                    <li onClick={handleLogout}><p>Logout</p></li>
                  </ul>
                </div>
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
    </>
  );
};