import { createBrowserRouter } from "react-router-dom";
import { Home } from "@/pages/Home/Home.jsx";
import { SignUp } from "@/pages/SignUp/SignUp.jsx";
import App from "@/App";
import { Activation } from "@/pages/Activation/Activation.jsx";
import { User } from "@/pages/User/User.jsx";
import { Login } from "@/pages/Login/Login.jsx";

export default createBrowserRouter([
  {
    path: "/",
    Component: App,
    children: [
      {
        path: "/",
        index: true,
        Component: Home,
      },
      {
        path: "/signup",
        Component: SignUp,
      },
      {
        path: "activation/:token",
        Component: Activation,
      },
      {
        path: "user/:id",
        Component: User,
      },
      {
        path: "login",
        Component: Login,
      },
    ],
  },
]);
