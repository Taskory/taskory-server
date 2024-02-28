import {useCookies} from "react-cookie";
import {useEffect} from "react";

export const Profile: React.FC = () => {
  const [cookies] = useCookies(['token']);

  useEffect(() => {
    console.log(cookies.token);
  }, [cookies]);

  return (
    <>
    </>
  );
};