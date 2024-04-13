import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {API_URL} from "../constants";
import {getAuthCookie} from "../util/CookieUtil";

// "/socialconnect"
export const SocialSignup: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [userId, setUserId] = useState<number | null>(null);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');

  useEffect(() => {
    const userInfo = JSON.parse(location.state.data);
    setUserId(userInfo.id);
    setEmail(userInfo.email ? userInfo.email : '');
    setName(userInfo.name ? userInfo.name : '');

  }, [location.state.data]);

  const fetchSignup = async () => {
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${getAuthCookie()}`
      },
      body: JSON.stringify({
        id: userId,
        name: name,
        email: email,
        password: password
      }),
    }

    try {
      await fetch(API_URL + "/auth/signup", requestOptions).then(res => {
        if (res.ok) {
          navigate('/');
        }
      })
    } catch (e) {
      alert("회원가입 오류");
    }

  }

  const handleSignup = async (event: React.FormEvent) => {
    event.preventDefault(); // 폼 제출 이벤트의 기본 동작 중지
    await fetchSignup();
  };



  return (
    <div className="flex items-center justify-center h-screen">
      <div className="p-8 rounded-lg shadow-md w-96">
        <h2 className="text-3xl font-semibold text-center mb-4">Sign Up</h2>
        <form onSubmit={handleSignup}>
          <div className="mb-4">
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">
              Name
            </label>
            <input
              type=""
              id="name"
              className="mt-1 p-2 w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-500"
              placeholder="Enter your name"
              value={name}
              onChange={e => setName(e.target.value)}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email address
            </label>
            <input
              type="email"
              id="email"
              className="mt-1 p-2 w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-500"
              placeholder="Enter your email"
              value={email}
              autoComplete={"current-email"}
              onChange={e => setEmail(e.target.value)}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
              Password
            </label>
            <input
              type="password"
              id="password"
              className="mt-1 p-2 w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-500"
              placeholder="Enter your password"
              value={password}
              autoComplete={"current-password"}
              onChange={e => setPassword(e.target.value)}
            />
          </div>
          <button
            type="submit"
            className="mt-4 w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none focus:bg-blue-600"
            onClick={handleSignup}
          >
            Sign up
          </button>
        </form>
      </div>
    </div>
  );
};