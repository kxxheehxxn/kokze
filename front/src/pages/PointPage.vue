<template>
  <div class="point-page">
    <div class="point-header mt-4">
      <h1>포인트 관리</h1>
      <div class="point-summary">
        <div class="current-points">
          <span class="label">보유 포인트</span>
          <span class="amount">{{ totalPoints.toLocaleString() }} P</span>
        </div>
        <button
          class="withdraw-btn"
          @click="showWithdrawModal = true"
          :disabled="totalPoints < 10000"
        >
          출금
        </button>
      </div>
    </div>
    <div class="point-content">
      <PointHistoryComponent />
    </div>
    <!-- 출금 모달 -->
    <div
      v-if="showWithdrawModal"
      class="modal-overlay"
      @click.self="showWithdrawModal = false"
    >
      <div class="modal-content">
        <h3>포인트 출금</h3>
        <div class="withdraw-form">
          <div class="form-group">
            <label>출금 금액</label>
            <input
              v-model.number="withdrawAmount"
              type="number"
              min="10000"
              step="1000"
              placeholder="최소 10,000원"
            />
            <small>최소 출금 금액: 10,000원</small>
          </div>
          <div class="form-group">
            <label>출금 사유</label>
            <input
              v-model="withdrawReason"
              type="text"
              placeholder="출금 사유를 입력하세요"
            />
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn cancel" @click="showWithdrawModal = false">
            취소
          </button>
          <button
            class="btn confirm"
            @click="handleWithdraw"
            :disabled="!canWithdraw"
          >
            출금
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue';
import { getMyPoints, withdrawPoints } from '@/api/userApi';
import PointHistoryComponent from '@/components/PointHistoryComponent.vue';
const totalPoints = ref(0);
const showWithdrawModal = ref(false);
const withdrawAmount = ref(10000);
const withdrawReason = ref('');
const canWithdraw = computed(() => {
  return (
    withdrawAmount.value >= 10000 &&
    withdrawAmount.value <= totalPoints.value &&
    withdrawReason.value.trim() !== ''
  );
});
const loadTotalPoints = async () => {
  try {
    const data = await getMyPoints();
    if (data.success) {
      totalPoints.value = data.totalPoints;
    }
  } catch (err) {
    console.error('Failed to load total points:', err);
  }
};
const handleWithdraw = async () => {
  if (!canWithdraw.value) {
    alert('출금 조건을 확인해주세요.');
    return;
  }
  try {
    const response = await withdrawPoints(
      withdrawAmount.value,
      withdrawReason.value
    );
    if (response.success) {
      alert('포인트 출금이 완료되었습니다.');
      showWithdrawModal.value = false;
      withdrawAmount.value = 10000;
      withdrawReason.value = '';
      await loadTotalPoints();
    } else {
      alert(response.message || '출금 처리 중 오류가 발생했습니다.');
    }
  } catch (err) {
    console.error('Withdraw failed:', err);
    alert('출금 처리 중 오류가 발생했습니다.');
  }
};
onMounted(() => {
  loadTotalPoints();
});
</script>
<style scoped>
.point-page {
  min-height: 100vh;
  background: #f6f6f6;
  padding: 20px;
}
.point-header {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.point-header h1 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}
.point-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.current-points {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.current-points .label {
  font-size: 14px;
  color: #666;
}
.current-points .amount {
  font-size: 32px;
  font-weight: bold;
  color: #3573ee;
}
.withdraw-btn {
  padding: 12px 24px;
  background: #3573ee;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.withdraw-btn:hover:not(:disabled) {
  background: #147acc;
}
.withdraw-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.point-content {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  width: 400px;
  max-width: 90vw;
}
.modal-content h3 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
  font-weight: bold;
}
.withdraw-form {
  margin-bottom: 24px;
}
.form-group {
  margin-bottom: 16px;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #333;
}
.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}
.form-group small {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: #666;
}
.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.btn.cancel {
  background: #eee;
  color: #333;
}
.btn.cancel:hover {
  background: #ddd;
}
.btn.confirm {
  background: #3573ee;
  color: white;
}
.btn.confirm:hover:not(:disabled) {
  background: #147acc;
}
.btn.confirm:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
