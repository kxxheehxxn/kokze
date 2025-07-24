import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
import inquiryRoutes from './inquiry';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    ...inquiryRoutes,
  ],
});

export default router;
