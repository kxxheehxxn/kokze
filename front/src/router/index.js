import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
import TermsPage from '@/pages/TermsPage.vue';
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
      component: () => TermsPage,
    },
    ...authRoutes,
  ],
  scrollBehavior() {
    return { left: 0, top: 0 }; // 모든 라우트 변경시 페이지의 최상단으로 스크롤
  },
});

export default router;
