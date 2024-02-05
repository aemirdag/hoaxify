import { Link } from "react-router-dom";
import { ProfileImage } from "@/shared/components/ProfileImage.jsx";

export function UserListItem({ user }) {
  return (
    <Link
      className={"list-group-item list-group-item-action"}
      to={"/user/" + user.id}
      style={{ textDecoration: "none" }}
    >
      <ProfileImage width={30} image={user.image} />
      <span className="ms-2">{user.username}</span>
    </Link>
  );
}
