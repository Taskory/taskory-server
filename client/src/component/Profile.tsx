import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import googleLogo from "../assets/images/google-logo.png";
import {API_URL, GOOGLE_AUTH_URL} from '../constants';
import {existAuthCookie, getAuthCookie} from "../util/CookieUtil";

interface UserState {
  email: string;
  name: string;
  socialProviders: string[];
}

export const Profile: React.FC = () => {
  const [userInfo, setUserInfo] = useState<UserState>({email: "", name: "", socialProviders: []});
  const navigate = useNavigate();

  useEffect(() => {
    if (!existAuthCookie()) {
      navigate("/");
    }

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getAuthCookie(),
      },
    }

    const requestProfile = () => {
      try {
        fetch(API_URL + "/user/profile", requestOptions)
          .then(res => {
            if (res.ok) {
              res.json()
                .then(result => {
                  console.log(result);
                  setUserInfo({email: result.email, name: result.name, socialProviders: result.socialProviders});
                });
            }
          });
      } catch (error) {
        console.error(error);
      }
    };
    requestProfile();
  }, [navigate]);
  return (
    <>
      <p>Name : {userInfo.name}</p>
      <p>Email : {userInfo.email}</p>
      <p>connected social accounts :</p>
      <ul>
        {userInfo.socialProviders.map((provider, index) => (
          <li key={index}>{provider}</li>
        ))}
      </ul>
      <div className="h-auto space-y-2 my-4">
        {/*<a className="flex justify-start btn w-full h-min" href={GOOGLE_AUTH_URL}>*/}
        {/*  <img className={"size-1/12"} src={googleLogo} alt="Google"/>*/}
        {/*  <p className={"ml-4"}>Log in with Google</p>*/}
        {/*</a>*/}
      </div>
    </>
  );
};