import axios from 'axios'

// axios 인스턴스 생성 (JWT 토큰 자동 포함)
const api = axios.create({ 
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  }
})

// 요청 인터셉터: JWT 토큰 자동 추가
api.interceptors.request.use(config => {
  const auth = localStorage.getItem('auth')
  console.log('🔍 localStorage auth 데이터:', auth)
  
  if (auth) {
    try {
      const { token } = JSON.parse(auth)
      console.log('🔐 파싱된 토큰:', token ? token.substring(0, 50) + '...' : 'null')
      
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
        console.log('✅ Authorization 헤더 설정됨')
      } else {
        console.log('⚠️ 토큰이 null입니다.')
      }
    } catch (error) {
      console.log('❌ auth 데이터 파싱 실패:', error)
    }
  } else {
    console.log('⚠️ localStorage에 auth 정보가 없습니다.')
  }
  console.log('📤 API 요청:', config.method?.toUpperCase(), config.url, config.data)
  console.log('📤 요청 헤더:', config.headers)
  return config
})

// 응답 인터셉터: 토큰 만료 시 처리
api.interceptors.response.use(
  response => {
    console.log('📥 API 응답 성공:', response.status, response.data)
    return response
  },
  error => {
    console.log('❌ API 응답 실패:', error.response?.status, error.response?.data)
    if (error.response?.status === 401) {
      // 토큰 만료 시 로그인 페이지로 리다이렉트
      localStorage.removeItem('auth')
      window.location.href = '/auth/login'
    }
    return Promise.reject(error)
  }
)

export async function fetchUserInfo() {
  try {
    // 로그인된 사용자 정보 (name, mbti, user_id)
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
        payAmount: data.user.payAmount
      }
    } else {
      throw new Error(data.message || '사용자 정보를 불러올 수 없습니다.')
    }
  } catch (error) {
    console.error('Failed to get user info:', error)
    // 에러 발생 시 기본값 반환
    return { name: '사용자', mbti: '미입력', user_id: null }
  }
}

export async function getUserPoints() {
  try {
    // 임시로 mock 데이터 반환 (포인트 API 구현 후 변경)
    return 13700
  } catch (error) {
    console.error('Failed to get user points:', error)
    return 0
  }
}

// 자산정보 업데이트 API
export async function updateUserProfile(profileData) {
  try {
    const { data } = await api.put('/auth/asset', profileData)
    return data
  } catch (error) {
    console.error('Failed to update user profile:', error)
    throw error
  }
}

// MBTI 수정 API
export async function updateMbti(mbti) {
  try {
    const { data } = await api.put('/auth/mbti', { mbti })
    return data
  } catch (error) {
    console.error('Failed to update MBTI:', error)
    throw error
  }
}

// 비밀번호 수정 API
export async function updatePassword(currentPassword, newPassword) {
  try {
    const { data } = await api.put('/auth/password', { 
      currentPassword: currentPassword,
      newPassword: newPassword 
    })
    return data
  } catch (error) {
    console.error('Failed to update password:', error)
    throw error
  }
}

// 회원 탈퇴 API
export async function withdrawUser() {
  try {
    const { data } = await api.delete('/auth/withdraw')
    return data
  } catch (error) {
    console.error('Failed to withdraw user:', error)
    throw error
  }
}

// 테스트용 사용자 생성 API
export async function createTestUser() {
  try {
    const { data } = await api.post('/auth/test-user')
    if (data.success) {
      // 생성된 토큰을 localStorage에 저장
      localStorage.setItem('auth', JSON.stringify({ token: data.token }))
      console.log('✅ 테스트 사용자 생성 및 토큰 저장 완료')
    }
    return data
  } catch (error) {
    console.error('Failed to create test user:', error)
    throw error
  }
}

// 포인트 관련 API
export async function getMyPoints() {
  const { data } = await api.get('/auth/points');
  return data;
}

export async function getMyPointHistory() {
  const { data } = await api.get('/auth/points/history');
  return data;
}

export async function withdrawPoints(amount, reason) {
  const { data } = await api.post('/points/withdraw', {
    amount,
    reason
  });
  return data;
}

// 은행 목록 관련 API
export async function getBankList() {
  const { data } = await api.get('/banks');
  return data;
}
