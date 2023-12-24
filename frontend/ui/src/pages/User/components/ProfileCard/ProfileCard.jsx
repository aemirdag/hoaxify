import defaultProfileImage from "@/assets/profile.png";
//import { useAuthState } from "@/shared/state/context.jsx";
import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useSelector } from "react-redux";

export function ProfileCard({ user }) {
  //const authState = useAuthState();
  const authState = useSelector((store) => store.auth);
  const { t } = useTranslation();

  return (
    <div className="card">
      <div className="card-header text-center">
        <img
          src={defaultProfileImage}
          width={"200"}
          className="img-fluid rounded-circle shadow-sm"
        />
      </div>
      <div className="card-body text-center">
        <span className="fs-3">{user.username}</span>
        {authState.id === user.id && <Button center text={t("edit")} />}
      </div>
    </div>
  );
}
