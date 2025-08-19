import api from './index'

export default {
  async getUserAssetSummary(userId) {
    try {
      const { data } = await api.get(`api/${userId}/summary`)
      return data
    } catch (error) {
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
  async updateBankAccount(userId) {
     try {
      const { data } = await api.get(`api/allaccount?userId=${userId}`)
    
      if (data.success) {
        console.log('CODEF 데이터 처리 완료')
        return data
      } else {
        throw new Error(data.message || '처리 실패')
      }
    } catch (error) {
    throw error
  }
  },
}