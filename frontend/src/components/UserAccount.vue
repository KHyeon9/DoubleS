<template>
  <LoginNavbar />
  <main class="main-content mt-0">
    <router-view />
  </main>
</template>
<script setup>
  import LoginNavbar from './layout/user/UserAccountNavbar.vue';
</script>
<style scoped>

</style>