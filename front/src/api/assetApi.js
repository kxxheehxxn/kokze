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
  async getYeomsky95Assets() {
    try {
      const { data } = await api.get('AllAccount/yeomsky95/assets')
      return data
    } catch (error) {
      throw error
    }
  },
}
