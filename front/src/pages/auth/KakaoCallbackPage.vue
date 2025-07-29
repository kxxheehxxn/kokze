<template>
  <div class="callback-container">
    <div class="loading-spinner">
      <div class="spinner"></div>
      <p>카카오 로그인 처리 중...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { userAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = userAuthStore()

onMounted(async () => {
  try {
    // URL에서 인증 코드 추출
    const urlParams = new URLSearchParams(window.location.search)
    const code = urlParams.get('code')
    
    if (!code) {
      console.error('인증 코드가 없습니다.')
      router.push('/auth/login')
      return
    }

    // 백엔드로 인증 코드 전송하여 사용자 정보 받기
    // 직접 백엔드 URL 사용
    const response = await fetch(`http://localhost:8080/api/auth/kakao/callback?code=${code}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    })
    
    // 프록시를 사용하는 경우 (현재 작동하지 않음)
    // const response = await fetch(`/api/auth/kakao/callback?code=${code}`, {
    //   method: 'GET',
    //   headers: {
    //     'Accept': 'application/json'
    //   }
    // })

    if (response.ok) {
      const result = await response.json()
      console.log('카카오 로그인 성공:', result)
      
      // 로그인 성공 시 사용자 정보 저장
      auth.login({ 
        email: result.user?.email || 'kakao_user',
        token: result.token 
      })
      
      // 홈으로 이동
      router.push('/')
    } else {
      console.error('카카오 로그인 실패')
      alert('카카오 로그인에 실패했습니다.')
      router.push('/auth/login')
    }
  } catch (error) {
    console.error('카카오 로그인 처리 중 오류:', error)
    alert('카카오 로그인 처리 중 오류가 발생했습니다.')
    router.push('/auth/login')
  }
})
</script>

<style scoped>
.callback-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #fafafa;
}

.loading-spinner {
  text-align: center;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3573ee;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

p {
  color: #666;
  font-size: 16px;
}
</style> 