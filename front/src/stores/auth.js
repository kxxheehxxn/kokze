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
        try {
            // 카카오 로그인의 경우 토큰이 포함되어 있음
            if (member.token) {
                state.value.token = member.token;
                state.value.user = {
                    email: member.email,
                    roles: ['USER'],
                };
            } else {
                // 일반 로그인의 경우 UserController 사용
                const response = await axios.post(
                    'http://localhost:8080/api/auth/login',
                    member
                );

                if (response.data && response.data.success) {
                    state.value.token = response.data.token;
                    state.value.user = {
                        email: response.data.user.email,
                        roles: ['USER'],
                    };
                } else {
                    throw new Error(
                        response.data.message || '로그인에 실패했습니다.'
                    );
                }
            }

            localStorage.setItem('auth', JSON.stringify(state.value));
        } catch (error) {
            console.error('로그인 실패:', error);
            throw error;
        }
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

    // 회원가입용 상태
    const initialUserInfo = {
        name: '',
        email: '',
        password: '',
        phoneNum: '',
        birthDate: '',
        sex: '',
        salary: 0,
        payAmount: 0,
        mbti: '',
    };

    const userInfo = reactive({ ...initialUserInfo });

    // 회원가입 - 단일 항목
    const setUserInfo = (key, value) => {
        if (key in userInfo) {
            userInfo[key] = value;
        }
    };

    // 회원가입 - 전체 업데이트
    const setAllUserInfo = (newData) => {
        Object.assign(userInfo, newData);
    };

    // 회원가입 후 초기화
    const resetUserInfo = () => {
        Object.assign(userInfo, initialUserInfo);
    };

    load();

    return {
        state,
        email,
        isLogin,
        login,
        logout,
        getToken,

        // 회원가입 관련
        userInfo,
        setUserInfo,
        setAllUserInfo,
        resetUserInfo,
    };
});
