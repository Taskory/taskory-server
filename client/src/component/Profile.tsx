import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import googleLogo from "../assets/images/google-logo.png";
import {API_URL, GOOGLE_AUTH_URL} from '../constants';
import {existAuthCookie, getAuthCookie} from "../util/CookieUtil";

interface UserState {
  email: string;
  name: string;
}

export const Profile: React.FC = () => {
  const [userState, setUserState] = useState<UserState | null>(null);
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
              console.log(res.body)
              // res.json()
              //   .then(result => {
              //     console.log(result);
              //     setUserState(result);
              //     console.log(userState);
              //   });
            }

          });
      } catch (error) {
        console.error(error);
      }
    };
    requestProfile();
  }, [navigate, userState]);
  return (
    <>
      {userState ? (
        <p>{userState.name}</p>
      ) : <></>}
      {/*<div className="h-auto space-y-2 my-4">*/}
      {/*  <a className="flex justify-start btn w-full h-min" href={GOOGLE_AUTH_URL}>*/}
      {/*    <img className={"size-1/12"} src={googleLogo} alt="Google"/>*/}
      {/*    <p className={"ml-4"}>Log in with Google</p>*/}
      {/*  </a>*/}
      {/*</div>*/}
    </>
  );
};