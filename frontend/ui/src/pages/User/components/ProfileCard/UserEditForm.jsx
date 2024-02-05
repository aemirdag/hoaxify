import { Input } from "@/shared/components/Input.jsx";
import { Alert } from "@/shared/components/Alert.jsx";
import { Button } from "@/shared/components/Button.jsx";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { updateUser } from "@/pages/User/components/ProfileCard/api.js";
import { userUpdateSuccess } from "@/shared/state/redux.js";
import { useDispatch, useSelector } from "react-redux";

export function UserEditForm({ setIsEditMode, setTempImage }) {
  const authState = useSelector((store) => store.auth);
  const dispatch = useDispatch();
  const { t } = useTranslation();
  const [newUsername, setNewUsername] = useState(authState.username);
  const [apiProgress, setApiProgress] = useState(false);
  const [errors, setErrors] = useState({});
  const [generalErrorMessage, setGeneralError] = useState("");
  const [newImage, setNewImage] = useState("");

  const onChangeUsername = (event) => {
    setNewUsername(event.target.value);
    setErrors((lastErrors) => {
      return {
        ...lastErrors,
        username: undefined,
      };
    });
    setGeneralError("");
  };

  const onClickCancel = () => {
    setIsEditMode(false);
    setNewUsername(authState.username);
    setNewImage();
    setTempImage("");
  };

  const onSelectImage = (event) => {
    setErrors((lastErrors) => {
      return {
        ...lastErrors,
        image: undefined,
      };
    });

    if (event.target.files.length < 1) {
      return;
    }

    const file = event.target.files[0];
    const fileReader = new FileReader();

    fileReader.onloadend = () => {
      const data = fileReader.result;
      setNewImage(data);
      setTempImage(data);
    };

    fileReader.readAsDataURL(file);
  };

  const onSubmit = async (event) => {
    event.preventDefault();
    setApiProgress(true);
    setErrors({});
    setGeneralError("");

    try {
      const response = await updateUser(authState.id, {
        username: newUsername,
        image: newImage,
      });

      setIsEditMode(false);
      dispatch(userUpdateSuccess(response.data));
    } catch (error) {
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
    <form onSubmit={onSubmit}>
      <Input
        label={t("username")}
        defaultValue={authState.username}
        onChange={onChangeUsername}
        error={errors.username}
      />
      <Input
        label={t("profileImage")}
        type="file"
        onChange={onSelectImage}
        error={errors.image}
      />
      {generalErrorMessage && (
        <Alert styleType="danger">{generalErrorMessage}</Alert>
      )}
      <Button
        center
        text={t("save")}
        className={"m-1"}
        apiProgress={apiProgress}
        type={"submit"}
      />
      <Button
        center
        text={t("cancel")}
        styleType={"outline-secondary"}
        onClick={onClickCancel}
      />
    </form>
  );
}
