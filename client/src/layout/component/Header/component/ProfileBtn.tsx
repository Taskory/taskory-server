import {existAuthCookie} from "../../../../util/CookieUtil";
import React from "react";
import {useNavigate} from "react-router-dom";

export const ProfileBtn = () => {
    const navigate = useNavigate();
    const navigateToProfile = () => {
        navigate("/profile");
    };
    return (
        <>
            {existAuthCookie() && (
                <div
                    onClick={navigateToProfile}
                    className="flex items-center cursor-pointer hover:bg-gray-200 p-2 rounded-full"
                >
                    <img
                        src={"/asset/img/logo.png"}
                        alt={"Profile"}
                        className="h-8 w-8 rounded-full"
                    />
                </div>
            )}
        </>
    );
};