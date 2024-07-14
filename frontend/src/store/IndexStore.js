import { defineStore } from "pinia";

export const useIndexStore = defineStore('index', {
  state: () => ({
    now_page: 'Home',
    body_class: '',
  }),
  actions: {
    changePage(payload) {
      const page_names = {
        '/main': 'Home',
        '/main/notification': 'Notification',
        '/main/todo': 'Todo',
        '/main/post': 'Post',
        '/main/group': 'Study Group',
        '/main/chat': 'Chatting',
        '/main/alarm': 'Alarm'
      };
      console.log(payload, 'payload');
      this.now_page = page_names[payload];
      console.log(this.now_page)
    },

    setBodyClass(newClass) {
      this.bodyClass = newClass;
      document.body.className = newClass;
    },
  }
})