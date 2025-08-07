<template>
  <UserCardLayout>
    <h2 class="title">비밀번호 수정</h2>
    <form class="pw-form" @submit.prevent="onSubmit">
      <div class="form-group">
        <label class="label">현재 비밀번호</label>
        <input
          v-model="currentPassword"
          type="password"
          class="input"
          placeholder="현재 비밀번호를 입력하세요."
        />
      </div>
      <div class="form-group">
        <label class="label">새 비밀번호</label>
        <input
          v-model="password"
          type="password"
          class="input"
          placeholder="새 비밀번호를 입력하세요."
        />
        <!-- <div v-if="password && !passwordStrength.valid" class="error-msg">
          {{ passwordStrength.message }}
        </div> -->
        <ul class="password-rules">
          <li :class="passwordStrength.length ? 'success' : 'hint'">
            8자 이상
          </li>
          <li :class="passwordStrength.hasLetter ? 'success' : 'hint'">
            영문자 포함
          </li>
          <li :class="passwordStrength.hasNumber ? 'success' : 'hint'">
            숫자 포함
          </li>
          <li :class="passwordStrength.hasSpecial ? 'success' : 'hint'">
            특수문자 포함
          </li>
        </ul>
      </div>
      <div class="form-group">
        <label class="label">새 비밀번호 확인</label>
        <input
          v-model="passwordCheck"
          type="password"
          class="input"
          placeholder="새 비밀번호를 다시 입력하세요."
        />
        <div
          v-if="passwordCheck && password !== passwordCheck"
          class="error-msg"
        >
          비밀번호가 일치하지 않습니다!
        </div>
      </div>
      <div class="mt-5 text-center">
        <button type="button" class="btn cancel-btn" @click="onCancel">
          취소하기
        </button>
        <button
          type="submit"
          class="btn submit-btn ms-4"
          :disabled="!canSubmit || loading"
        >
          {{ loading ? '수정 중...' : '수정' }}
        </button>
      </div>
      <div v-if="error" class="error-msg">{{ error }}</div>
    </form>
  </UserCardLayout>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import UserCardLayout from '@/components/UserCardLayout.vue';
import { updatePassword } from '@/api/userApi';

const currentPassword = ref('');
const password = ref('');
const passwordCheck = ref('');
const loading = ref(false);
const error = ref('');
const router = useRouter();

const passwordStrength = computed(() => {
  const pwd = password.value;
  if (!pwd) return { valid: false };

  const hasLength = pwd.length >= 8;
  const hasLetter = /[a-zA-Z]/.test(pwd);
  const hasNumber = /\d/.test(pwd);
  const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(pwd);

  const valid = hasLength && hasLetter && hasNumber && hasSpecial;
  return {
    valid,
  };
});

const canSubmit = computed(
  () =>
    currentPassword.value &&
    password.value &&
    passwordStrength.value.valid &&
    password.value === passwordCheck.value
);

function onCancel() {
  router.back();
}
async function onSubmit() {
  if (!canSubmit.value) return;

  loading.value = true;
  error.value = '';

  try {
    const result = await updatePassword(currentPassword.value, password.value);
    if (result.success) {
      alert('비밀번호가 성공적으로 변경되었습니다!');
      router.push('/userpage');
    } else {
      error.value =
        '비밀번호 변경에 실패했습니다: ' +
        (result.message || '알 수 없는 오류');
    }
  } catch (error) {
    error.value = '비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.';
    console.error('비밀번호 변경 실패:', error);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.title {
  background-color: #fff;
  font-size: 28px;
  font-weight: bold;
  margin: 0 auto 32px auto;
  text-align: center;
  width: 100%;
}
.pw-form {
  background-color: #fff;
  width: 100%;
}
.form-group {
  background-color: #fff;
  margin-bottom: 32px;
}
.label {
  background-color: #fff;
  display: block;
  color: #888;
  font-size: 20px;
  margin-bottom: 12px;
  margin-left: 8px;
}
.input {
  width: 100%;
  border: none;
  border-radius: 24px;
  background: #f6f6f6;
  box-shadow: 0 2px 8px 0 #e5e7eb inset;
  font-size: 20px;
  padding: 12px 24px;
  outline: none;
  margin: 0 8px 0 0;
}
.error-msg {
  color: #e74c3c;
  font-size: 16px;
  margin-top: 8px;
  margin-left: 8px;
}
.btn {
  width: 180px;
  height: 48px;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
}
.cancel-btn {
  background: #fafbfc;
  color: #222;
  border: 1.5px solid #e5e7eb;
}
.submit-btn {
  background: #2573ee;
  color: #fff;
  border: none;
}
.password-rules {
  margin-top: 6px;
  font-size: 13px;
  padding-left: 16px;
  list-style: disc;
}
</style>
