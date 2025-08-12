<template>
  <UserCardLayout>
    <h2 class="title">자산정보 수정</h2>
    <form class="asset-form mt-5" @submit.prevent="onSubmit">
      <div class="form-group">
        <label class="label">월급(수입)</label>
        <div class="d-flex w-100">
          <input
            :value="formatNumber(salary)"
            @input="salary = parseNumber($event.target.value)"
            type="text"
            class="input"
            placeholder="월급을 입력하세요."
          />
          <span class="unit">원</span>
        </div>
        <div class="korean-amount">
          {{ numberToKorean(salary) }}
        </div>
      </div>
      <div class="form-group">
        <label class="label">월 지출비</label>
        <div class="d-flex w-100">
          <input
            :value="formatNumber(payAmount)"
            @input="payAmount = parseNumber($event.target.value)"
            type="text"
            class="input"
            placeholder="월 지출비를 입력하세요."
          />
          <span class="unit">원</span>
        </div>
        <div class="korean-amount">{{ numberToKorean(payAmount) }}</div>
      </div>
      <div class="mt-5 text-center">
        <button type="button" class="btn cancel-btn" @click="onCancel">
          취소하기
        </button>
        <button type="submit" class="btn submit-btn ms-4" :disabled="loading">
          수정
        </button>
      </div>
      <div v-if="error" class="error-msg">{{ error }}</div>
    </form>
    <BaseModal
      :visible="modalVisible"
      :message="modalMessage"
      :buttons="modalButtons"
    />
  </UserCardLayout>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserInfo, updateUserProfile, createTestUser } from '@/api/userApi';
import UserCardLayout from '@/components/UserCardLayout.vue';
import BaseModal from '@/components/BaseModal.vue';
const salary = ref(0);
const payAmount = ref(0);
const loading = ref(false);
const error = ref(null);
const router = useRouter();
const modalVisible = ref(false);
const modalMessage = ref('');
const modalButtons = ref([]);
async function loadUserAsset() {
  error.value = null;
  try {
    const user = await getUserInfo();
    salary.value = user.salary || 0;
    payAmount.value = user.payAmount || 0;
  } catch (e) {
    error.value = '사용자 정보를 불러올 수 없습니다.';
    try {
      await createTestUser();
      const user = await getUserInfo();
      salary.value = user.salary || 0;
      payAmount.value = user.payAmount || 0;
      error.value = null;
    } catch (testError) {
      error.value = '테스트용 사용자 생성에도 실패했습니다.';
    }
  }
}
function formatNumber(value) {
  if (value === null || value === undefined) return '';
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
function parseNumber(value) {
  const cleaned = value.replace(/[^0-9]/g, '');
  return cleaned ? parseInt(cleaned, 10) : 0;
}
onMounted(() => {
  loadUserAsset();
});
async function onSubmit() {
  if (
    salary.value === null ||
    payAmount.value === null ||
    isNaN(salary.value) ||
    isNaN(payAmount.value)
  ) {
    error.value = '모든 필드를 숫자로 정확히 입력해주세요.';
    return;
  }
  loading.value = true;
  error.value = null;
  try {
    const result = await updateUserProfile({
      salary: salary.value,
      payAmount: payAmount.value,
    });
    if (result.success) {
      // 성공 시 모달 띄우기
      modalMessage.value = '자산 정보가 성공적으로 수정되었습니다.';
      modalButtons.value = [
        {
          text: '확인',
          onClick: () => {
            modalVisible.value = false;
            router.push('/user');
          },
        },
      ];
      modalVisible.value = true;
    } else {
      error.value =
        '자산 정보 수정에 실패했습니다: ' +
        (result.message || '알 수 없는 오류');
    }
  } catch (e) {
    error.value = '자산 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
}
function onCancel() {
  router.back();
}
function numberToKorean(num) {
  const units = ['', '만', '억', '조'];
  const result = [];
  let strNum = String(num);
  let i = 0;
  while (strNum.length > 0) {
    const chunk = strNum.length >= 4 ? strNum.slice(-4) : strNum;
    strNum = strNum.slice(0, -4);
    if (Number(chunk) !== 0) {
      result.unshift(`${Number(chunk)}${units[i]}`);
    }
    i++;
  }
  return result.length > 0 ? result.join(' ') + ' 원' : ' 원';
}
</script>
<style scoped>
.title {
  background-color: #fff;
  font-size: 28px;
  font-weight: bold;
  margin: 0 auto 2px auto;
  text-align: center;
  width: 100%;
}
.asset-form {
  background-color: #fff;
  width: 100%;
  padding: 0 16px;
}
.form-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 24px;
}
.label {
  color: #888;
  font-size: 16px;
  margin-bottom: 8px;
  width: 100%;
  text-align: left;
}
.input {
  width: 95%;
  max-width: 100%;
  border: none;
  border-radius: 24px;
  background: #f6f6f6;
  box-shadow: 0 2px 8px 0 #e5e7eb inset;
  font-size: 16px;
  padding: 12px 16px;
  outline: none;
  box-sizing: border-box;
}
.unit {
  padding: 0 0 15px 5px;
  font-size: 16px;
  color: #666;
  align-self: flex-end;
}
/* 버튼 영역 */
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
}
.submit-btn:disabled {
  background: #b3d0fa;
  cursor: not-allowed;
}
.error-msg {
  font-size: 14px;
  margin-top: 8px;
  color: #e74c3c;
}
input[type='number']::-webkit-outer-spin-button,
input[type='number']::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type='number'] {
  -moz-appearance: textfield;
}
.korean-amount {
  margin-top: 4px;
  padding-left: 10px;
  color: #777;
  font-size: 14px;
}
</style>
