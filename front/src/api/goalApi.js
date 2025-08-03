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
