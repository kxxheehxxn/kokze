//import { isAuthenticated } from '@/util/guards'; //로그인했는지 확인하는 용도
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
  },
  {
    path: '/notice/update/:no',
    name: 'noticeUpdate',
    component: () => import('../pages/notice/NoticeUpdatePage.vue'),
  },
];
