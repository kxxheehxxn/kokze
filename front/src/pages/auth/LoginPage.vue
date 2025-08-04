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
const disableSubmit = computed(() => !(user.email && user.password));

const handleLogin = async () => {
  if (!user.email) {
    alert('이메일을 입력해주세요.');
    return;
  }
  if (!user.password) {
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
    <router-link to="/" class="logo-section text-decoration-none">
      <div class="logo d-flex align-items-center">
        <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
      </div>
    </router-link>

    <div class="login-box">
      <div class="title">로그인</div>

      <div class="form-group">
        <label for="email">이메일</label>
        <input
          id="email"
          type="email"
          v-model="user.email"
          placeholder="이메일을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input
          id="password"
          type="password"
          v-model="user.password"
          placeholder="비밀번호를 입력하세요"
        />
      </div>

      <div class="forgot-password">
        <router-link to="/find-password">비밀번호를 잊으셨나요?</router-link>
      </div>

      <button class="login-button button-like" @click="handleLogin">
        로그인
      </button>
      <div
        class="kakao-login button-like"
        alt="Kakao login"
        @click="handleKakaoLogin"
      >
        <img :src="kakaoLogin" width="40px" class="kakao-logo" />
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
.container {
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 0 16px 40px 16px;
  position: relative;
}

/* header */
.header {
  position: absolute;
  top: 20px;
  left: 20px;
}

.logo {
  height: 50px;
  width: 50px;
  object-fit: cover;
}

.logo-section {
  cursor: pointer;
  transition: transform 0.2s ease;
  background-color: transparent;
  flex-shrink: 0;
  margin-left: 20px;
  margin-top: 10px;
  margin-bottom: 0;
  align-self: flex-start;
}
.logo-section:hover {
  transform: scale(1.05);
}
.logo {
  background-color: transparent;
}
.logo-icon {
  width: 54px;
  height: 54px;
  background: transparent !important;
  border-radius: 50%;
  padding: 2px;
  object-fit: contain;
}

/* login-box */
.login-box {
  background-color: #fff;
  width: 100%;
  max-width: 900px;
  padding: 90px 140px;
  border-radius: 28px;
  box-shadow: 0 0 20px #85858540;
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.title {
  background-color: #fff;
  font-size: 24px;
  font-weight: 600;
  text-align: left;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  color: #686868;
  font-size: 14px;
  margin-bottom: 6px;
}

.form-group input {
  background-color: #fff;
  border-radius: 12px;
  border: 1px solid #d9d9d9;
  padding: 14px 16px;
  font-size: 16px;
  box-shadow: inset 0 0 5px #eee;
}

.forgot-password {
  background-color: #fff;
  text-align: right;
  color: #3573ee;
  font-size: 14px;
  cursor: pointer;
}

.button-like {
  width: 100%;
  height: 60px;
  border-radius: 12px;
  display: block;
}

.login-button {
  background-color: #3573ee;
  color: white;
  padding: 0;
  border: none;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
}

.kakao-login {
  display: flex;
  align-items: center;
  position: relative;
  cursor: pointer;
  border: none;
  background-color: #fee500;
  font-size: 18px;
  color: #181600;
  border-radius: 12px;
  width: 100%;
  height: 60px;
  padding: 0; /* 내부 여백은 로고/텍스트에서 조절 */
  box-sizing: border-box;
}

.kakao-login {
  position: relative; /* 텍스트 절대 위치 기준 */
  display: flex;
  align-items: center;
  cursor: pointer;
  border: none;
  background-color: #fee500;
  font-size: 18px;
  color: #181600;
  border-radius: 12px;
  width: 100%;
  height: 60px;
}

.kakao-logo {
  position: absolute;
  left: 20px; /* 왼쪽 여백 */
  width: 40px;
  height: auto;
  object-fit: contain;
}

.kakao-login-text {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-weight: 600;
  white-space: nowrap;
}
.sign-up-prompt {
  background-color: #fff;
  text-align: center;
  font-size: 16px;
}

.sign-up {
  color: #3573ee;
  text-decoration: underline;
  cursor: pointer;
}

/* 모바일 화면 대응 */
@media (max-width: 768px) {
  .login-box {
    max-width: 100%;
    padding: 40px 30px;
    border-radius: 20px;
  }

  .title {
    font-size: 22px;
  }

  .login-button,
  .button-like {
    height: 50px;
    font-size: 16px;
  }

  .form-group input {
    font-size: 15px;
    padding: 12px 14px;
  }

  .sign-up-prompt {
    font-size: 14px;
  }
}
</style>
