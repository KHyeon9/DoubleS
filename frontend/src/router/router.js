import { createRouter, createWebHistory } from "vue-router";
import { useIndexStore } from "../store/IndexStore";
import MainComp from "../components/MainComp.vue";
import Todo from "../components/layout/main/Todo.vue"
import QuestionBoardList from "../components/layout/main/QuestionBoardList.vue";
import Home from "../components/layout/main/Home.vue";
import Chat from "../components/layout/main/Chat.vue";
import Notice from "../components/layout/main/Notice.vue";
import UserAccount from "../components/UserAccount.vue";
import Login from "../components/layout/user/Login.vue";
import Regist from "../components/layout/user/Regist.vue";
import QuestionBoardDetail from '../components/layout/main/QuestionBoardDetail.vue'
import Alarm from "../components/layout/main/Alarm.vue";
import { useAuthStore } from "../store/authStore";
import CreateQuestionBoard from "../components/layout/main/CreateQuestionBoard.vue";
import CreateNotice from "../components/layout/main/CreateNotice.vue";
import NoticeDetail from "../components/layout/main/NoticeDetail.vue";
import ModifyNotice from "../components/layout/main/ModifyNotice.vue";
import ModifyQuestionBoard from "../components/layout/main/ModifyQuestionBoard.vue";
import Profile from "../components/layout/user/Profile.vue";
import ModifiyProfile from "../components/layout/user/ModifiyProfile.vue";
import MyQuestionBoardList from "../components/layout/main/MyQuestionBoardList.vue";
import StudyGroup from "../components/layout/main/StudyGroup.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login', // '/' 경로로 접근 시 '/login'으로 리다이렉트
    },
    { 
      path: '/main', 
      component: MainComp,
      meta: { bodyClass: 'g-sidenav-show bg-gray-200' }, 
      children: [
        {
          path: '', 
          component: Home,
        },
        {
          path: 'notice',
          component: Notice
        },
        {
          path: 'notice/:id',
          component: NoticeDetail
        },
        {
          path: 'notice/modify/:id',
          component: ModifyNotice
        },
        {
          path: 'notice/new', 
          component: CreateNotice,
        },
        {
          path: 'todo', 
          component: Todo,
        },
        {
          path: 'question_board', 
          component: QuestionBoardList,
        },
        {
          path: 'my_question_board', 
          component: MyQuestionBoardList,
        },
        {
          path: 'question_board/:id', 
          component: QuestionBoardDetail,
        },
        {
          path: 'question_board/modify/:id', 
          component: ModifyQuestionBoard,
        },
        {
          path: 'question_board/new', 
          component: CreateQuestionBoard,
        },
        {
          path: 'group', 
          component: StudyGroup,
        },
        {
          path: 'chat', 
          component: Chat,
        },
        {
          path: 'alarm', 
          component: Alarm,
        },
        {
          path: 'profile/:userId', 
          component: Profile,
        },
        {
          path: 'profile/modify/:userId', 
          component: ModifiyProfile,
        },
      ]
    },
    { 
      path: '/', 
      component: UserAccount, 
      children: [
        {
          path: 'login',
          component: Login,
        },
        {
          path: 'regist',
          component: Regist,
        },
      ]
    },
    // {
    //   path: '*',
    //   name: 404,
    // }
    
  ]
})

router.beforeEach((to, from, next) => {
  const indexStore = useIndexStore();
  const authStore = useAuthStore();

  // 바디 클레스 변경 로직
  if (to.meta.bodyClass !== undefined) {
    indexStore.setBodyClass(to.meta.bodyClass);
  } else {
    indexStore.setBodyClass('');
  }

  // 인증 확인
  if (to.meta.requiresAuth && !authStore.token) {
    next('/login');
  } else {
    next();
  }
});

export default router;