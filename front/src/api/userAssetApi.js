import api from 'axios';

const BASE_URL = '/api/user';

export default {
  async getUserAssetSummary(userId) {
    try {
      // GET 요청: /api/user/{userId}/assets 엔드포인트로 요청
      // 예를 들어, userId가 '550e8400-e29b-41d4-a716-446655440001' 이면
      // 요청 URL은 '/api/user/550e8400-e29b-41d4-a716-446655440001/assets'가 됩니다.
      const { data } = await api.get(`${BASE_URL}/${userId}/assets`);
      console.log('User Asset Summary Data:', data); // 개발자 도구 콘솔에서 데이터를 확인
      return data;
    } catch (error) {
      console.log('Failed to fetch user asset summary:', error);
      // 에러 발생 시, 호출하는 쪽에서 catch 블록으로 처리할 수 있도록 에러를 다시 던집니다.
      throw error;
    }
  },
};
