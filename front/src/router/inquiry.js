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
export default [
  {
    path: '/inquiry/list',
    name: 'inquiryList',
    component: () => import('../pages/inquiry/InquiryListPage.vue'),
  },
  {
    path: '/inquiry/detail/:no',
    name: 'inquiryDetail',
    component: () => import('../pages/inquiry/InquiryDetailPage.vue'),
  },
  {
    path: '/inquiry/create',
    name: 'inquiryCreate',
    component: () => import('../pages/inquiry/InquiryCreatePage.vue'),
    beforeEnter: isAuthenticated,
  },
  {
    path: '/inquiry/update/:no',
    name: 'inquiryUpdate',
    component: () => import('../pages/inquiry/InquiryUpdatePage.vue'),
    beforeEnter: isAuthenticated,
  },
];
