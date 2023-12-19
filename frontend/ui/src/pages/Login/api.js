import http from "@/lib/http";

export function loginUser(body) {
  return http.post("/api/v1/auth", body);
}
