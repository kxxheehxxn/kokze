import api from '@/api';
const BASE_URL = '/api/inquiry';
export default {
  async getList(params) {
    //전체 리스트
    const { data } = await api.get(BASE_URL, { params });
    console.log('BOARD GET LIST: ', data);
    return data;
  },
  async getSearchList(params) {
    //검색 리스트
    const { data } = await api.get(`${BASE_URL}/search`, { params });
    console.log('BOARD GET SEARCH LIST: ', data);
    return data;
  },
  async create(article) {
    const { data } = await api.post(BASE_URL, article, {
      headers: { 'Content-Type': 'application/json' },
    });
    console.log('BOARD POST: ', data);
    return data;
  },
  async get(no) {
    const { data } = await api.get(`${BASE_URL}/${no}`);
    console.log('BOARD GET', data);
    return data;
  },
  async delete(no) {
    const { data } = await api.delete(`${BASE_URL}/${no}`);
    console.log('BOARD DELETE', data);
    return data;
  },
  async update(article) {
    console.log('article.infoId: ', article);
    const { data } = await api.patch(`${BASE_URL}/${article.infoId}`, article, {
      headers: { 'Content-Type': 'application/json' },
    });
    console.log('BOARD PATCH: ', data);
    return data;
  },
  async updateAnswer(article) {
    console.log('article.infoId: ', article);
    const { data } = await api.patch(
      `${BASE_URL}/${article.infoId}/answer`,
      article,
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    console.log('BOARD PATCH: ', data);
    return data;
  },
};
