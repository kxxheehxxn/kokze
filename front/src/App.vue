<script setup>
import { RouterView } from 'vue-router';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import DefaultLayout from './components/layouts/DefaultLayout.vue';

const route = useRoute();

// 헤더/풋터가 없어야 하는 페이지 목록
const pagesWithoutHeaderFooter = ['/auth/login'];

const showHeaderFooter = computed(() => {
  return !pagesWithoutHeaderFooter.includes(route.path);
});
</script>

<template>
  <DefaultLayout v-if="showHeaderFooter">
    <RouterView />
  </DefaultLayout>
  <div v-else class="no-layout-wrapper">
    <RouterView />
  </div>
</template>

<style scoped>
.no-layout-wrapper {
  min-height: 100vh;
  background-color: #f8f9fa;
  width: 100%;
  overflow-x: hidden;
}
</style>
