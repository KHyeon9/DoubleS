<template>
  <aside class="sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3   bg-gradient-dark" id="sidenav-main">
    <div class="sidenav-header">
      <i class="fas fa-times p-3 cursor-pointer text-white opacity-5 position-absolute end-0 top-0 d-none d-xl-none" aria-hidden="true" id="iconSidenav"></i>
      <router-link 
        class="navbar-brand m-0"  
        @click="indexStore.changePage('/main')"
        to="/main"
      >
        <div class="text-white text-center me-5 d-flex align-items-center justify-content-center">
          <i id="side-logo" class="material-icons opacity-10">import_contacts</i> 
          <span id="side-title" class="ms-3 font-weight-bold text-white">DoubleS</span>
        </div>
      </router-link>
    </div>
    <hr class="horizontal light mt-0 mb-2">
    <div class="collapse navbar-collapse  w-auto " id="sidenav-collapse-main">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main') }]"
            @click="indexStore.changePage('/main')"
            to="/main"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">home</i>
            </div>
            <span class="nav-link-text ms-1">홈</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/notice') }]"
            @click="indexStore.changePage('/main/notice')"
            to="/main/notice"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">priority_high</i>
            </div>
            <span class="nav-link-text ms-1">공지사항</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/todo') }]" 
            @click="indexStore.changePage('/main/todo')"
            to="/main/todo"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">fact_check</i>
            </div>
            <span class="nav-link-text ms-1">Todo</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/question_board') }]" 
            @click="indexStore.changePage('/main/question_board')"
            to="/main/question_board"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">table_view</i>
            </div>
            <span class="nav-link-text ms-1">질문 게시판</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/study_group') }]" 
            @click="indexStore.changePage('/main/study_group')"
            to="/main/study_group"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">group</i>
            </div>
            <span class="nav-link-text ms-1">스터디 그룹</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/chat') }]" 
            @click="indexStore.changePage('/main/chat')"
            to="/main/chat"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">chat</i>
            </div>
            <span class="nav-link-text ms-1">채팅</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/alarm') }]" 
            @click="indexStore.changePage('/main/alarm')"
            to="/main/alarm"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">notifications</i>
            </div>
            <span class="nav-link-text ms-1">알람</span>
          </router-link>
        </li>
        <li class="nav-item mt-3">
          <h6 class="ps-4 ms-2 text-uppercase text-xs text-white font-weight-bolder opacity-8">Account pages</h6>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/profile') }]"
            :to="`/main/profile/${userId}`"
            @click="indexStore.changePage('/main/profile')"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">person</i>
            </div>
            <span class="nav-link-text ms-1">내 정보</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/main/my_question_board') }]" 
            @click="indexStore.changePage('/main/my_question_board')"
            to="/main/my_question_board"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">table_view</i>
            </div>
            <span class="nav-link-text ms-1">내 질문 게시글</span>
          </router-link>
        </li>
        <li class="nav-item">
          <a @click="logout" class="nav-link text-white">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">assignment</i>
            </div>
            <span class="nav-link-text ms-1">Logout</span>
          </a>
        </li>
      </ul>
    </div>
    <div class="sidenav-footer position-absolute w-100 bottom-0 ">
      <div class="mx-3">
        <a 
          class="btn bg-gradient-primary w-100" 
          href="https://github.com/KHyeon9/DoubleS" 
          type="button"
        >Go To Github</a>
      </div>
    </div>
  </aside>
</template>
<script setup>
  import { useRoute, useRouter } from 'vue-router';
  import { useIndexStore } from '../../../../store/IndexStore.js';
  import { useAuthStore } from '../../../../store/authStore.js';
  import { computed } from 'vue';
  const route = useRoute();
  const router = useRouter();
  const indexStore = useIndexStore();
  const authStore = useAuthStore();
  const userId = computed(() => authStore.userId);

  const isActive = (path) => {
    const nowPath = path.split('/');

    if (nowPath.length == 2) {
      return route.path === path;
    }
    
    return  route.path.startsWith(path);
  };

  const logout = () => {
    authStore.clearToken();
    authStore.clearUserInfo();

    router.push('/login');
  }

</script>
<style scoped>
  #side-title {
    font-size: 23px;
  }

  #side-logo {
    font-size: 28px;
  }
</style>