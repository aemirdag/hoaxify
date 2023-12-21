import { useParams } from "react-router-dom";
import { getUser } from "./api";
import { useEffect, useState } from "react";
import { Alert } from "@/shared/components/Alert";
import { Spinner } from "@/shared/components/Spinner";
import { useRouteParamApiRequest } from "@/shared/hooks/useRouteParamApiRequest";
import { ProfileCard } from "./components/ProfileCard/ProfileCard.jsx";

export function User() {
  const {
    apiProgress,
    data: user,
    error,
  } = useRouteParamApiRequest("id", getUser);

  return (
    <>
      {apiProgress && (
        <Alert styleType="secondary" center="true">
          <Spinner />
        </Alert>
      )}
      {user && <ProfileCard user={user} />}
      <div>{error && <Alert styleType="danger">{error}</Alert>}</div>
    </>
  );
}
