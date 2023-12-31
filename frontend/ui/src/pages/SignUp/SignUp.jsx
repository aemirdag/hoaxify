import axios from "axios";
import { useEffect, useState, useMemo, useTransition } from "react";
import { signUp } from "./api";
import { Input } from "../../shared/components/Input";
import { useTranslation } from "react-i18next";
import { Alert } from "@/shared/components/Alert";
import { Spinner } from "@/shared/components/Spinner";
import { Button } from "@/shared/components/Button";

export function SignUp() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordRepeat, setPasswordRepeat] = useState("");
  const [apiProgress, setApiProgress] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errors, setErrors] = useState({});
  const [generalErrorMessage, setGeneralError] = useState("");
  const { t } = useTranslation();

  useEffect(() => {
    setErrors((lastErrors) => {
      return {
        ...lastErrors,
        username: undefined,
      };
    });
  }, [username]);

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

  const passwordRepeatError = useMemo(() => {
    if (password && password !== passwordRepeat) {
      return t("passwordMismatch");
    }

    return "";
  }, [password, passwordRepeat]);

  const onSubmit = async (event) => {
    event.preventDefault();
    setApiProgress(true);
    setSuccessMessage("");
    setGeneralError("");
    try {
      const response = await signUp({
        username,
        email,
        password,
      });
      setSuccessMessage(response.data.message);
    } catch (error) {
      if (error.response?.data && error.response.data.status === 400) {
        setErrors(error.response.data.validationErrors);
      } else if (error.response?.data && error.response.data.status !== 400) {
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
            <h1>{t("signUp")}</h1>
          </div>
          <div className="card-body">
            <Input
              id="username"
              label={t("username")}
              type="text"
              error={errors.username}
              onChange={(event) => setUsername(event.target.value)}
            />
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
            <Input
              id="passwordRepeat"
              label={t("passwordRepeat")}
              type="password"
              error={passwordRepeatError}
              onChange={(event) => setPasswordRepeat(event.target.value)}
            />
            <div>
              {successMessage && <Alert>{successMessage}</Alert>}
              {generalErrorMessage && (
                <Alert styleType="danger">{generalErrorMessage}</Alert>
              )}
            </div>
            <Button
              center
              disabled={apiProgress || !password || password !== passwordRepeat}
              type={"submit"}
              apiProgress={apiProgress}
              text={t("signUp")}
            />
          </div>
        </form>
      </div>
    </div>
  );
}
