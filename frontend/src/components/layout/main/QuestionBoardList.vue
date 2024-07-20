<template>
  <div>
    question_board
    <router-link to="/main/question_board/detail">detail</router-link>
  </div>
</template>
<script setup>
  import { useIndexStore } from '../../../store/IndexStore';

  const store = useIndexStore();
</script>
<style scoped>
  
</style>