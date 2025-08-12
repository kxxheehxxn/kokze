import axios from 'axios'
import { unref } from 'vue'

export const fetchProductList = async (page = 1, size = 8) => {
  const response = await axios.get(`/api/products`, {
    params: { page, size },
  })
  return response.data
}

export const fetchProductDetail = async finPrdtCd => {
  const res = await axios.get(`/api/products/${finPrdtCd}`)
  return res.data
}

export const fetchRecommendedProducts = async userId => {
  const response = await axios.get('/api/products/recommend', {
    params: { userId },
  })
  return response.data
}

export async function filterProducts(filter) {
  try {
    const payload = {
      bankNames: Array.isArray(unref(filter.bankNames))
        ? [...unref(filter.bankNames)]
        : [],
      joinMembers: Array.isArray(unref(filter.joinMembers))
        ? [...unref(filter.joinMembers)]
        : [],
      productType: Array.isArray(unref(filter.productType))
        ? [...unref(filter.productType)]
        : [],
      minSaveTrm: filter.minSaveTrm ?? null,
      maxSaveTrm: filter.maxSaveTrm ?? null,
      minAmount: filter.minAmount ?? null,
      maxAmount: filter.maxAmount ?? null,
      spclCndKeywords: Array.isArray(unref(filter.spclCndKeywords))
        ? [...unref(filter.spclCndKeywords)]
        : [],
    }
    Object.keys(payload).forEach(k => {
      if (payload[k] === undefined) payload[k] = null
    })

    const res = await axios.post('/api/products/filter', payload, {
      headers: { 'Content-Type': 'application/json' },
    })
    return res.data
  } catch (error) {
    console.error('상품 필터링 에러:', error?.response?.data || error.message)
    return []
  }
}
