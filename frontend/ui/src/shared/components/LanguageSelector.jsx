import { useTranslation } from "react-i18next";

export function LanguageSelector() {
  const { i18n } = useTranslation();

  const onSelectLanguage = (language) => {
    i18n.changeLanguage(language);
    localStorage.setItem("lang", language);
  };

  return (
    <div className="">
      <img
        className="m-2"
        role="button"
        src="https://flagcdn.com/16x12/tr.png"
        width="24"
        height="18"
        alt="Türkçe"
        onClick={() => onSelectLanguage("tr")}
      ></img>
      <img
        className=""
        role="button"
        src="https://flagcdn.com/16x12/us.png"
        width="24"
        height="18"
        alt="English"
        onClick={() => onSelectLanguage("en")}
      ></img>
    </div>
  );
}
