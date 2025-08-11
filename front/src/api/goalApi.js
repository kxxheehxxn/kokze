import axios from 'axios';

export const fetchGoals = (userId, token) => {
  return axios.get('/api/goal', {
    params: { userId },
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const createGoal = (userId, goalData, token) => {
  return axios.post('/api/goal', goalData, {
    params: { userId },
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const deleteGoalById = (goalId, token) => {
  return axios.delete(`/api/goal/${goalId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getGoalById = (goalId, token) => {
  return axios.get(`/api/goal/${goalId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getRecommendedProducts = (goalId, token) => {
  return axios.get(`/api/goal/${goalId}/recommend-products`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const updateGoal = (goalId, goalData, token) => {
  return axios.put(`/api/goal/${goalId}`, goalData, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getAccountsByUserId = (userId, token) => {
  return axios.get(`/api/goal/accounts/${userId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export function linkAccountToGoal(goalId, accountId, token) {
  return axios.post(
    `/api/goal/${goalId}/link-account`,
    { accountId },
    { headers: { Authorization: `Bearer ${token}` } }
  );
}

export const unlinkAccount = (accountId, token) => {
  return axios.delete(`/api/goal/unlink/${accountId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const fetchPastGoals = async (userId) => {
  const res = await axios.get('/api/goal/past', {
    params: { userId },
  });
  return res.data;
};

// 추천 목표 API 호출
export async function fetchRecommendedGoal(userId) {
  try {
    const response = await axios.get('/api/goal/recommend-next', {
      params: { userId },
    });
    return response.data;
  } catch (error) {
    console.error('❌ 추천 목표 가져오기 실패:', error);
    throw error;
  }
}
