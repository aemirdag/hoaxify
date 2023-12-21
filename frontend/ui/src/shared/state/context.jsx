import { createContext, useEffect, useReducer } from "react";
import { loadAuthState, storeAuthState } from "@/shared/state/storage.js";

export const AuthContext = createContext();

const authReducer = (authState, action) => {
  switch (action.type) {
    case "login-success":
      return action.data;
    case "logout-success":
      return { id: 0 };
    default:
      throw new Error(`unknown action: ${action.type}`);
  }
};

export function AuthenticationContext({ children }) {
  const [authState, dispatch] = useReducer(authReducer, loadAuthState());

  useEffect(() => {
    storeAuthState(authState);
  }, [authState]);

  return (
    <AuthContext.Provider
      value={{
        ...authState,
        dispatch,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
