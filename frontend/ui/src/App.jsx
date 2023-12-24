import { Outlet } from "react-router-dom";
import { NavBar } from "./shared/components/NavBar";
//import { AuthenticationContext } from "@/shared/state/context.jsx";
import { Provider } from "react-redux";
import { store } from "@/shared/state/redux.js";

function App() {
  return (
    //<AuthenticationContext>
    <Provider store={store}>
      <NavBar />
      <div className="container mt-3">
        <Outlet />
      </div>
    </Provider>
    //</AuthenticationContext>
  );
}

export default App;
