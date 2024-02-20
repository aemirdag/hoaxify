import http from "@/lib/http.js";

export function logout() {
  return http.post("/api/v1/logout");
}
