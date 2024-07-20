<template>
  <div>
    Post
    <router-link to="/main/detail" @click="store.changePage('/main/detail')">detail</router-link>
  </div>
</template>
<script setup>
  import { useIndexStore } from '../../../store/IndexStore';

  const store = useIndexStore();
</script>
<style scoped>
  
</style>