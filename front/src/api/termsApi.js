import api from '@/api';

const BASE_URL = '/api/term';

export default {
  async fetchTerms() {
    const { data } = await api.get(BASE_URL);

    return data;
  },
};
