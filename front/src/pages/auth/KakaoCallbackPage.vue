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

const router = useRouter();
const auth = userAuthStore();

onMounted(async () => {
    try {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');

        if (!code) {
            console.error('인증 코드가 없습니다.');
            router.push('/auth/login');
            return;
        }
        
        // 실제 카카오 엔드포인트 사용
        const response = await fetch(
            `http://localhost:8080/api/auth/kakao/callback?code=${code}`,
            {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }
        );

        const result = await response.json();
        console.log('카카오 로그인 응답:', result);

        if (response.status === 200 && result.success && result.data) {
            const authData = result.data;
            
            if (authData.newUser === true) {
                auth.setAllUserInfo({
                    name: authData.user.username || '카카오 사용자',
                    email: authData.user.email || '',
                });
                auth.isKakao = true;
                auth.setToken(authData.accessToken);

                console.log('신규 사용자 - 회원가입 페이지로 이동');
                router.push('/signup/step1');
            } else {
                auth.login({
                    email: authData.user.email,
                    token: authData.accessToken,
                    userId: authData.user.userId,
                    userName: authData.user.username,
                });

                console.log('기존 사용자 - 메인 페이지로 이동');
                router.push('/');
            }
        } else {
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
