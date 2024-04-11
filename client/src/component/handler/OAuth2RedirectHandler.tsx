import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {getAuthCookie, setAuthCookie} from "../../util/CookieUtil";

export const OAuth2RedirectHandler: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const getUrlParameter = (name: string) => {
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const results = regex.exec(location.search);
    return results === null ? undefined : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };

  useEffect(() => {
    const token = getUrlParameter("token");
    const error = getUrlParameter("error");
    if (token) {
      setAuthCookie(token);
      // console.log(getAuthCookie());
      navigate("/");
    } else if (error) {
      navigate("/");
      console.log(error);
    } else {
      throw new Error("OAuth error")
    }
  });

  return (
    <></>
  );
};
