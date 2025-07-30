<template>
    <div class="callback-container">
        <div class="loading-spinner">
            <div class="spinner"></div>
            <p>카카오 로그인 처리 중...</p>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';
import axios from 'axios';

const router = useRouter();
const auth = userAuthStore();

onMounted(async () => {
    try {
        // URL에서 인증 코드 추출
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');

        if (!code) {
            console.error('인증 코드가 없습니다.');
            router.push('/auth/login');
            return;
        }

        // 백엔드로 인증 코드 전송하여 사용자 정보 받기
        // 직접 백엔드 URL 사용
        const response = await fetch(
            `http://localhost:8080/api/auth/kakao/callback?code=${code}`,
            {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }
        );

        // 프록시를 사용하는 경우 (현재 작동하지 않음)
        // const response = await fetch(`/api/auth/kakao/callback?code=${code}`, {
        //   method: 'GET',
        //   headers: {
        //     'Accept': 'application/json'
        //   }
        // })

        const result = await response.json();

        if (response.status === 200 && result.token && result.user) {
            // ✅ 로그인 성공
            console.log('카카오 로그인 성공:', result);

            auth.login({
                email: result.user.email,
                token: result.token,
                userId: result.user.userId,
            });

            router.push('/'); // 홈으로 이동
        } else if (response.status === 401) {
            // ✅ 회원가입이 필요한 경우
            console.log('카카오 유저 미가입 상태, 회원가입 진행');

            auth.setAllUserInfo({
                name: result.name || '카카오 사용자',
                email: result.email || '',
            });
            auth.isKakao = true;

            router.push('/signup/step1');
        } else {
            // ❌ 예외
            console.error('카카오 로그인 실패:', result);
            alert('카카오 로그인에 실패했습니다.');
            router.push('/auth/login');
        }
    } catch (error) {
        console.error('카카오 로그인 처리 중 오류:', error);
        alert('카카오 로그인 처리 중 오류가 발생했습니다.');
        router.push('/auth/login');
    }
});
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
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}

p {
    color: #666;
    font-size: 16px;
}
</style>
