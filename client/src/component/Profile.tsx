import {useCookies} from "react-cookie";
import {useEffect} from "react";

export const Profile: React.FC = () => {
  const [cookies, setCookie] = useCookies(['token']);




  useEffect(() => {
    const requsetOptions = {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + cookies.token
      },
      method: 'GET'
    };
    const requestProfile = async () => {
      await fetch("http://localhost:8080/api/v1/user/profile", requsetOptions)
        .then(res => {
          res.json().then(result => {
            console.log(result);
            return result;
            }
          )
        });
    };
    requestProfile()

  })

  return (
    <>
    </>
  );
};