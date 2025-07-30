<template>
  <UserCardLayout>
    <h2 class="title">자산정보 수정</h2>
    <form class="asset-form" @submit.prevent="onSubmit">
      <div class="form-group">
        <label class="label">월급(수입)</label>
        <input
          v-model.number="salary"
          type="number"
          min="0"
          class="input"
          placeholder="월급을 입력하세요."
        />
        <span class="unit">원</span>
      </div>
      <div class="form-group">
        <label class="label">월 지출비</label>
        <input
          v-model.number="payAmount"
          type="number"
          min="0"
          class="input"
          placeholder="월 지출비를 입력하세요."
        />
        <span class="unit">원</span>
      </div>
      <div class="button-row">
        <button type="button" class="cancel-btn" @click="onCancel">취소</button>
        <button type="submit" class="submit-btn" :disabled="loading">수정</button>
      </div>
      <div v-if="error" class="error-msg">{{ error }}</div>
      <div v-if="success" class="success-msg">자산 정보가 성공적으로 수정되었습니다.</div>
    </form>
  </UserCardLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserInfo, updateUserProfile, createTestUser } from '@/api/userApi';
import UserCardLayout from '@/components/UserCardLayout.vue';

const salary = ref(0);
const payAmount = ref(0);
const loading = ref(false);
const error = ref(null);
const success = ref(false);
const router = useRouter();

async function loadUserAsset() {
  error.value = null;
  try {
    const user = await getUserInfo();
    salary.value = user.salary || 0;
    payAmount.value = user.payAmount || 0;
  } catch (e) {
    error.value = '사용자 정보를 불러올 수 없습니다.';
    console.log('사용자 정보 로드 실패:', e);
    
    // 개발 중에만 테스트용 사용자 생성 시도
    try {
      console.log('🔄 테스트용 사용자 생성 시도...');
      await createTestUser();
      console.log('✅ 테스트용 사용자 생성 완료');
      
      // 다시 사용자 정보 로드 시도
      const user = await getUserInfo();
      salary.value = user.salary || 0;
      payAmount.value = user.payAmount || 0;
      error.value = null;
    } catch (testError) {
      console.log('❌ 테스트용 사용자 생성 실패:', testError);
      error.value = '테스트용 사용자 생성에도 실패했습니다.';
    }
  }
}

onMounted(() => {
  loadUserAsset();
});

async function onSubmit() {
  if (salary.value === null || payAmount.value === null) {
    error.value = '모든 필드를 입력해주세요.';
    return;
  }
  
  loading.value = true;
  error.value = null;
  success.value = false;
  try {
    const result = await updateUserProfile({ salary: salary.value, payAmount: payAmount.value });
    if (result.success) {
      success.value = true;
      setTimeout(() => {
        router.push('/userpage');
      }, 1200);
    } else {
      error.value = '자산 정보 수정에 실패했습니다: ' + (result.message || '알 수 없는 오류');
    }
  } catch (e) {
    error.value = '자산 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.';
    console.error('자산 정보 수정 실패:', e);
  } finally {
    loading.value = false;
  }
}

function onCancel() {
  router.back();
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
.asset-form {
  background-color: #fff;
  width: 100%;
}
.form-group {
  background-color: #fff;
  margin-bottom: 32px;
  display: flex;
  align-items: center;
}
.label {
  background-color: #fff;
  display: block;
  color: #888;
  font-size: 20px;
  margin-bottom: 0;
  margin-left: 8px;
  width: 120px;
  text-align: left;
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
  margin: 0 8px 0 0;
  min-width: 0;
}
.unit {
  color: #888;
  font-size: 18px;
  margin-left: 4px;
}
.button-row {
  background-color: #fff;
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 32px;
  width: 100%;
}
.cancel-btn,
.submit-btn {
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
.submit-btn:disabled {
  background: #b3d0fa;
  cursor: not-allowed;
}
.error-msg {
  color: #e74c3c;
  font-size: 16px;
  margin-top: 8px;
  margin-left: 8px;
}
.success-msg {
  color: #2573ee;
  font-size: 16px;
  margin-top: 8px;
  margin-left: 8px;
}

/* Chrome, Safari, Edge, Opera - number input spin button 제거 */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox - number input spin button 제거 */
input[type="number"] {
  -moz-appearance: textfield;
}

</style>
