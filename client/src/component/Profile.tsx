import React, {useEffect, useState} from 'react';
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";

interface UserState {
  email: string;
  name: string;
}

export const Profile: React.FC = () => {
  const [userState, setUserState] = useState<UserState | null>(null);
  const [cookies] = useCookies(['token']);
  const navigate = useNavigate();
  useEffect(() => {
    if (!cookies.token) {
      navigate("/");
    }

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + cookies.token,
      },
    }

    const requestProfile = async () => {
      try {
        await fetch("http://localhost:8080/api/v1/user/profile", requestOptions)
          .then(res => {
            if (res.ok) {
              res.json()
                .then(result => {
                  console.log(result);
                  setUserState(result);
                  console.log(userState);
                });
            }

          });
      } catch (error) {
        console.error(error);
      }
    };

    requestProfile();
  }, [cookies.token]);
  return (
    <div className="flex justify-between px-30 sm:px-60">
      {/* Sidebar */}
      <div className="text-black w-72">
        <img src="profile_image_url" alt="Profile" className="w-16 h-16 rounded-full mr-4" />
        <div className="p-6 flex items-center justify-between">
          <div className="flex items-center">
            <div>
              <h2 className="text-lg font-semibold">User Name</h2>
              <p className="text-sm">User Role</p>
            </div>
          </div>
          <button className="focus:outline-none">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" />
          </button>
        </div>
        <nav className="mt-8">
          <ul>
            <li className="px-4 py-2 hover:bg-gray-700">
              <a href="#" className="block">
                Dashboard
              </a>
            </li>
            <li className="px-4 py-2 hover:bg-gray-700">
              <a href="#" className="block">
                Projects
              </a>
            </li>
            <li className="px-4 py-2 hover:bg-gray-700">
              <a href="#" className="block">
                Settings
              </a>
            </li>
            {/* Add more menu items here */}
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 p-4 sm:p-8 border-2 rounded-lg">
        <h1 className="text-2xl font-semibold mb-4">User Profile</h1>
        {/* Add user profile content here */}
      </div>
    </div>
  );
};