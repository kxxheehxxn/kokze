<template>
  <UserCardLayout>
    <h2 class="title">비밀번호 수정</h2>
    <form class="pw-form" @submit.prevent="onSubmit">
      <div class="form-group">
        <label class="label">새 비밀번호</label>
        <input
          v-model="password"
          type="password"
          class="input"
          placeholder="비밀번호를 입력하세요."
        />
      </div>
      <div class="form-group">
        <label class="label">비밀번호 확인</label>
        <input
          v-model="passwordCheck"
          type="password"
          class="input"
          placeholder="비밀번호를 입력하세요."
        />
        <div v-if="passwordCheck && password !== passwordCheck" class="error-msg">
          비밀번호가 일치하지 않습니다!
        </div>
      </div>
      <div class="button-row">
        <button type="button" class="cancel-btn" @click="onCancel">취소하기</button>
        <button type="submit" class="submit-btn" :disabled="!canSubmit">수정</button>
      </div>
    </form>
  </UserCardLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import UserCardLayout from '@/components/UserCardLayout.vue'

const password = ref('')
const passwordCheck = ref('')
const router = useRouter()

const canSubmit = computed(() => password.value && password.value === passwordCheck.value)

function onCancel() {
  router.back()
}
function onSubmit() {
  if (!canSubmit.value) return
  // 실제 비밀번호 변경 API 연동 필요
  alert('비밀번호가 변경되었습니다!')
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