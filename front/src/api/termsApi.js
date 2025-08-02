import api from '@/api'; // 여기서 'api'는 axios 인스턴스라고 가정합니다.

const BASE_URL = '/api/term';

export default {
  async fetchTerms() {
    //전체 리스트
    const { data } = await api.get(BASE_URL);
    console.log('TERM GET LIST: ', data);
    return data;
  },
};
