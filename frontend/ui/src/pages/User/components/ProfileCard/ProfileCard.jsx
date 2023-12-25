import defaultProfileImage from "@/assets/profile.png";
//import { useAuthState } from "@/shared/state/context.jsx";
import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import { useState } from "react";
import { Input } from "@/shared/components/Input.jsx";
import { updateUser } from "./api";
import { Alert } from "@/shared/components/Alert";
import { userUpdateSuccess } from "@/shared/state/redux";
import { ProfileImage } from "@/shared/components/ProfileImage";
import { UserEditForm } from "@/pages/User/components/ProfileCard/UserEditForm.jsx";

export function ProfileCard({ user }) {
  //const authState = useAuthState();
  const authState = useSelector((store) => store.auth);
  const [isEditMode, setIsEditMode] = useState(false);
  const { t } = useTranslation();

  const isEditButtonVisible = !isEditMode && authState.id === user.id;
  const visibleUsername =
    authState.id === user.id ? authState.username : user.username;

  return (
    <div className="card">
      <div className="card-header text-center">
        <ProfileImage width={200} />
      </div>
      <div className="card-body text-center">
        {!isEditMode && <span className="fs-3 d-block">{visibleUsername}</span>}
        {isEditButtonVisible && (
          <Button center text={t("edit")} onClick={() => setIsEditMode(true)} />
        )}
        {isEditMode && <UserEditForm setIsEditMode={setIsEditMode} />}
      </div>
    </div>
  );
}
