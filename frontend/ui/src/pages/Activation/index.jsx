import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";

export function Activation() {
  const { t } = useTranslation();
  const { token } = useParams();

  return <div>{t("activation") + token}</div>;
}
