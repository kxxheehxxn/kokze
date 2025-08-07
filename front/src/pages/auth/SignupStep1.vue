<script setup>
import { reactive, ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { userAuthStore } from '@/stores/auth';
import PrivacyPolicyModal from '@/components/PrivacyPolicyModal.vue';
import {
  sendSignupVerificationCode,
  verifySignupCode,
} from '@/api/passwordApi.js';
const router = useRouter();
const authStore = userAuthStore();
const isKakao = computed(() => authStore.isKakao);
const today = ref('');
const userInfo = reactive({
  name: '',
  gender: 'male',
  birth: '',
  phone1: '',
  phone2: '',
  phone3: '',
  emailId: '',
  emailDomain: '',
  emailCode: '',
  password: '',
  passwordConfirm: '',
  agreed: false,
  agreeSub0: false,
  agreeSub1: false,
  agreeSub2: false,
});
onMounted(() => {
  // 오늘 날짜 (YYYY-MM-DD)
  today.value = new Date().toISOString().split('T')[0];
  if (isKakao.value) {
    const emailParts = authStore.userInfo.email.split('@');
    userInfo.emailId = emailParts[0];
    userInfo.emailDomain = emailParts[1];
    userInfo.name = authStore.userInfo.name;
  }
});
const emailSent = ref(false);
const emailSentError = ref('');
const emailVerified = ref(false);
const emailVerifiedError = ref('');
const passwordError = ref('');
const isPolicyModalOpen = ref(false);
const sendEmailVerification = async () => {
  const fullEmail = `${userInfo.emailId}@${userInfo.emailDomain}`;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(fullEmail)) {
    emailSentError.value = '이메일을 정확히 입력해주세요.';
    emailSent.value = false;
    return;
  }
  try {
    const res = await axios.get(`/api/auth/signup/check/${fullEmail}`);
    if (res.data === true) {
      emailSentError.value = '이미 존재하는 이메일입니다.';
      emailSent.value = false;
    } else {
      try {
        const success = await sendSignupVerificationCode(fullEmail);
        if (success) {
          emailSentError.value = '';
          emailSent.value = true;
        } else {
          emailSentError.value = '인증번호 발송에 실패했습니다.';
          emailSent.value = false;
        }
      } catch (error) {
        emailSentError.value = '인증번호 발송에 실패했습니다.';
        emailSent.value = false;
      }
    }
  } catch (err) {
    console.error('이메일 중복 확인 실패', err);
    emailSentError.value = '서버 오류가 발생했습니다.';
    emailSent.value = false;
  }
};
const confirmEmailCode = async () => {
  const fullEmail = `${userInfo.emailId}@${userInfo.emailDomain}`;
  try {
    const success = await verifySignupCode(userInfo.emailCode, fullEmail);
    emailVerified.value = success;
    emailVerifiedError.value = success ? '' : '인증번호가 올바르지 않습니다.';
  } catch (error) {
    console.error('인증번호 확인 실패:', error);
    emailVerified.value = false;
    emailVerifiedError.value = '인증번호 확인에 실패했습니다.';
  }
};
const passwordConditions = computed(() => {
  const pw = userInfo.password;
  return {
    length: pw.length >= 8,
    hasLetter: /[a-zA-Z]/.test(pw),
    hasNumber: /[0-9]/.test(pw),
    hasSpecial: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(pw),
  };
});
watch(
  () => [userInfo.password, userInfo.passwordConfirm],
  ([pw, pwConfirm]) => {
    if (!pwConfirm) {
      passwordError.value = '';
      return;
    }
    passwordError.value =
      pw !== pwConfirm ? '비밀번호가 일치하지 않습니다.' : '';
  }
);
const passwordSuccess = computed(() => {
  return (
    userInfo.password &&
    userInfo.passwordConfirm &&
    userInfo.password === userInfo.passwordConfirm
  );
});
watch(
  () => userInfo.agreed,
  (val) => {
    userInfo.agreeSub0 = val;
    userInfo.agreeSub1 = val;
    userInfo.agreeSub2 = val;
  }
);
watch(
  () => userInfo.agreeSub0,
  (val) => {
    userInfo.agreed = val;
    userInfo.agreeSub1 = val;
    userInfo.agreeSub2 = val;
  }
);
const openModal = () => {
  isPolicyModalOpen.value = true;
};
const handlePolicyAgree = (agreeData) => {
  userInfo.agreed = agreeData.agreed;
  userInfo.agreeSub0 = agreeData.agreeSub0;
  userInfo.agreeSub1 = agreeData.agreeSub1;
  userInfo.agreeSub2 = agreeData.agreeSub2;
  isPolicyModalOpen.value = false;
};
const isFormValid = computed(() => {
  const requiredFields =
    userInfo.name &&
    userInfo.gender &&
    userInfo.birth &&
    userInfo.phone1 &&
    userInfo.phone2 &&
    userInfo.phone3 &&
    userInfo.agreed;
  if (isKakao.value) {
    return requiredFields;
  } else {
    return (
      requiredFields &&
      userInfo.emailId &&
      userInfo.emailDomain &&
      userInfo.password &&
      userInfo.passwordConfirm &&
      emailVerified.value &&
      userInfo.password === userInfo.passwordConfirm
    );
  }
});
const goNext = () => {
  if (!isFormValid.value) return;
  authStore.setUserInfo('name', userInfo.name);
  authStore.setUserInfo('sex', userInfo.gender);
  authStore.setUserInfo('birthDate', userInfo.birth);
  authStore.setUserInfo(
    'phoneNum',
    `${userInfo.phone1}-${userInfo.phone2}-${userInfo.phone3}`
  );
  authStore.setUserInfo('email', `${userInfo.emailId}@${userInfo.emailDomain}`);
  if (!isKakao.value) {
    authStore.setUserInfo('password', userInfo.password);
  }
  router.push('/signup/step2');
};
</script>
<template>
  <div class="container">
    <div class="no-nav-header">
      <router-link to="/" class="logo-section text-decoration-none">
        <div class="logo d-flex align-items-center">
          <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
        </div>
      </router-link>
    </div>
    <div class="signup-box">
      <div class="sign-top">
        <div class="title">
          콕재 서비스를 이용하려면<br />회원 가입이 필요해요
        </div>
        <div class="page-num">1/3</div>
      </div>
      <hr />
      <div class="title">개인정보 입력</div>
      <div class="form-group">
        <label>이름</label>
        <input
          v-model="userInfo.name"
          placeholder="이름을 입력하세요"
          :readonly="isKakao"
          :class="{ 'readonly-input': isKakao }"
        />
      </div>
      <div class="form-group">
        <label>성별</label>
        <div class="gender-group">
          <label>
            <input type="radio" value="male" v-model="userInfo.gender" />
            남성</label
          >
          <label>
            <input type="radio" value="female" v-model="userInfo.gender" />
            여성</label
          >
        </div>
      </div>
      <div class="form-group">
        <label>생년월일</label>
        <input type="date" v-model="userInfo.birth" :max="today" />
      </div>
      <div class="form-group">
        <label>전화번호</label>
        <div class="phone-group">
          <select v-model="userInfo.phone1">
            <option value="">선택</option>
            <option value="010">010</option>
            <option value="011">011</option>
          </select>
          <input v-model="userInfo.phone2" maxlength="4" />
          <input v-model="userInfo.phone3" maxlength="4" />
        </div>
      </div>
      <div class="form-group">
        <label>이메일</label>
        <div class="email-group">
          <input
            v-model="userInfo.emailId"
            :readonly="isKakao"
            :class="{ 'readonly-input': isKakao }"
          />
          <span>@</span>
          <select
            v-model="userInfo.emailDomain"
            :disabled="isKakao"
            :class="{ 'readonly-input': isKakao }"
          >
            <option value="">선택</option>
            <option value="example.com">example.com</option>
            <option value="gmail.com">gmail.com</option>
            <option value="naver.com">naver.com</option>
            <option value="daum.net">daum.net</option>
          </select>
          <button
            @click="sendEmailVerification"
            :disabled="isKakao"
            :class="{ 'readonly-button': isKakao }"
          >
            인증
          </button>
        </div>
        <p v-if="emailSent" class="hint">이메일로 인증번호가 전송되었습니다.</p>
        <p v-else-if="emailSentError" class="error">
          {{ emailSentError }}
        </p>
      </div>
      <div class="form-group">
        <label>인증번호</label>
        <div class="auth-group">
          <input
            v-model="userInfo.emailCode"
            :readonly="isKakao"
            :class="{ 'readonly-input': isKakao }"
          />
          <button
            @click="confirmEmailCode"
            :disabled="isKakao"
            :class="{ 'readonly-button': isKakao }"
          >
            확인
          </button>
        </div>
        <p v-if="emailVerified" class="success">이메일 인증 완료</p>
        <p v-else-if="emailVerifiedError" class="error">
          {{ emailVerifiedError }}
        </p>
      </div>
      <div class="form-group">
        <label>비밀번호</label>
        <input
          type="password"
          v-model="userInfo.password"
          placeholder="비밀번호 입력"
          :readonly="isKakao"
          :class="{ 'readonly-input': isKakao }"
        />
        <ul class="password-rules">
          <li :class="passwordConditions.length ? 'success' : 'hint'">
            8자 이상
          </li>
          <li :class="passwordConditions.hasLetter ? 'success' : 'hint'">
            영문자 포함
          </li>
          <li :class="passwordConditions.hasNumber ? 'success' : 'hint'">
            숫자 포함
          </li>
          <li :class="passwordConditions.hasSpecial ? 'success' : 'hint'">
            특수문자 포함
          </li>
        </ul>
      </div>
      <div class="form-group">
        <label>비밀번호 확인</label>
        <input
          type="password"
          v-model="userInfo.passwordConfirm"
          placeholder="비밀번호 재입력"
          :readonly="isKakao"
          :class="{ 'readonly-input': isKakao }"
        />
        <p class="success" v-if="passwordSuccess">비밀번호가 일치합니다.</p>
        <p class="error" v-if="passwordError">{{ passwordError }}</p>
      </div>
      <hr />
      <div class="agreement">
        <div class="agreement-con1">
          <label>
            <input type="checkbox" v-model="userInfo.agreed" />
            <strong>[필수] 개인(신용)정보 처리 동의</strong>
          </label>
        </div>
        <div class="agreement-con2">
          <div class="agreement-left">
            <div class="agreement-item">
              <label>
                <input type="checkbox" v-model="userInfo.agreeSub0" />
                개인(신용)정보 수집·이용 동의
              </label>
            </div>
            <div class="agreement-sub-items">
              <label>
                <input type="checkbox" v-model="userInfo.agreeSub1" disabled />
                고유식별정보
              </label>
              <label>
                <input type="checkbox" v-model="userInfo.agreeSub2" disabled />
                개인(신용)정보
              </label>
            </div>
          </div>
          <div class="agreement-right">
            <button @click="openModal">＞</button>
          </div>
        </div>
      </div>
      <div class="button-group">
        <button class="btn cancel-button" @click="router.push('/')">
          취소하기
        </button>
        <button
          :disabled="!isFormValid"
          class="btn next-button"
          @click="goNext"
        >
          다음 단계
        </button>
      </div>
    </div>
  </div>
  <PrivacyPolicyModal
    v-if="isPolicyModalOpen"
    @close="isPolicyModalOpen = false"
    @agree="handlePolicyAgree"
  />
