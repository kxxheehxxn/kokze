import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
import inquiryRoutes from './inquiry';
import noticeRoutes from './notice';
import authRoutes from './auth';
import { userAuthStore } from '@/stores/auth';

const isAuthenticated = (to, from, next) => {
  const authStore = userAuthStore();

  if (authStore.isLogin) {
    next();
  } else {
    alert('로그인이 필요합니다.');
    next('/auth/login');
  }
};

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
      path: '/user',
      name: 'UserPage',
      component: () => import('@/pages/mypage/UserPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/user/point',
      name: 'PointPage',
      component: () => import('@/pages/mypage/PointPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/user/asset',
      name: 'UserAssetEditPage',
      component: () => import('@/pages/mypage/UserAssetEditPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/user/mbti',
      name: 'UserMbtiEditPage',
      component: () => import('@/pages/mypage/UserMbtiEditPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/user/password',
      name: 'UserPasswordEditPage',
      component: () => import('@/pages/mypage/UserPasswordEditPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/user/withdraw',
      name: 'UserWithdrawPage',
      component: () => import('@/pages/mypage/UserWithdrawPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/goals',
      name: 'GoalPage',
      component: () => import('@/pages/goal/GoalPage.vue'),
      beforeEnter: isAuthenticated,
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
      component: () => import('@/pages/auth/FindPasswordPage.vue'),
    },
    {
      path: '/tax-management',
      name: 'TaxPage',
      component: () => import('@/pages/tax/TaxPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/product',
      name: 'ProductRecommendPage',
      component: () => import('@/pages/product/ProductRecommendPage.vue'),
      beforeEnter: isAuthenticated,
    },
    {
      path: '/product/:fin_prdt_cd',
      name: 'ProductDetailPage',
      component: () => import('@/pages/product/ProductDetailPage.vue'),
      props: true,
    },
    ...authRoutes,
    ...inquiryRoutes,
    ...noticeRoutes,
  ],
});

export default router;
