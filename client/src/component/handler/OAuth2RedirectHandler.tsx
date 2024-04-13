import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {getAuthCookie, setAuthCookie} from "../../util/CookieUtil";

export const OAuth2RedirectHandler: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const getUrlParameters = (names: string[]): { [key: string]: string | undefined } => {
    const params: { [key: string]: string | undefined } = {};
    names.forEach((name) => {
      const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
      const results = regex.exec(location.search);
      params[name] = results === null ? undefined : decodeURIComponent(results[1].replace(/\+/g, ' '));
    });
    return params;
  };

  useEffect(() => {
    const {token, forbidden} = getUrlParameters(["token", "forbidden"]);
    const {error} = getUrlParameters(["error"]);
    if (token && forbidden) {
      setAuthCookie(token);
      // console.log(forbidden);
      navigate("/socialconnect", {state : {data: forbidden}});
      // console.log(forbidden);
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
