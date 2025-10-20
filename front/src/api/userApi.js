import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

const LOGIN_PATH = '/auth/login'

function getAuth() {
  const raw = localStorage.getItem('auth')
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}
function setToken(token) {
  const current = getAuth() || {}
  localStorage.setItem('auth', JSON.stringify({ ...current, token }))
}
function clearAuthAndRedirect() {
  localStorage.removeItem('auth')
  setTimeout(() => {
    window.location.assign(LOGIN_PATH)
  }, 0)
}

api.interceptors.request.use(config => {
  const auth = getAuth()
  if (auth?.token) {
    config.headers = config.headers || {}
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

let isRefreshing = false
let pendingQueue = []

async function processQueue(error, newToken = null) {
  pendingQueue.forEach(({ resolve, reject, original }) => {
    if (newToken) {
      original.headers = original.headers || {}
      original.headers.Authorization = `Bearer ${newToken}`
      resolve(api(original))
    } else {
      reject(error)
    }
  })
  pendingQueue = []
}

async function refreshAccessToken() {
  const auth = getAuth()
  if (!auth?.token) throw new Error('no token to refresh')
  const res = await axios.post('/api/auth/refresh-token', null, {
    headers: { Authorization: `Bearer ${auth.token}` },
    baseURL: '/api',
  })
  if (!res.data?.success || !res.data?.token) {
    throw new Error(res.data?.message || 'refresh failed')
  }
  return res.data.token
}

api.interceptors.response.use(
  response => {
    const newToken = response.headers?.['x-new-token']
    if (newToken) setToken(newToken)
    return response
  },
  async error => {
    const original = error.config

    if (!error.response || !original) {
      return Promise.reject(error)
    }
    if (original._retry) {
      return Promise.reject(error)
    }
    if (error.response.status !== 401) {
      return Promise.reject(error)
    }

    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        pendingQueue.push({ resolve, reject, original })
      })
    }

    isRefreshing = true
    original._retry = true

    try {
      const newToken = await refreshAccessToken()
      setToken(newToken)

      await processQueue(null, newToken)

      original.headers = original.headers || {}
      original.headers.Authorization = `Bearer ${newToken}`
      return api(original)
    } catch (e) {
      await processQueue(e, null)
      clearAuthAndRedirect()
      return Promise.reject(e)
    } finally {
      isRefreshing = false
    }
  },
)

export async function fetchUserInfo() {
  const { data } = await api.get('/auth/profile')
  return data.user
}
export async function fetchUserPoint(user_id) {
  if (!user_id) throw new Error('user_id is required')
  const { data } = await api.get(`/point/${user_id}`)
  return data.point_amount
}
export async function getUserInfo() {
  try {
    const { data } = await api.get('/auth/profile')
    if (data.success) {
      return {
        name: data.user.name,
        mbti: data.user.mbti,
        user_id: data.user.userId,
        email: data.user.email,
        phoneNum: data.user.phoneNum,
        birthDate: data.user.birthDate,
        sex: data.user.sex,
        salary: data.user.salary,
        payAmount: data.user.payAmount,
      }
    } else {
      throw new Error(data.message || '사용자 정보를 불러올 수 없습니다.')
    }
  } catch (error) {
    console.error('Failed to get user info:', error)
    return { name: '사용자', mbti: '미입력', user_id: null }
  }
}
export async function getUserPoints() {
  try {
    const { data } = await api.get('/auth/points')
    if (data.success) return data.totalPoints
    console.error('포인트 조회 실패:', data.message)
    return 0
  } catch (error) {
    console.error('Failed to get user points:', error)
    return 0
  }
}
export async function updateUserProfile(profileData) {
  const { data } = await api.put('/auth/asset', profileData)
  return data
}
export async function updateMbti(mbti) {
  const { data } = await api.put('/auth/mbti', { mbti })
  return data
}
export async function updatePassword(currentPassword, newPassword) {
  const { data } = await api.put('/auth/password', {
    currentPassword,
    newPassword,
  })
  return data
}
export async function withdrawUser() {
  const { data } = await api.delete('/auth/withdraw')
  return data
}
export async function getMyPoints() {
  const { data } = await api.get('/auth/points')
  return data
}
export async function getMyPointHistory() {
  const { data } = await api.get('/auth/points/history')
  return data
}
export async function withdrawPoints(amount, reason) {
  const { data } = await api.post('/points/withdraw', { amount, reason })
  return data
}
export async function getBankList() {
  const { data } = await api.get('/banks')
  return data
}
export async function createTestUser() {
  const { data } = await api.post('/auth/test-user')
  if (data.success && data.token) {
    setToken(data.token)
  }
  return data
}
export async function signupUser(userInfo) {
  const response = await axios.post('/api/user/signup', userInfo)
  return response.data
}

export default api
