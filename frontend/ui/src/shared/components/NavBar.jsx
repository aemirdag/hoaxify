import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import logo from "@/assets/hoaxify.png";
import { LanguageSelector } from "./LanguageSelector";
//import { useAuthDispatch, useAuthState } from "@/shared/state/context.jsx";
import { useDispatch, useSelector } from "react-redux";
import { logoutSuccess } from "@/shared/state/redux.js";
import {ProfileImage} from "@/shared/components/ProfileImage.jsx";

export function NavBar() {
  const { t } = useTranslation();
  //const authState = useAuthState();
  //const authDispatch = useAuthDispatch();
  const authState = useSelector((store) => store.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const onClickLogout = () => {
    dispatch(logoutSuccess());
    navigate("/");
  };

  return (
    <nav className="navbar navbar-expand bg-body-tertiary shadow-sm">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          <img src={logo} width={60} />
          Hoaxify
        </Link>
        <ul className="navbar-nav">
          {authState.id === 0 && (
            <>
              <li className="nav-item">
                <Link className="nav-link" to="/login">
                  {t("login")}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/signup">
                  {t("signUp")}
                </Link>
              </li>
            </>
          )}
          {authState.id > 0 && (
            <>
              <li className="nav-item">
                <Link className="nav-link" to={`/user/${authState.id}`}>
                  <ProfileImage width={30} />
                  <span className={"ms-2"}>{authState.username}</span>
                </Link>
              </li>
              <li className="nav-item">
                <span
                  className="nav-link"
                  role={"button"}
                  onClick={onClickLogout}
                >
                  {t("logOut")}
                </span>
              </li>
            </>
          )}

          <li className="nav-item">
            <LanguageSelector />
          </li>
        </ul>
      </div>
    </nav>
  );
}
