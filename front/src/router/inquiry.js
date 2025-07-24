//import { isAuthenticated } from '@/util/guards'; //로그인했는지 확인하는 용도
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
  },
  {
    path: '/inquiry/update/:no',
    name: 'inquiryUpdate',
    component: () => import('../pages/inquiry/InquiryUpdatePage.vue'),
  },

  //   {
  //     path: '/inquiry/create',
  //     name: 'inquiryCreate',
  //     component: () => import('../pages/inquiry/InquiryCreatePage.vue'),
  //     beforeEnter: isAuthenticated,
  //   },
  //   {
  //     path: '/inquiry/update/:no',
  //     name: 'inquiryUpdate',
  //     component: () => import('../pages/inquiry/InquiryUpdatePage.vue'),
  //     beforeEnter: isAuthenticated,
  //   },
];
