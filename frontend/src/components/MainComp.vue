<template>
  <Sidebar />
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
    <TopNavbar />
    <div class="container-fluid py-4">
      <div id="main-content">
        <router-view />
      </div>
      <FooterComp />
    </div>
  </main>
  
</template>

<script setup>
  import Sidebar from './layout/main/fragments/Sidebar.vue';
  import TopNavbar from './layout/main/fragments/TopNavbar.vue';
  import FooterComp from './layout/main/fragments/FooterComp.vue';
</script>

<style scoped>

</style>