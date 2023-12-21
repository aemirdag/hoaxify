import { Outlet } from "react-router-dom";
import { NavBar } from "./shared/components/NavBar";
import { AuthenticationContext } from "@/shared/state/context.jsx";

function App() {
  return (
    <AuthenticationContext>
      <NavBar />
      <div className="container mt-3">
        <Outlet />
      </div>
    </AuthenticationContext>
  );
}

export default App;
