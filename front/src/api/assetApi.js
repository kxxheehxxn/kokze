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
      // 백엔드에서 CODEF 처리 후 성공 여부만 반환
      const { data } = await api.get(`api/allaccount?userId=${userId}`)
    
      // 성공 응답 확인
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
