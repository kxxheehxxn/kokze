export default [
    {
        path: '/auth/login',
        name: 'login',
        component: () => import('../pages/auth/LoginPage.vue'),
    },
    {
        path: '/signup/step1/local',
        name: 'signup-step1-local',
        component: () => import('../pages/auth/SignupStep1Local.vue'),
    },
    {
        path: '/signup/step1/kakao',
        name: 'signup-step1-kakao',
        component: () => import('../pages/auth/SignupStep1Kakao.vue'),
    },
    {
        path: '/signup/step2',
        name: 'signup-step2',
        component: () => import('../pages/auth/SignupStep2.vue'),
    },
    {
        path: '/signup/step3',
        name: 'signup-step3',
        component: () => import('../pages/auth/SignupStep3.vue'),
    },
];
