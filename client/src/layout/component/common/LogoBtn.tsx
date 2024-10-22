import React from "react";
import {useNavigate} from "react-router-dom";

export const LogoBtn = () => {
    const navigate = useNavigate();

    const navigateToHome = () => {
        navigate("/");
    };

    return (
        <div className="flex items-center" onClick={navigateToHome}>
            <img className="mr-2 h-8 w-auto" src={"/asset/img/logo.png"} alt={"Logo"}/>
            <span className="font-semibold">Taskflower</span>
        </div>
    );
};