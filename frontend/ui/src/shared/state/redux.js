import { configureStore, createSlice } from "@reduxjs/toolkit";
import { loadAuthState, storeAuthState } from "@/shared/state/storage.js";
import { setToken } from "@/lib/http.js";

const authSlice = createSlice({
  name: "auth",
  initialState: loadAuthState(),
  reducers: {
    loginSuccess: (state, action) => {
      state.id = action.payload.user.id;
      state.username = action.payload.user.username;
      state.email = action.payload.user.email;
      state.image = action.payload.user.image;

      setToken(action.payload.token);
    },
    logoutSuccess: (state, action) => {
      state.id = 0;
      delete state.username;
      delete state.email;
      delete state.image;

      setToken();
    },
  },
});

export const { loginSuccess, logoutSuccess } = authSlice.actions;

export const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
  },
});

store.subscribe(() => {
  storeAuthState(store.getState().auth);
});
