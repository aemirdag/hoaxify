import { useCallback, useEffect, useState } from "react";
import { getUsers } from "../api";
import { Spinner } from "@/shared/components/Spinner";
import { UserListItem } from "./UserListItem";
import { useTranslation } from "react-i18next";

export function UserList() {
  const { t } = useTranslation();

  const [userPage, setUserPage] = useState({
    content: [],
    last: false,
    first: false,
    number: 0,
  });

  const [apiProgress, setApiProgress] = useState(false);

  const fetch = useCallback(async (page) => {
    setApiProgress(true);

    try {
      const response = await getUsers(page);
      setUserPage(response.data);
    } catch (axiosError) {
    } finally {
      setApiProgress(false);
    }
  }, []);

  useEffect(() => {
    fetch(0);
  }, []);

  return (
    <div className="card">
      <div className="card-header text-center fs-4">{t("userList")}</div>
      <ul className="list-group list-group-flush">
        {userPage.content.map((user) => {
          return <UserListItem key={user.id} user={user} />;
        })}
      </ul>
      <div className="card-footer">
        {apiProgress && <Spinner />}
        {!apiProgress && !userPage.first && (
          <button
            className="btn btn-outline-secondary btn-sm float-start"
            onClick={() => fetch(userPage.number - 1)}
          >
            {t("previous")}
          </button>
        )}
        {!apiProgress && !userPage.last && (
          <button
            className="btn btn-outline-secondary btn-sm float-end"
            onClick={() => fetch(userPage.number + 1)}
          >
            {t("next")}
          </button>
        )}
      </div>
    </div>
  );
}