</template>
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 0 16px 40px 16px;
  position: relative;
}
.title {
  font-size: 24px;
  font-weight: 600;
  text-align: left;
  flex-shrink: 0;
}
.page-num {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #777;
  white-space: nowrap;
  flex-shrink: 0;
}
.page-num::after {
  content: '';
  flex-grow: 1;
  height: 1px;
  display: inline-block;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.form-group label {
  font-size: 15px;
  font-weight: 600;
  color: #222;
}
.form-group input,
.form-group select {
  border: 1px solid #ccc;
  border-radius: 30px;
  padding: 12px 14px;
  font-size: 16px;
  width: 100%;
  font-family: inherit;
}
.gender-group {
  display: flex;
  gap: 40px;
}
.gender-group label {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
  font-weight: 500;
  white-space: nowrap;
}
.phone-group,
.email-group,
.auth-group {
  display: flex;
  align-items: center;
  gap: 8px;
}
.phone-group select,
.email-group select {
  min-width: 100px;
}
.readonly-input {
  background-color: #f2f2f2;
}
.readonly-button {
  background-color: #e0e0e0;
  color: #999;
  cursor: not-allowed;
  border: none;
}
.email-group button,
.auth-group button {
  height: 42px;
  padding: 0 16px;
  font-size: 15px;
  font-weight: 500;
  background-color: #3573ee;
  color: #fff;
  border: none;
  border-radius: 30px;
  white-space: nowrap;
  cursor: pointer;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.email-group button:hover,
.auth-group button:hover {
  background-color: #255edb;
}
.password-rules {
  margin-top: 6px;
  font-size: 13px;
  padding-left: 16px;
  list-style: disc;
}
.agreement {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.agreement-con1 {
  background-color: #f5f5f5;
  padding: 16px 24px;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
}
.agreement-con1 label {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 500;
}
.agreement-con2 {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 16px 24px;
}
.agreement-left {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.agreement-item label {
  font-size: 15px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}
.agreement-sub-items {
  display: flex;
  gap: 40px;
  padding-left: 28px;
  font-size: 14px;
}
.agreement-sub-items label {
  display: flex;
  align-items: center;
  gap: 6px;
}
.agreement-right button {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  padding: 0 6px;
}
.agreement-con2 input[type='checkbox'] {
  appearance: none;
  -webkit-appearance: none;
  width: 16px;
  height: 16px;
  margin: 0;
  border: none;
  background: none;
  position: relative;
  cursor: pointer;
}
.agreement-con2 input[type='checkbox']::after {
  content: '✔';
  position: absolute;
  top: 0;
  left: 0;
  font-size: 16px;
  line-height: 1;
  color: gray;
}
.agreement-con2 input[type='checkbox']:checked::after {
  color: #3573ee;
}
.hint {
  font-size: 13px;
  color: gray;
}
.success {
  font-size: 13px;
  color: green;
}
.error {
  font-size: 13px;
  color: red;
}
.button-group {
  display: flex;
  gap: 10px;
  justify-content: space-between;
  margin-top: 20px;
}
.btn {
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
}
.cancel-button {
  background: #fafbfc;
  color: #222;
  border: none;
  border-radius: 30px;
  padding: 14px 24px;
  min-width: 200px;
}
.next-button {
  background: #3573ee;
  color: white;
  border: none;
  border-radius: 30px;
  padding: 14px 24px;
  min-width: 200px;
}
.next-button:disabled {
  background: #a5c2ff;
  cursor: not-allowed;
}
.next-button:hover:enabled {
  background-color: #255edb;
}
</style>
