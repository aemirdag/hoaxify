import { deleteUser } from "@/pages/User/components/ProfileCard/UserDeleteButton/api.js";
import { useAuthState } from "@/shared/state/context.jsx";
import { useCallback, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logoutSuccess } from "@/shared/state/redux.js";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

export function useUserDeleteButton() {
  const { id } = useSelector((store) => store.auth);
  const [apiProgress, setApiProgress] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const onClick = useCallback(async () => {
    try {
      const result = confirm(t("confirm"));
      if (!result) {
        return;
      }

      setApiProgress(true);
      await deleteUser(id);
      dispatch(logoutSuccess());
      navigate("/");
    } catch {
      /* empty */
    } finally {
      setApiProgress(false);
    }
  }, [id]);

  return { onClick, apiProgress };
}
