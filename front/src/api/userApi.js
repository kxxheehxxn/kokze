import axios from 'axios';

export async function fetchUserInfo() {
  // 로그인된 사용자 정보 (name, mbti, user_id)
  const { data } = await axios.get('/api/user/me');
  return data;
}

export async function fetchUserPoint(user_id) {
  const { data } = await axios.get(`/api/point/${user_id}`);
  return data.point_amount;
} 