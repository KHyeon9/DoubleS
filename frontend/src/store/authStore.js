import { defineStore } from "pinia";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
  }),
  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    clearToken() {
      this.token = '';
      localStorage.removeItem('token');
    }
  },
  getters: {
    isAuthenticated: (state) => !!state.token,
  }
});