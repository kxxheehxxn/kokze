import api from 'axios'

export default {
  async getUserAssetSummary(userId) {
    try {
      const { data } = await api.get(`api/${userId}/summary`)
      console.log('User Asset Summary Data:', data) // 개발자 도구 콘솔에서 데이터를 확인
      return data
    } catch (error) {
      console.log('Failed to fetch user asset summary:', error)
      // 에러 발생 시, 호출하는 쪽에서 catch 블록으로 처리할 수 있도록 에러를 다시 던집니다.
      throw error
    }
  },
  async getUserBankAccounts(userId) {
    try {
      const { data } = await api.get(`api/${userId}/accounts`)
      console.log('User Bank Accounts Data:', data) // 개발자 도구 콘솔에서 데이터를 확인
      return data
    } catch (error) {
      console.log('Failed to fetch user bank accounts:', error)
      throw error
    }
  },
}
