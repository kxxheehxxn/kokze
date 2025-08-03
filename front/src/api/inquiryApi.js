import api from '@/api';

const BASE_URL = '/api/inquiry';

export default {
  async getList(params) {
    const { data } = await api.get(BASE_URL, { params });
    return data;
  },
  async getSearchList(params) {
    const { data } = await api.get(`${BASE_URL}/search`, { params });
    return data;
  },
  async create(article) {
    const { data } = await api.post(BASE_URL, article, {
      headers: { 'Content-Type': 'application/json' },
    });
    return data;
  },
  async get(no) {
    const { data } = await api.get(`${BASE_URL}/${no}`);
    return data;
  },
  async delete(no) {
    const { data } = await api.delete(`${BASE_URL}/${no}`);
    return data;
  },
  async update(article) {
    const { data } = await api.patch(`${BASE_URL}/${article.infoId}`, article, {
      headers: { 'Content-Type': 'application/json' },
    });
    return data;
  },
  async updateAnswer(article) {
    const { data } = await api.patch(
      `${BASE_URL}/${article.infoId}/answer`,
      article,
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    return data;
  },
  async getFaqList() {
    const { data } = await api.get(`${BASE_URL}/faq`);
    return data;
  },
  async increaseViewCount(infoId) {
    await api.patch(`${BASE_URL}/${infoId}/increaseViewCount`);
  },
};
