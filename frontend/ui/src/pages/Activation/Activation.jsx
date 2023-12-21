import { activateUser } from "./api";
import { Alert } from "@/shared/components/Alert";
import { Spinner } from "@/shared/components/Spinner";
import { useRouteParamApiRequest } from "@/shared/hooks/useRouteParamApiRequest";

export function Activation() {
  const { apiProgress, data, error } = useRouteParamApiRequest(
    "token",
    activateUser
  );

  return (
    <>
      {apiProgress && (
        <Alert styleType="secondary" center="true">
          <Spinner />
        </Alert>
      )}
      <div>
        {data?.message && <Alert>{data.message}</Alert>}
        {error && <Alert styleType="danger">{error}</Alert>}
      </div>
    </>
  );
}
