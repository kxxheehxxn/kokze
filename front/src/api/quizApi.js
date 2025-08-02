import axios from 'axios';

// API 베이스 URL 설정
const API_BASE_URL = 'http://localhost:8080/api/quiz';

export const quizApi = {
  async getTodayQuiz(userId) {
    try {
      const response = await axios.get(`${API_BASE_URL}/today`, {
        params: { userId },
      });

      console.log('오늘의 퀴즈 조회 성공:', response.data);
      return response.data;
    } catch (error) {
      console.error('오늘의 퀴즈 조회 실패:', error);

      // 에러 상태별 처리
      if (error.response) {
        const status = error.response.status;

        switch (status) {
          case 400:
            throw new Error('잘못된 요청입니다. 사용자 정보를 확인해주세요.');
          case 409:
            throw new Error('오늘은 이미 퀴즈를 풀었습니다. 내일 다시 도전해보세요!');
          case 500:
            throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
          default:
            throw new Error('퀴즈를 불러오는데 실패했습니다.');
        }
      } else {
        throw new Error('네트워크 오류가 발생했습니다.');
      }
    }
  },

  /**
   * 퀴즈 정답 제출
   * @param {string} userId - 사용자 ID
   * @param {number} quizId - 퀴즈 ID
   * @param {string} userAnswer - 사용자 답안
   * @returns {Promise<Object>} 정답 확인 결과
   */
  async submitAnswer(userId, quizId, userAnswer) {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/submit`,
        {
          quiz_id: quizId,
          user_answer: userAnswer,
        },
        {
          params: { userId },
        }
      );

      console.log('정답 제출 성공:', response.data);
      return response.data;
    } catch (error) {
      console.error('정답 제출 실패:', error);

      // 에러 상태별 처리
      if (error.response) {
        const status = error.response.status;

        switch (status) {
          case 400:
            throw new Error('잘못된 요청입니다. 입력값을 확인해주세요.');
          case 404:
            throw new Error('존재하지 않는 퀴즈입니다.');
          case 409:
            throw new Error('오늘은 이미 퀴즈를 풀었습니다.');
          case 500:
            throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
          default:
            throw new Error('정답 제출에 실패했습니다.');
        }
      } else {
        throw new Error('네트워크 오류가 발생했습니다.');
      }
    }
  },
};
