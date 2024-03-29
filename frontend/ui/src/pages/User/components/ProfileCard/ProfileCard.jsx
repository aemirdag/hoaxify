import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useSelector } from "react-redux";
import { useState } from "react";
import { ProfileImage } from "@/shared/components/ProfileImage";
import { UserEditForm } from "@/pages/User/components/ProfileCard/UserEditForm.jsx";
import { UserDeleteButton } from "@/pages/User/components/ProfileCard/UserDeleteButton/UserDeleteButton.jsx";

export function ProfileCard({ user }) {
  //const authState = useAuthState();
  const authState = useSelector((store) => store.auth);
  const [isEditMode, setIsEditMode] = useState(false);
  const { t } = useTranslation();
  const [tempImage, setTempImage] = useState("");

  const isLoggedInUser = !isEditMode && authState.id === user.id;
  const visibleUsername =
    authState.id === user.id ? authState.username : user.username;

  return (
    <div className="card">
      <div className="card-header text-center">
        <ProfileImage width={200} tempImage={tempImage} image={user.image} />
      </div>
      <div className="card-body text-center">
        {!isEditMode && <span className="fs-3 d-block">{visibleUsername}</span>}
        {isLoggedInUser && (
          <>
            <Button
              center
              text={t("edit")}
              onClick={() => setIsEditMode(true)}
            />
            <div className={"d-inline m-1"}></div>
            <UserDeleteButton />
          </>
        )}
        {isEditMode && (
          <UserEditForm
            setIsEditMode={setIsEditMode}
            setTempImage={setTempImage}
          />
        )}
      </div>
    </div>
  );
}
