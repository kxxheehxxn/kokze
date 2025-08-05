import axios from 'axios';

export const fetchProductList = async (page = 1, size = 8) => {
  const response = await axios.get(`/api/products`, {
    params: { page, size },
  });
  return response.data;
};

export const fetchProductDetail = async (finPrdtCd) => {
  const res = await axios.get(`/api/products/${finPrdtCd}`);
  return res.data;
};

export const fetchRecommendedProducts = async (userId) => {
  const response = await axios.get('/api/products/recommend', {
    params: { userId },
  });
  return response.data;
};

export async function filterProducts(filter) {
  try {
    const response = await axios.post('/api/products/filter', filter);
    return response.data;
  } catch (error) {
    console.error('상품 필터링 에러:', error);
    return [];
  }
}
