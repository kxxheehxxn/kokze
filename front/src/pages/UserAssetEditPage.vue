<template>
  <UserCardLayout>
    <h2 class="title">자산정보 수정</h2>
    <form class="asset-form" @submit.prevent="onSubmit">
      <div class="form-group">
        <label class="label-group">수입</label>
        <div class="input-row">
          <span class="input-label">월급</span>
          <input
            v-model="income"
            type="number"
            class="input"
            placeholder="0"
            min="0"
            max="999999999"
            step="1000"
          />
          <span class="unit">원</span>
        </div>
      </div>
      <div class="form-group">
        <label class="label-group">지출</label>
        <div class="input-row">
          <span class="input-label">월 지출비</span>
          <input
            v-model="expense"
            type="number"
            class="input"
            placeholder="0"
          />
          <span class="unit">원</span>
        </div>
      </div>
      <div class="button-row">
        <button type="button" class="cancel-btn" @click="onCancel">취소</button>
        <button type="submit" class="submit-btn">수정</button>
      </div>
    </form>
  </UserCardLayout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import UserCardLayout from '@/components/UserCardLayout.vue'

const income = ref('')
const expense = ref('')
const router = useRouter()

function onCancel() {
  router.back()
}
function onSubmit() {
  if (income.value < 0 || expense.value < 0) {
    alert('음수 값은 입력할 수 없습니다.')
    return
  }
  if (income.value > 999999999 || expense.value > 999999999) {
    alert('입력 가능한 최대값을 초과했습니다.')
    return
  }
  // 실제 저장 로직은 추후 API 연동
  alert('자산정보가 수정되었습니다!')
  router.back()
}
</script>

<style scoped>
.title {
  background-color: #fff;
  font-size: 32px;
  font-weight: bold;
  margin: 0 auto 48px auto;
  text-align: center;
  width: 100%;
}
.asset-form {
  background-color: #fff;
  width: 100%;
}
.form-group {
  background-color: #fff;
  margin-bottom: 48px;
}
.label-group {
  background-color: #fff;
  display: block;
  color: #888;
  font-size: 20px;
  margin-bottom: 12px;
  margin-left: 8px;
}
.input-row {
  background-color: #fff;
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 8px;
}
.input-label {
  background-color: #fff;
  color: #888;
  font-size: 18px;
  min-width: 90px;
  text-align: right;
}
.input {
  flex: 1;
  border: none;
  border-radius: 24px;
  background: #f6f6f6;
  box-shadow: 0 2px 8px 0 #e5e7eb inset;
  font-size: 20px;
  padding: 12px 24px;
  outline: none;
  margin: 0 8px;
}
.unit {
  background-color: #fff;
  color: #888;
  font-size: 18px;
  min-width: 32px;
}
.button-row {
  background-color: #fff;
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 64px;
  width: 100%;
}
.cancel-btn,
.submit-btn {
  width: 220px;
  height: 56px;
  border-radius: 24px;
  font-size: 20px;
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
/* Chrome, Safari, Edge, Opera */
input[type='number']::-webkit-outer-spin-button,
input[type='number']::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
/* Firefox */
input[type='number'] {
  -moz-appearance: textfield;
}
</style>
