import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useUserDeleteButton } from "@/pages/User/components/ProfileCard/UserDeleteButton/useUserDeleteButton.js";

export function UserDeleteButton() {
  const { t } = useTranslation();
  const { onClick, apiProgress } = useUserDeleteButton();

  return (
    <Button
      styleType={"danger"}
      text={t("delete")}
      onClick={onClick}
      apiProgress={apiProgress}
    />
  );
}
