import api from '@/api';
const BASE_URL = '/api/notice';
export default {
  async getList(params) {
    //전체 리스트
    const { data } = await api.get(BASE_URL, { params });
    return data;
  },
  async getSearchList(params) {
    //검색 리스트
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
    const { data } = await api.patch(
      `${BASE_URL}/${article.noticeId}`,
      article,
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    return data;
  },
};
