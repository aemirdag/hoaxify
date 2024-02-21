import http from "@/lib/http.js";

export function deleteUser(id) {
  return http.delete(`/api/v1/users/${id}`);
}
