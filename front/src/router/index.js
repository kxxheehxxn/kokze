import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
import inquiryRoutes from './inquiry';
import authRoutes from './auth';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/terms',
      name: 'TermsPage',
      component: () => import('@/pages/TermsPage.vue'),
    },
    ...authRoutes,
    ...inquiryRoutes,
  ],
});

export default router;
