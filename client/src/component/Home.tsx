import {Login} from "./Login";
import React from "react";
import {Signup} from "./Signup";
import {Footer} from "./Footer";
import {Header} from "./Header";

export const Home: React.FC = () => {
  return (
    <>
      <div className="min-h-screen">
        <Header/>
        <Banner/>
      </div>
      <Footer/>
    </>
  );
};

const Banner: React.FC = () => {
  return (
    <>
      <main className="flex-1 bg-blue-50">
        <div className="container mx-auto py-8">
          {/* Main Content Here */}
          <h2 className="text-3xl font-semibold text-center mb-4">Welcome to Our Website!</h2>
          <p className="text-center text-gray-700">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vel commodo lorem.
          </p>
        </div>
      </main>
    </>
  )
}
