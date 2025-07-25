<template>
  <UserCardLayout>
    <h2 class="title">비밀번호 찾기</h2>
    <form v-if="step === 1" class="pw-form" @submit.prevent="onNext">
      <div class="form-group">
        <label>이름</label>
        <input v-model="name" class="input" placeholder="이름을 입력하세요" />
      </div>
      <div class="form-group">
        <label>이메일</label>
        <div class="input-row">
          <input v-model="email" class="input" placeholder="이메일을 입력하세요" />
          <button type="button" class="btn" @click="onEmailCheck">인증</button>
        </div>
        <div v-if="emailChecked === false" class="error-msg">존재하지 않는 계정입니다</div>
      </div>
      <div class="form-group">
        <label>인증번호</label>
        <div class="input-row">
          <input v-model="code" class="input" placeholder="인증번호를 입력하세요" />
          <button type="button" class="btn" @click="onCodeCheck">확인</button>
        </div>
        <div v-if="codeChecked === true" class="success-msg">이메일 인증 완료</div>
        <div v-if="codeChecked === false" class="error-msg">인증번호가 일치하지 않습니다</div>
      </div>
      <div class="button-row">
        <button type="button" class="cancel-btn" @click="onCancel">취소하기</button>
        <button type="submit" class="submit-btn" :disabled="!canNext">다음 단계</button>
      </div>
    </form>
    <form v-else class="pw-form" @submit.prevent="onChangePassword">
      <div class="form-group">
        <label>새 비밀번호</label>
        <input v-model="password" type="password" class="input" placeholder="비밀번호를 입력하세요." />
      </div>
      <div class="form-group">
        <label>비밀번호 확인</label>
        <input v-model="passwordCheck" type="password" class="input" placeholder="비밀번호를 입력하세요." />
        <div v-if="password && passwordCheck && password !== passwordCheck" class="error-msg">
          비밀번호가 일치하지 않습니다
        </div>
      </div>
      <div class="button-row">
        <button type="button" class="cancel-btn" @click="onCancel">취소하기</button>
        <button type="submit" class="submit-btn" :disabled="!canChange">수정</button>
      </div>
    </form>
  </UserCardLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import UserCardLayout from '@/components/UserCardLayout.vue'
import {
  checkEmailExists,
  sendVerificationCode,
  verifyCode,
  changePassword,
} from '@/api/passwordApi.js'

const step = ref(1)
const name = ref('')
const email = ref('')
const code = ref('')
const password = ref('')
const passwordCheck = ref('')
const emailChecked = ref(null)
const codeChecked = ref(null)
const router = useRouter()

async function onEmailCheck() {
  emailChecked.value = await checkEmailExists(email.value)
  if (emailChecked.value) await sendVerificationCode(email.value)
}
async function onCodeCheck() {
  codeChecked.value = await verifyCode(code.value)
}
const canNext = computed(
  () => name.value && emailChecked.value && codeChecked.value,
)
function onNext() {
  if (canNext.value) step.value = 2
}
const canChange = computed(
  () => password.value && password.value === passwordCheck.value,
)
async function onChangePassword() {
  if (!canChange.value) return
  await changePassword(email.value, password.value)
  alert('비밀번호가 변경되었습니다!')
  router.push('/auth/login')
}
function onCancel() {
  router.back()
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
.form-group label {
  background-color: #fff;
}
.input-row {
  background-color: #fff;
  display: flex;
  align-items: center;
}
.input-row .input {
  flex: 1;
}
.input-row .btn {
  margin-left: 12px;
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
.btn {
  background: #2573ee;
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 8px 22px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  transition: background 0.2s;
}
.error-msg { 
  background-color: #fff;
  color: #e74c3c; font-size: 15px; margin-top: 6px; }
.success-msg { color: #2573ee; font-size: 15px; margin-top: 6px; }
.button-row {
  background-color: #fff;
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 32px;
  width: 100%;
}
.cancel-btn, .submit-btn {
  width: 180px;
  height: 48px;
  border-radius: 18px;
  font-size: 18px;
  font-weight: 500;
  border: none;
  cursor: pointer;
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
</style>
