import { Outlet } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { NavBar } from "./shared/components/NavBar";

function App() {
  return (
    <>
      <NavBar />
      <div className="container mt-3">
        <Outlet />
      </div>
    </>
  );
}

export default App;
