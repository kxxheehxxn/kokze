// src/api/goalApi.js
import axios from 'axios';

// 목표 전체 조회
export const fetchGoals = (userId, token) => {
  return axios.get('/api/goal', {
    params: { userId },
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// 목표 생성
export const createGoal = (userId, goalData, token) => {
  return axios.post('/api/goal', goalData, {
    params: { userId },
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// 목표 삭제
export const deleteGoalById = (goalId, token) => {
  return axios.delete(`/api/goal/${goalId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// 목표 상세조회
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

// 목표 수정
export const updateGoal = (goalId, goalData, token) => {
  return axios.put(`/api/goal/${goalId}`, goalData, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
