import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const initState = {
  token: '', // 접근 토큰(JWT)
  user: {
    userId: '', // 사용자 ID
    email: '', // email
    role: '', // 권한 목록
  },
}
export const userAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState })

  // 로그인 여부 파악
  const isLogin = computed(() => state.value?.user?.email ? true : false) // 로그인 여부
  const userId = computed(() => state.value?.user?.userId || '') // 로그인 사용자 userId
  const email = computed(() => state.value?.user?.email || '') // 로그인 사용자 email
  const role = computed(() => state.value?.user?.role || '') // 로그인 사용자 role

  // 로그인
  const login = async (userData) => {
    try {
      state.value = {
        token: userData.token,
        user: {
          userId: userData.user?.userId || userData.userId,
          email: userData.email || userData.user?.email,
          role: userData.user?.role || 'USER'
        }
      }
      localStorage.setItem('auth', JSON.stringify(state.value))
    } catch (error) {
      console.error('로그인 실패:', error)
    }
  }

  const logout = () => {
    state.value = { ...initState }
    localStorage.removeItem('auth')
  }

  const restore = () => {
    const saved = localStorage.getItem('auth')
    if (saved) {
      state.value = JSON.parse(saved)
    }
  }

  const getToken = () => state.value.token

  // 새로고침 후 상태 복원
  const load = () => {
    const auth = localStorage.getItem('auth')
    if (auth != null) {
      state.value = JSON.parse(auth)
    }
  }

  load()

  return {
    state,
    userId,
    email,
    isLogin,
    role,
    login,
    logout,
    getToken,
  }
})
