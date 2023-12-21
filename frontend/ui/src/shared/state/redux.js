import { configureStore, createSlice } from "@reduxjs/toolkit";

const authSlice = createSlice({
  name: "auth",
});

export const store = configureStore({
  reducer: {},
});
