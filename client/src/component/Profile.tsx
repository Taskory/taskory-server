import {useCookies} from "react-cookie";
import {useEffect} from "react";

export const Profile: React.FC = () => {
  const [cookies, setCookie] = useCookies(['token']);


  useEffect(() => {
    const fetchLogin = async () => {
      const requestOptions = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email: "seongwon@gmail.com",
          password: "1234"
        }),
      }
      try {
        await fetch("http://localhost:8080/api/v1/auth/login", requestOptions).then(res => {
          if (res.ok) {
            res.json().then(result => {
              setCookie('token', result.token);
              // navigate("/");
            });
          } else {
            alert("이메일 및 비밀번호가 잘못되었습니다.")
          }
        })
      } catch (e) {
        alert("로그인 오류");
      }
    }

    fetchLogin();
    const requsetOptions = {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + cookies.token
      },
      method: 'GET'
    }
    // console.log(cookies.token);
    fetch("http://localhost:8080/api/v1/user/profile", requsetOptions)
      .then(res => {
        // console.log(res);
      })
  }, []);

  return (
    <>
    </>
  );
};