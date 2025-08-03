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
    beforeEnter: isAuthenticated,
  },
  {
    path: '/notice/update/:no',
    name: 'noticeUpdate',
    component: () => import('../pages/notice/NoticeUpdatePage.vue'),
    beforeEnter: isAuthenticated,
  },
];
