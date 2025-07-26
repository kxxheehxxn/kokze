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
    {
      path: '/userpage',
      name: 'UserPage',
      component: () => import('@/pages/UserPage.vue'),
    },
    {
      path: '/user/asset',
      name: 'UserAssetEditPage',
      component: () => import('@/pages/UserAssetEditPage.vue'),
    },
    {
      path: '/user/mbti',
      name: 'UserMbtiEditPage',
      component: () => import('@/pages/UserMbtiEditPage.vue'),
    },
    {
      path: '/user/password',
      name: 'UserPasswordEditPage',
      component: () => import('@/pages/UserPasswordEditPage.vue'),
    },
    {
      path: '/user/withdraw',
      name: 'UserWithdrawPage',
      component: () => import('@/pages/UserWithdrawPage.vue'),
    },
    {
      path: '/goals',
      name: 'GoalPage',
      component: () => import('@/pages/GoalPage.vue'),
    },
    {
      path: '/goals/:goalId', // 상세 페이지 경로
      name: 'GoalDetailPage',
      component: () => import('@/pages/GoalDetailPage.vue'),
      props: true, // goalId를 props로 전달
    },
    {
      path: '/goals/create',
      name: 'GoalCreatePage',
      component: () => import('@/pages/GoalCreatePage.vue'),
    },
    {
      path: '/goal/edit/:goalId',
      name: 'GoalEditPage',
      component: () => import('@/pages/GoalEditPage.vue'),
      props: true, // goalId param을 props로 전달
    },

    {
      path: '/find-password',
      name: 'FindPasswordPage',
      component: () => import('@/pages/FindPasswordPage.vue'),
    },
    ...authRoutes,
    ...inquiryRoutes,
  ],
});

export default router;
