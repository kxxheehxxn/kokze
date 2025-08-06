import { ref, computed, reactive } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const initState = {
  token: '',
  user: {
    userId: '',
    userName: '',
    email: '',
    role: '',
  },
};

export const userAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });

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

  const isLogin = computed(() => !!state.value.user.email);
  const userId = computed(() => state.value.user.userId);
  const userName = computed(() => {
    try {
      const authData = localStorage.getItem('auth');
      if (authData) {
        const parsed = JSON.parse(authData);
        const name = parsed.user?.userName;
        return name || '';
      }
    } catch (error) {
      console.error('localStorage 파싱 오류:', error);
    }

    const name = state.value.user.userName;
    return name || '';
  });
  const email = computed(() => state.value.user.email);
  const role = computed(() => state.value.user.role);
  const userInfo = reactive({ ...initialUserInfo });
  const isKakao = ref(false);

  const login = async (member) => {
    try {
      if (member.token) {
        state.value.token = member.token;
        state.value.user = {
          userId: member.userId || '',
          userName: member.userName,
          email: member.email,
          role: 'USER',
        };
      } else {
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

  const setToken = (token) => {
    state.value.token = token;
    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  const load = () => {
    const auth = localStorage.getItem('auth');
    if (auth != null) {
      state.value = JSON.parse(auth);
    }
  };

  const setUserInfo = (key, value) => {
    if (key in userInfo) {
      userInfo[key] = value;
    }
  };

  const setAllUserInfo = (newData) => {
    Object.assign(userInfo, newData);
  };

  const resetUserInfo = () => {
    Object.assign(userInfo, initialUserInfo);
    isKakao.value = false;
  };

  load();

  return {
    state,
    email,
    userId,
    isLogin,
    login,
    logout,
    getToken,
    setToken,
    userInfo,
    setUserInfo,
    setAllUserInfo,
    resetUserInfo,
    isKakao,
  };
});
