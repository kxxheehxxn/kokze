import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const initState = {
  token: '', // 접근 토큰(JWT)
  user: {
    userId: '', // 사용자 ID
    email: '', // email
    roles: [], // 권한 목록
  },
}

export const userAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState })

  // 로그인 여부 파악
  const isLogin = computed(() => !!state.value.user.email) // 로그인 여부
  const email = computed(() => state.value.user.email) // 로그인 사용자 email
  const userId = computed(() => state.value.user.userId) // 로그인 사용자 ID

  // 로그인
  const login = async member => {
    try {
      // 카카오 로그인의 경우 토큰이 포함되어 있음
      if (member.token) {
        state.value.token = member.token
        state.value.user = {
          userId: member.userId || '',
          email: member.email,
          roles: ['USER'],
        }
      } else {
        // 일반 로그인의 경우 UserController 사용
        const response = await axios.post(
          'http://localhost:8080/api/auth/login',
          member,
        )

        if (response.data && response.data.success) {
          state.value.token = response.data.token
          state.value.user = {
            userId: response.data.user.userId || '',
            email: response.data.user.email,
            roles: ['USER'],
          }
        } else {
          throw new Error(response.data.message || '로그인에 실패했습니다.')
        }
      }

      localStorage.setItem('auth', JSON.stringify(state.value))
    } catch (error) {
      console.error('로그인 실패:', error)
      throw error
    }
  }

  // 로그아웃
  const logout = () => {
    localStorage.removeItem('auth')
    state.value = { ...initState }
  }

  const getToken = () => state.value.token

  // 새로고침 후 상태 복원
  const load = () => {
    const auth = localStorage.getItem('auth')
    if (auth != null) {
      state.value = JSON.parse(auth)
      console.log('복원 : ', state.value)
    }
  }

  load()

  return {
    state,
    email,
    userId,
    isLogin,
    login,
    logout,
    getToken,
  }
})
