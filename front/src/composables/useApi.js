import { ref, computed } from 'vue'
import axios from 'axios'

export function useApi() {
  const loading = ref(false)
  const error = ref(null)
  const data = ref(null)

  const isLoading = computed(() => loading.value)
  const hasError = computed(() => error.value !== null)
  const errorMessage = computed(() => error.value?.message || '알 수 없는 오류가 발생했습니다.')

  const execute = async (apiCall) => {
    loading.value = true
    error.value = null
    
    try {
      const result = await apiCall()
      data.value = result
      return result
    } catch (err) {
      error.value = {
        message: err.response?.data?.message || err.message,
        status: err.response?.status,
        code: err.response?.data?.errorCode
      }
      throw err
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    loading.value = false
    error.value = null
    data.value = null
  }

  return {
    loading: isLoading,
    error: hasError,
    errorMessage,
    data,
    execute,
    reset
  }
}

// 특정 API 호출을 위한 래퍼
export function useApiCall(apiCall) {
  const { loading, error, errorMessage, data, execute, reset } = useApi()

  const call = async (...args) => {
    return await execute(() => apiCall(...args))
  }

  return {
    loading,
    error,
    errorMessage,
    data,
    call,
    reset
  }
} 