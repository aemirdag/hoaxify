import { Spinner } from "./Spinner";

export function Button(props) {
  const { center, disabled, type, apiProgress, text } = props;

  return (
    <div className={`${center ? "text-center" : ""}`}>
      <button disabled={disabled} type={type} className="btn btn-primary">
        {apiProgress && <Spinner sm={true} />}
        {text}
      </button>
    </div>
  );
}
