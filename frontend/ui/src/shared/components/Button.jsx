import { Spinner } from "./Spinner";

export function Button({
  center = true,
  disabled = false,
  type = "button",
  apiProgress = false,
  text,
  onClick = null,
  styleType = "primary",
}) {
  return (
    <div className={`${center ? "text-center" : ""}`}>
      <button
        disabled={disabled}
        type={type}
        className={`btn btn-${styleType}`}
        onClick={onClick}
      >
        {apiProgress && <Spinner sm={true} />}
        {text}
      </button>
    </div>
  );
}
