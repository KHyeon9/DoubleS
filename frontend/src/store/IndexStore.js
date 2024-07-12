import { defineStore } from "pinia";

export const useIndexStore = defineStore('index', {
  state: () => ({
    now_page: 'Home',
  }),
  actions: {
    changePage(payload) {
      const page_names = {
        '/': 'Home',
        '/todo': 'Todo',
        '/post': 'Post',
        '/group': 'Study Group',
        '/chat': 'Chatting',
        '/alarm': 'Alarm'
      };
      console.log(payload, 'payload');
      this.now_page = page_names[payload];
      console.log(this.now_page)
    }
  }
})