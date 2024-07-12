<template>
  <aside class="sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3   bg-gradient-dark" id="sidenav-main">
    <div class="sidenav-header">
      <i class="fas fa-times p-3 cursor-pointer text-white opacity-5 position-absolute end-0 top-0 d-none d-xl-none" aria-hidden="true" id="iconSidenav"></i>
      <a class="navbar-brand m-0" href=" https://demos.creative-tim.com/material-dashboard/pages/dashboard " target="_blank">
        <div class="text-white text-center me-5 d-flex align-items-center justify-content-center">
          <i id="side-logo" class="material-icons opacity-10">import_contacts</i> 
          <span id="side-title" class="ms-3 font-weight-bold text-white">DoubleS</span>
        </div>
      </a>
    </div>
    <hr class="horizontal light mt-0 mb-2">
    <div class="collapse navbar-collapse  w-auto " id="sidenav-collapse-main">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/') }]"
            @click="store.changePage('/')"
            to="/"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">home</i>
            </div>
            <span class="nav-link-text ms-1">Home</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/todo') }]" 
            @click="store.changePage('/todo')"
            to="/todo"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">fact_check</i>
            </div>
            <span class="nav-link-text ms-1">Todo</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/post') }]" 
            @click="store.changePage('/post')"
            to="/post"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">table_view</i>
            </div>
            <span class="nav-link-text ms-1">Post</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/group') }]" 
            @click="store.changePage('/group')"
            to="/group"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">group</i>
            </div>
            <span class="nav-link-text ms-1">Study Group</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/chat') }]" 
            @click="store.changePage('/chat')"
            to="/chat"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">chat</i>
            </div>
            <span class="nav-link-text ms-1">Chatting</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            :class="['nav-link', 'text-white', { 'active bg-gradient-primary': isActive('/alarm') }]" 
            @click="store.changePage('/alarm')"
            to="/alarm"
          >
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">notifications</i>
            </div>
            <span class="nav-link-text ms-1">Alarm</span>
          </router-link>
        </li>
        <li class="nav-item mt-3">
          <h6 class="ps-4 ms-2 text-uppercase text-xs text-white font-weight-bolder opacity-8">Account pages</h6>
        </li>
        <li class="nav-item">
          <a class="nav-link text-white " href="../pages/profile.html">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">person</i>
            </div>
            <span class="nav-link-text ms-1">Profile</span>
          </a>
        </li>
        <li class="nav-item">
          <router-link class="nav-link text-white " to="/login">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">login</i>
            </div>
            <span class="nav-link-text ms-1">Sign In</span>
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link text-white " href="../pages/sign-up.html">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">assignment</i>
            </div>
            <span class="nav-link-text ms-1">Sign Up</span>
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
  import { useRoute } from 'vue-router';
  import { useIndexStore } from '../../store/IndexStore';
  const route = useRoute();
  const store = useIndexStore();

  const isActive = (path) => {
    return route.path === path;
  };

</script>
<style scoped>
  #side-title {
    font-size: 23px;
  }

  #side-logo {
    font-size: 28px;
  }
</style>