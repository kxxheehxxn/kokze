import { ref, computed, reactive } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const initState = {
  token: '', // 접근 토큰(JWT)
  user: {
    userId: '', // 사용자 ID
    userName: '', // 사용자 이름
    email: '', // email
    role: '', // 권한 목록
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

  // 로그인 여부 파악
  const isLogin = computed(() => !!state.value.user.email); // 로그인 여부
  const userId = computed(() => state.value.user.userId); // 로그인 사용자 ID
  const userName = computed(() => state.value.user.userName); // 로그인 사용자 이름
  const email = computed(() => state.value.user.email); // 로그인 사용자 email
  const role = computed(() => state.value.user.role); // 로그인 사용자 role
  const userInfo = reactive({ ...initialUserInfo });
  const isKakao = ref(false); // 로컬/카카오 구분

  // 로그인
  const login = async (member) => {
    try {
      // 카카오 로그인의 경우 토큰이 포함되어 있음
      if (member.token) {
        state.value.token = member.token;
        state.value.user = {
          userId: member.userId || '',
          userName: member.userName,
          email: member.email,
          role: 'USER',
        };
      } else {
        // 로컬 로그인
        const response = await axios.post('http://localhost:8080/api/auth/login', member);

        if (response.data && response.data.success) {
          state.value.token = response.data.token;
          state.value.user = {
            userId: response.data.user.userId || '',
            userName: response.data.user.name,
            email: response.data.user.email,
            role: response.data.user.role,
          };
        } else {
          throw new Error(response.data.message || '로그인에 실패했습니다.');
        }
      }

      // 상태 저장
      localStorage.setItem('auth', JSON.stringify(state.value));
    } catch (error) {
      console.error('로그인 실패:', error);
      throw error;
    }
  };

  const logout = () => {
    state.value = { ...initState };
    localStorage.removeItem('auth');
  };

  const restore = () => {
    const saved = localStorage.getItem('auth');
    if (saved) {
      state.value = JSON.parse(saved);
    }
  };

  const getToken = () => state.value.token;

  // 새로고침 후 상태 복원
  const load = () => {
    const auth = localStorage.getItem('auth');
    if (auth != null) {
      state.value = JSON.parse(auth);
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
    userId,
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
