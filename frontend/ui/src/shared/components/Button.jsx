import { Spinner } from "./Spinner";

export function Button({
  center = true,
  disabled = false,
  type = "button",
  apiProgress = false,
  text,
  onClick,
  styleType = "primary",
}) {
  return (
    <div className={`d-inline m-1 ${center ? "text-center" : ""}`}>
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
