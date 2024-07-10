<template>
  <div id="wrapper">
    <Sidebar />
    <div id="content-wrapper" class="d-flex flex-column">
      <div id="content">
        <Topbar />
        <BlankPage />
      </div>
      <Fotter />
    </div>
  </div>
  <LogoutModal />
</template>

<script setup>
  import Sidebar from './layout/Sidebar.vue' ;
  import LogoutModal from './layout/LogoutModal.vue';
  import Topbar from './layout/Topbar.vue';
  import Fotter from './layout/Fotter.vue';
  import BlankPage from './layout/BlankPage.vue';
</script>

<style scoped>

</style>
