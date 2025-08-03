import api from 'axios'

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
}
