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
        '/main/notice': 'Notice',
        '/main/todo': 'Todo',
        '/main/question_board': 'Question Board',
        '/main/question_board/detail': 'Question Board Detail',
        '/main/my_question_board': 'My Question Board',
        '/main/group': 'Study Group',
        '/main/chat': 'Chatting',
        '/main/alarm': 'Alarm',
        '/main/profile': 'Profile',
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