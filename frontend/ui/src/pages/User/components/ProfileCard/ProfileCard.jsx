import defaultProfileImage from "@/assets/profile.png";
//import { useAuthState } from "@/shared/state/context.jsx";
import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useSelector } from "react-redux";
import { useState } from "react";
import { Input } from "@/shared/components/Input.jsx";

export function ProfileCard({ user }) {
  //const authState = useAuthState();
  const authState = useSelector((store) => store.auth);
  const { t } = useTranslation();
  const [isEditMode, setIsEditMode] = useState(false);

  const isEditButtonVisible = !isEditMode && authState.id === user.id;

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
        <span className="fs-3 d-block">{user.username}</span>
        {isEditButtonVisible && (
          <Button center text={t("edit")} onClick={setIsEditMode(true)} />
        )}
        {isEditMode && (
          <>
            <Input />
            <Button center text={t("save")} />
            <div className={"d-inline m-1"}></div>
            <Button center text={t("cancel")} styleType={"outline-secondary"} />
          </>
        )}
      </div>
    </div>
  );
}
