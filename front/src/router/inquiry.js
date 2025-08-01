import { userAuthStore } from '@/stores/auth'; // 로그인했는지 확인하는 용도

const isAuthenticated = (to, from, next) => {
  const authStore = userAuthStore();

  if (authStore.isLogin) {
    next(); // 로그인되어 있으면 다음으로 진행
  } else {
    alert('로그인이 필요합니다.'); // 사용자에게 알림
    next('/auth/login'); // 로그인 페이지로 리다이렉트
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
    beforeEnter: isAuthenticated, //가드 적용
  },
  {
    path: '/inquiry/update/:no',
    name: 'inquiryUpdate',
    component: () => import('../pages/inquiry/InquiryUpdatePage.vue'),
    beforeEnter: isAuthenticated, // 가드 적용
  },
];
