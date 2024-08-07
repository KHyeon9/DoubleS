import { defineStore } from "pinia";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: '',
    nickname: '',
    roleType: '',
  }),
  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    clearToken() {
      this.token = '';
      localStorage.removeItem('token');
    },
    setUserInfo(userInfo) {
      this.userId = userInfo.userId;
      this.nickname = userInfo.nickname;
      this.roleType = userInfo.roleType;
    },
    clearUserInfo() {
      this.userId = '';
      this.nickname = '';
      this.roleType = '';
    } 
  },
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.roleType === 'ADMIN',
  }
});