<script setup>
import { reactive, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';
import kakaoLogin from '@/assets/images/kakao_logo.PNG';

const router = useRouter();
const auth = userAuthStore();

const user = reactive({
  email: '',
  password: '',
});

const error = ref('');
const disableSubmit = computed(
  () => !(user.email.trim() && user.password.trim())
);

const handleLogin = async () => {
  if (!user.email.trim()) {
    alert('이메일을 입력해주세요.');
    return;
  }
  if (!user.password.trim()) {
    alert('비밀번호를 입력해주세요.');
    return;
  }
  try {
    await auth.login(user);
    router.push('/');
  } catch (e) {
    console.error('에러=======', e);
    alert(e.response?.data || '로그인에 실패했습니다. 다시 시도해주세요.');
  }
};

const handleKakaoLogin = () => {
  const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID;
  const REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI;
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
  window.location.href = kakaoAuthUrl;
};

const handleSignup = () => {
  router.push('/signup/step1');
};
</script>

<template>
  <div class="container">
    <router-link to="/" class="logo-section" aria-label="홈으로 이동">
      <div class="logo">
        <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
      </div>
    </router-link>

    <div class="login-box" role="form" aria-label="로그인 폼">
      <div class="title">로그인</div>

      <div class="form-group">
        <label for="email">이메일</label>
        <input
          id="email"
          type="email"
          v-model="user.email"
          placeholder="이메일을 입력하세요"
          autocomplete="username"
          aria-required="true"
        />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input
          id="password"
          type="password"
          v-model="user.password"
          placeholder="비밀번호를 입력하세요"
          autocomplete="current-password"
          aria-required="true"
        />
      </div>

      <div class="forgot-password">
        <router-link to="/find-password">비밀번호를 잊으셨나요?</router-link>
      </div>

      <button
        class="login-button button-like"
        :disabled="disableSubmit"
        @click="handleLogin"
        :aria-disabled="disableSubmit"
      >
        로그인
      </button>

      <div
        class="kakao-login button-like"
        role="button"
        tabindex="0"
        @click="handleKakaoLogin"
        @keyup.enter="handleKakaoLogin"
        aria-label="카카오 로그인"
      >
        <div class="kakao-logo-wrapper">
          <img :src="kakaoLogin" alt="Kakao 로고" class="kakao-logo" />
        </div>
        <span class="kakao-login-text">카카오 로그인</span>
      </div>

      <p class="sign-up-prompt">
        <span>아직 콕재 회원이 아닌가요?&nbsp;</span>
        <span class="sign-up" @click="handleSignup">가입하기</span>
      </p>
    </div>
  </div>
</template>

<style>
:root {
  --radius: 12px;
  --shadow: 0 0 20px rgba(133, 133, 133, 0.25);
  --primary: #3573ee;
  --text-default: #2a2a2a;
  --muted: #686868;
  --bg: #fafafa;
}

* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
    'Helvetica Neue', Arial, sans-serif;
  color: var(--text-default);
}

.container {
  background-color: var(--bg);
  min-height: 100vh;
  padding: 0 1rem 2.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}
.login-box {
  background-color: #fff;
  width: 100%;
  max-width: 900px;
  padding: 70px 40px 50px;
  border-radius: 28px;
  box-shadow: var(--shadow);
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-top: 10px;
}

.title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 4px;
}

.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-size: 14px;
  margin-bottom: 6px;
  color: var(--muted);
}
.form-group input {
  border: 1px solid #d9d9d9;
  border-radius: var(--radius);
  padding: 14px 16px;
  font-size: 16px;
  background: #fff;
  outline: none;
  box-shadow: inset 0 0 5px #eee;
  width: 100%;
}
.form-group input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(53, 115, 238, 0.2);
}

.forgot-password {
  text-align: right;
  font-size: 14px;
  margin-top: 4px;
}
.forgot-password a {
  color: var(--primary);
  text-decoration: none;
}
.forgot-password a:hover {
  text-decoration: underline;
}

.button-like {
  width: 100%;
  border: none;
  cursor: pointer;
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  transition: opacity 0.2s ease;
}

.login-button {
  background-color: var(--primary);
  color: #fff;
  font-size: 18px;
  height: 56px;
}
.login-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.kakao-login {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fee500;
  color: #181600;
  font-size: 18px;
  height: 56px;
  gap: 8px;
  padding: 0 16px;
  margin-top: 4px;
}
.kakao-logo-wrapper {
  position: absolute;
  left: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.kakao-logo {
  width: 32px;
  height: auto;
  object-fit: contain;
}
.kakao-login-text {
  font-weight: 600;
  white-space: nowrap;
}

.sign-up-prompt {
  text-align: center;
  font-size: 15px;
  margin-top: 4px;
}
.sign-up {
  color: var(--primary);
  text-decoration: underline;
  cursor: pointer;
}
.sign-up:hover {
  opacity: 0.9;
}
@media (max-width: 1024px) and (orientation: portrait) {
  .login-box {
    min-height: 80vh; /* 화면 높이의 80% 이상 확보 */
    width: 90vw;
  }
}
</style>
