<script setup>
import { RouterView } from 'vue-router';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import DefaultLayout from './components/layouts/DefaultLayout.vue';
const route = useRoute();
const pagesWithoutHeaderFooter = [
    '/auth/login',
    '/signup/step1',
    '/signup/step1/local',
    '/signup/step1/kakao',
    '/signup/step2',
    '/signup/step3',
];
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
