import { ref, computed, reactive } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const initState = {
    token: '', // 접근 토큰(JWT)
    user: {
        email: '', // email
        roles: '', // 권한
    },
};

export const userAuthStore = defineStore('auth', () => {
    const state = ref({ ...initState });

    // 회원가입 정보 관리
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
        kakao: false,
    };

    const isLogin = computed(() => !!state.value.user.email); // 로그인 여부
    const email = computed(() => state.value.user.email); // 로그인 사용자 이메일
    const userInfo = reactive({ ...initialUserInfo });
    const isKakao = ref(false); // 로컬/카카오 구분

    // 로그인
    const login = async (member) => {
        try {
            // 카카오 로그인의 경우 토큰이 포함되어 있음
            if (member.token) {
                state.value.token = member.token;
                state.value.user = {
                    email: member.email,
                    roles: 'USER',
                };
            } else {
                // 로컬 로그인
                const response = await axios.post(
                    'http://localhost:8080/api/auth/login',
                    member
                );

                if (response.data && response.data.success) {
                    state.value.token = response.data.token;
                    state.value.user = {
                        email: response.data.user.email,
                        roles: 'USER',
                    };
                } else {
                    throw new Error(
                        response.data.message || '로그인에 실패했습니다.'
                    );
                }
            }

            // 상태 저장
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

    // 토큰 반환
    const getToken = () => state.value.token;

    // 새로고침 시 상태 복원
    const load = () => {
        const auth = localStorage.getItem('auth');
        if (auth != null) {
            state.value = JSON.parse(auth);
            console.log('복원 : ', state.value);
        }
    };

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
        isKakao.value = false;
    };

    // 실행 시 로컬스토리지에서 복원
    load();

    return {
        // 로그인 관련
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

        // 카카오
        isKakao,
    };
});
