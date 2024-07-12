import { createRouter, createWebHistory } from "vue-router";
import MainComp from "../components/MainComp.vue";
import Login from "../components/Login.vue";
import Todo from "../components/layout/Todo.vue"
import Post from "../components/layout/Post.vue";
import Home from "../components/layout/Home.vue";
import Group from "../components/layout/Group.vue";
import Chat from "../components/layout/Chat.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { 
      path: '/', 
      component: MainComp,
      children: [
        {
          path: '/', 
          component: Home,
        },
        {
          path: '/todo', 
          component: Todo,
        },
        {
          path: '/post', 
          component: Post,
        },
        {
          path: '/group', 
          component: Group,
        },
        {
          path: '/chat', 
          component: Chat,
        },
        {
          path: '/alarm', 
          component: Chat,
        },
      ]
    },
    { path: '/login', component: Login },
  ]
})

export default router;