import axios from 'axios'

export async function fetchUserInfo() {
  try {
    // 로그인된 사용자 정보 (name, mbti, user_id)
    const { data } = await axios.get('/api/user/me')
    return data
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
    const { data } = await axios.get(`/api/point/${user_id}`)
    return data.point_amount
  } catch (error) {
    console.error('Failed to fetch user points:', error)
    throw error
  }
}

export async function getUserInfo() {
  // 실제 API 연동 전까지는 mock 데이터 사용
  return { name: '김콕재', mbti: '안정추구 거북이형', user_id: 1 }
}

export async function getUserPoints() {
  // 실제 API 연동 전까지는 mock 데이터 사용
  return 13700
}
