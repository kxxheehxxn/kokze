export default [
    {
        path: '/auth/login',
        name: 'login',
        component: () => import('../pages/auth/LoginPage.vue'),
    },
    {
        path: '/auth/kakao/callback',
        name: 'kakaoCallback',
        component: () => import('../pages/auth/KakaoCallbackPage.vue'),
    },
];
