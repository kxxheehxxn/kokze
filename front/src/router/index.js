import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../pages/HomePage.vue';
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
            component: () => import('@/pages/TermsPage.vue')
        },
        {
            path: '/userpage',
            name: 'UserPage',
            component: () => import('@/pages/UserPage.vue')
        },
        {
            path: '/user/asset',
            name: 'UserAssetEditPage',
            component: () => import('@/pages/UserAssetEditPage.vue')
        },
        {
            path: '/user/mbti',
            name: 'UserMbtiEditPage',
            component: () => import('@/pages/UserMbtiEditPage.vue')
        },
        {
            path: '/user/password',
            name: 'UserPasswordEditPage',
            component: () => import('@/pages/UserPasswordEditPage.vue')
        },
        {
            path: '/user/withdraw',
            name: 'UserWithdrawPage',
            component: () => import('@/pages/UserWithdrawPage.vue')
        },
        ...authRoutes,
    ],
});

export default router;
