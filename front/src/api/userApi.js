import axios from 'axios'
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(config => {
  const auth = localStorage.getItem('auth')

  if (auth) {
    try {
      const { token } = JSON.parse(auth)

      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    } catch (error) {
    }
  }
  return config
})

api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('auth')
      window.location.href = '/auth/login'
    }
    return Promise.reject(error)
  },
)

export async function fetchUserInfo() {
  try {
    const { data } = await api.get('/auth/profile')
    return data.user
  } catch (error) {
    console.error('Failed to fetch user info:', error)
    throw error
  }
}

export async function fetchUserPoint(user_id) {
  if (!user_id) {
    throw new Error('user_id is required')
  }
  try {
    const { data } = await api.get(`/point/${user_id}`)
    return data.point_amount
  } catch (error) {
    console.error('Failed to fetch user points:', error)
    throw error
  }
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
    if (data.success) {
      return data.totalPoints
    } else {
      console.error('포인트 조회 실패:', data.message)
      return 0
    }
  } catch (error) {
    console.error('Failed to get user points:', error)
    return 0
  }
}

export async function updateUserProfile(profileData) {
  try {
    const { data } = await api.put('/auth/asset', profileData)
    return data
  } catch (error) {
    console.error('Failed to update user profile:', error)
    throw error
  }
}

export async function updateMbti(mbti) {
  try {
    const { data } = await api.put('/auth/mbti', { mbti })
    return data
  } catch (error) {
    console.error('Failed to update MBTI:', error)
    throw error
  }
}

export async function updatePassword(currentPassword, newPassword) {
  try {
    const { data } = await api.put('/auth/password', {
      currentPassword: currentPassword,
      newPassword: newPassword,
    })
    return data
  } catch (error) {
    console.error('Failed to update password:', error)
    throw error
  }
}

export async function withdrawUser() {
  try {
    const { data } = await api.delete('/auth/withdraw')
    return data
  } catch (error) {
    console.error('Failed to withdraw user:', error)
    throw error
  }
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
  const { data } = await api.post('/points/withdraw', {
    amount,
    reason,
  })
  return data
}

export async function getBankList() {
  const { data } = await api.get('/banks')
  return data
}
export async function createTestUser() {
  try {
    const { data } = await api.post('/auth/test-user')
    if (data.success) {
      localStorage.setItem('auth', JSON.stringify({ token: data.token }))
    }
    return data
  } catch (error) {
    console.error('Failed to create test user:', error)
    throw error
  }
}

export async function signupUser(userInfo) {
  try {
    const response = await axios.post('/api/user/signup', userInfo)
    return response.data
  } catch (error) {
    console.error('회원가입 실패:', error)
    throw error
  }
}
