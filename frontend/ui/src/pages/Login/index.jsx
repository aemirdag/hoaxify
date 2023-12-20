import { Spinner } from "@/shared/components/Spinner";
import { Input } from "../../shared/components/Input";
import { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { loginUser } from "./api";
import { Alert } from "@/shared/components/Alert";
import { Button } from "@/shared/components/Button";

export function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [apiProgress, setApiProgress] = useState(false);
  const [errors, setErrors] = useState({});
  const [generalErrorMessage, setGeneralError] = useState("");
  const { t } = useTranslation();

  useEffect(() => {
    setErrors((lastErrors) => {
      return {
        ...lastErrors,
        email: undefined,
      };
    });
  }, [email]);

  useEffect(() => {
    setErrors((lastErrors) => {
      return {
        ...lastErrors,
        password: undefined,
      };
    });
  }, [password]);

  const onSubmit = async (event) => {
    event.preventDefault();
    setApiProgress(true);
    setGeneralError("");
    try {
      const response = await loginUser({
        email,
        password,
      });
      setSuccessMessage(response.data.message);
    } catch (error) {
      console.log(error.response);
      if (error.response?.data && error.response.status === 400) {
        setErrors(error.response.data.validationErrors);
      } else if (error.response?.data && error.response.status !== 400) {
        setGeneralError(error.response.data.message);
      } else {
        setGeneralError(t("genericError"));
      }
    } finally {
      setApiProgress(false);
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center vh-100">
      <div className="col-lg-6 col-sm-8">
        <form className="card" onSubmit={onSubmit}>
          <div className="text-center card-header">
            <h1>{t("login")}</h1>
          </div>
          <div className="card-body">
            <Input
              id="email"
              label={t("email")}
              type="text"
              error={errors.email}
              onChange={(event) => setEmail(event.target.value)}
            />
            <Input
              id="password"
              label={t("password")}
              type="password"
              error={errors.password}
              onChange={(event) => setPassword(event.target.value)}
            />
            <div>
              {generalErrorMessage && (
                <Alert styleType="danger">{generalErrorMessage}</Alert>
              )}
            </div>
            <Button
              center
              disabled={apiProgress || !password || !email}
              type={"submit"}
              apiProgress={apiProgress}
              text={t("login")}
            />
          </div>
        </form>
      </div>
    </div>
  );
}
