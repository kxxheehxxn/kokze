import api from 'axios'

export default {
  async getUserAssetSummary(userId) {
    try {
      const { data } = await api.get(`api/${userId}/summary`)
      return data
    } catch (error) {
      // 에러 발생 시, 호출하는 쪽에서 catch 블록으로 처리할 수 있도록 에러를 다시 던집니다.
      throw error
    }
  },
  async getUserBankAccounts(userId) {
    try {
      const { data } = await api.get(`api/${userId}/accounts`)
      return data
    } catch (error) {
      throw error
    }
  },
}
