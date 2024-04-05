import React, {useEffect} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useCookies} from "react-cookie";

export const OAuth2RedirectHandler: React.FC = () => {
  const [cookies, setCookie] = useCookies();
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
      setCookie("token", token);
      navigate("/");
    } else if(error) {
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
