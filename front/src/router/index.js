import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
import inquiryRoutes from './inquiry';
import noticeRoutes from './notice';
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
      component: () => import('@/pages/term/TermsPage.vue'),
    },
    {
      path: '/userpage',
      name: 'UserPage',
      component: () => import('@/pages/UserPage.vue'),
    },
    {
      path: '/points',
      name: 'PointPage',
      component: () => import('@/pages/PointPage.vue'),
    },
    {
      path: '/points',
      name: 'PointPage',
      component: () => import('@/pages/PointPage.vue'),
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
      component: () => import('@/pages/goal/GoalPage.vue'),
    },
    {
      path: '/goals/:goalId', // 상세 페이지 경로
      name: 'GoalDetailPage',
      component: () => import('@/pages/goal/GoalDetailPage.vue'),
      props: true, // goalId를 props로 전달
    },
    {
      path: '/goals/create',
      name: 'GoalCreatePage',
      component: () => import('@/pages/goal/GoalCreatePage.vue'),
    },
    {
      path: '/goals/edit/:goalId',
      name: 'GoalEditPage',
      component: () => import('@/pages/goal/GoalEditPage.vue'),
      props: true, // goalId param을 props로 전달
    },

    {
      path: '/find-password',
      name: 'FindPasswordPage',
      component: () => import('@/pages/FindPasswordPage.vue'),
    },
    {
      path: '/tax-management',
      name: 'TaxPage.vue',
      component: () => import('@/pages/tax/TaxPage.vue'),
    },
    {
      path: '/product',
      name: 'ProductRecommendPage',
      component: () => import('@/pages/product/ProductRecommendPage.vue'),
    },
    {
      path: '/product/:fin_prdt_cd',
      name: 'ProductDetailPage',
      component: () => import('@/pages/product/ProductDetailPage.vue'),
      props: true, //
    },
    ...authRoutes,
    ...inquiryRoutes,
    ...noticeRoutes,
  ],
});

export default router;
