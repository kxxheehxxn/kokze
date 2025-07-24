import { ref, computed, reactive } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const initState = {
    token: '', // 접근 토큰(JWT)
    user: {
        email: '', // email
        roles: [], // 권한 목록
    },
};

export const userAuthStore = defineStore('auth', () => {
    const state = ref({ ...initState });

    // 로그인 여부 파악
    const isLogin = computed(() => !!state.value.user.email); // 로그인 여부
    const email = computed(() => state.value.user.email); // 로그인 사용자 email

    // 로그인
    const login = async (member) => {
        // 테스트용: 이메일만 사용
        state.value.token = 'test-token';
        state.value.user = {
            email: member.email,
            roles: ['USER'],
        };

        // 실제 로그인 API 예시 (나중에 주석 해제하고 사용)
        // const { data } = await axios.post('/api/auth/login',user);
        // state.value = { ...data };

        localStorage.setItem('auth', JSON.stringify(state.value));
    };

    // 로그아웃
    const logout = () => {
        localStorage.removeItem('auth');
        state.value = { ...initState };
    };

    const getToken = () => state.value.token;

    // 새로고침 후 상태 복원
    const load = () => {
        const auth = localStorage.getItem('auth');
        if (auth != null) {
            state.value = JSON.parse(auth);
            console.log('복원 : ', state.value);
        }
    };

    load();

    return {
        state,
        email,
        isLogin,
        login,
        logout,
        getToken,
    };
});
