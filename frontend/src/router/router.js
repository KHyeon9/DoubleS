import { createRouter, createWebHistory } from "vue-router";
import MainComp from "../components/MainComp.vue";
import Login from "../components/Login.vue";
import Todo from "../components/layout/Todo.vue"
import Post from "../components/layout/Post.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { 
      path: '/', 
      component: MainComp,
      children: [
        {
          path: '/todo', 
          component: Todo,
        },
        {
          path: '/post', 
          component: Post,
        },
      ]
    },
    { path: '/login', component: Login },
  ]
})

export default router;