import { userAuthStore } from '@/stores/auth'; // 로그인했는지 확인하는 용도

const isAuthenticated = (to, from, next) => {
  const authStore = userAuthStore();

  if (authStore.isLogin) {
    next(); // 로그인되어 있으면 다음으로 진행
  } else {
    alert('로그인이 필요합니다.'); // 사용자에게 알림
    next('/auth/login'); // 로그인 페이지로 리다이렉트 (또는 원하는 경로로)
  }
};
export default [
  {
    path: '/notice/list',
    name: 'noticeList',
    component: () => import('../pages/notice/NoticeListPage.vue'),
  },
  {
    path: '/notice/detail/:no',
    name: 'noticeDetail',
    component: () => import('../pages/notice/NoticeDetailPage.vue'),
  },
  {
    path: '/notice/create',
    name: 'noticeCreate',
    component: () => import('../pages/notice/NoticeCreatePage.vue'),
    beforeEnter: isAuthenticated, // 가드 적용
  },
  {
    path: '/notice/update/:no',
    name: 'noticeUpdate',
    component: () => import('../pages/notice/NoticeUpdatePage.vue'),
    beforeEnter: isAuthenticated, // 가드 적용
  },
];
