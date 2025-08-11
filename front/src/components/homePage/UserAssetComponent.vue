<template>
  <div class="asset-card">
    <div class="title-header">
      <h2 class="title">✨ {{ userName }}의 현재 자산 상황 ✨</h2>
    </div>
    <div class="asset-info">
      <div class="asset-item total-asset">
        <span class="label">총 자산</span>
        <span class="amount" :class="{ 'amount-update': isUpdating }">{{
          isUpdating ? '업데이트 중...' : formatCurrency(totalAsset)
        }}</span>
      </div>
      <div class="asset-item monthly-income">
        <span class="label">월 순수익</span>
        <span class="amount" :class="{ 'amount-update': isUpdating }">{{
          isUpdating ? '업데이트 중...' : formatCurrency(monthlyIncome)
        }}</span>
      </div>
    </div>
    <div class="progress-section">
      <div class="progress-info">
        <span class="progress-label">전체 목표 진행률</span>
        <span class="progress-value" :class="{ 'amount-update': isUpdating }">{{
          isUpdating ? '계산 중...' : averageGoalRate + '%'
        }}</span>
      </div>
      <div class="progress-bar">
        <div
          class="progress-fill"
          :style="{ width: (isUpdating ? 0 : currentGoalRate) + '%' }"
          :class="{ 'progress-update': isUpdating }"
        ></div>
      </div>
    </div>
    <button
      class="action-button"
      @click="handleAssetLookup"
      :disabled="isUpdating"
    >
      {{ isUpdating ? '업데이트 중...' : '현재 자산 조회 하기' }}
    </button>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import assetApi from '@/api/assetApi'
// Props로 userId 받기 (부모 컴포넌트에서 전달)
const props = defineProps({
  userId: {
    type: String,
    required: true,
  },
})
// Emits 정의
const emit = defineEmits(['asset-lookup'])
// 반응형 데이터
const userName = ref('사용자')
const totalAsset = ref(0)
const monthlyIncome = ref(0)
const averageGoalRate = ref(0)
const currentGoalRate = ref(0)
const isUpdating = ref(false)
const error = ref(null)
// yeomsky95 연동 관련 데이터
const syncing = ref(false)
const showSyncModal = ref(false)
const syncMessage = ref('')
const syncData = ref(null)
// Computed 속성
const formattedAverageGoalRate = computed(() => {
  if (averageGoalRate.value === null || averageGoalRate.value === undefined) {
    return '0.0'
  }
  return averageGoalRate.value.toFixed(1)
})
// 메서드들
const fetchUserAssetData = async () => {
  isUpdating.value = true
  error.value = null
  try {
    //const data = await assetApi.getUserAssetSummary(props.userId);
    const [data] = await Promise.all([
      assetApi.getUserAssetSummary(props.userId),
      new Promise(resolve => setTimeout(resolve, 700)),
    ])
    // 데이터 할당
    userName.value = data.name
    totalAsset.value = data.totalAssets
    monthlyIncome.value = data.monthlyNetIncome
    averageGoalRate.value = data.averageGoalRate
    // currentGoalRate를 0으로 초기화하여 애니메이션 시작 준비
    currentGoalRate.value = 0
    // 100ms 뒤에 실제 값으로 변경 (transition이 적용되도록)
    setTimeout(() => {
      currentGoalRate.value = averageGoalRate.value
    }, 100)
  } catch (err) {
    console.error('Failed to fetch user asset data:', err)
    error.value = '자산 정보를 불러오는 데 실패했습니다.'
    // 에러 발생 시 기본값 설정
    userName.value = '데이터 없음'
    totalAsset.value = 0
    monthlyIncome.value = 0
    averageGoalRate.value = 0
  } finally {
    isUpdating.value = false
  }
}
const formatCurrency = amount => {
  const value = amount === null || amount === undefined ? 0 : amount
  return (
    new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
    })
      .format(value)
      .replace('₩', '') + ' 원'
  )
}
const handleAssetLookup = () => {
  if (isUpdating.value) return
  emit('asset-lookup')
  fetchUserAssetData()
}
// yeomsky95 자산 연동 메서드
const handleYeomsky95Sync = async () => {
  syncing.value = true
  syncMessage.value = ''
  syncData.value = null
  try {
    const result = await assetApi.getYeomsky95Assets()
    syncData.value = result
    syncMessage.value =
      'yeomsky95 사용자의 자산 정보가 성공적으로 연동되었습니다.'
    showSyncModal.value = true
  } catch (err) {
    console.error('Failed to sync yeomsky95 assets:', err)
    syncMessage.value =
      '자산 연동에 실패했습니다: ' + (err.message || '알 수 없는 오류')
    showSyncModal.value = true
  } finally {
    syncing.value = false
  }
}
const closeSyncModal = () => {
  showSyncModal.value = false
  syncMessage.value = ''
  syncData.value = null
}
// 컴포넌트 마운트 시 실행
onMounted(() => {
  fetchUserAssetData()
})
</script>
<style scoped>
.asset-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  height: 100%;
  max-width: none;
  margin: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}
.amount-update {
  animation: pulse-update 0.5s ease-in-out;
}
@keyframes pulse-update {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}
.progress-update {
  animation: glow-update 1s ease-in-out;
}
@keyframes glow-update {
  0% {
    box-shadow: 0 0 0px 0px rgba(34, 197, 94, 0.5);
  }
  50% {
    box-shadow: 0 0 8px 4px rgba(34, 197, 94, 0.5);
  }
  100% {
    box-shadow: 0 0 0px 0px rgba(34, 197, 94, 0.5);
  }
}
.title-header {
  text-align: center;
  margin-bottom: 20px;
}
.title {
  font-size: 24px;
  font-weight: 600;
  background-color: transparent;
  color: #333;
  text-align: center;
  margin-top: 10px;
  line-height: 1.4;
}
.asset-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}
.asset-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 20px;
  margin: 20px 0;
  border-radius: 12px;
  font-weight: 600;
}
.total-asset {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  color: white;
}
.monthly-income {
  background: linear-gradient(135deg, #2563eb 0%, #60a5fa 100%);
  color: white;
}
.label {
  font-size: 18px;
  font-weight: 500;
  background-color: transparent;
  white-space: nowrap;
}
.amount {
  font-size: 22px;
  font-weight: 700;
  background-color: transparent;
  white-space: nowrap;
}
.progress-section {
  margin-top: 12px;
  margin-bottom: 24px;
  border: 1px solid #cfcfd0;
  border-radius: 12px;
  padding: 18px 20px;
}
.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.progress-label {
  font-size: 18px;
  color: #666;
  font-weight: 500;
  white-space: nowrap;
}
.progress-value {
  font-size: 22px;
  font-weight: 700;
  color: #22c55e;
  white-space: nowrap;
}
.progress-bar {
  height: 8px;
  background: #e5e7eb;
  border-radius: 4px;
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #22c55e 0%, #16a34a 100%);
  border-radius: 4px;
  transition: width 0.3s ease;
}
.button-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: auto;
}
.action-button {
  width: 100%;
  padding: 16px;
  margin-top: auto;
  background: #fd5757;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 20px;
  font-weight: 600;
  cursor: pointer;
  flex-shrink: 0;
  transition: all 0.3s ease;
}
.action-button:disabled {
  background: #9ca3af;
  cursor: not-allowed;
  transform: none;
}
.action-button:not(:disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.3);
}
.action-button:not(:disabled):active {
  transform: translateY(0);
}
.sync-button {
  width: 100%;
  padding: 16px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 20px;
  font-weight: 600;
  cursor: pointer;
  flex-shrink: 0;
}
.sync-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.3);
}
.sync-button:disabled {
  background: #9ca3af;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}
.sync-button:active:not(:disabled) {
  transform: translateY(0);
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
  padding: 24px;
  border-radius: 12px;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}
.modal-content h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}
.sync-result {
  margin-bottom: 20px;
}
.sync-result p {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 16px;
}
.sync-data {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 12px;
  margin-top: 12px;
  max-height: 300px;
  overflow-y: auto;
}
.sync-data pre {
  margin: 0;
  font-size: 12px;
  color: #495057;
  white-space: pre-wrap;
  word-break: break-all;
}
.modal-close-btn {
  width: 100%;
  padding: 12px;
  background: #6b7280;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}
.modal-close-btn:hover {
  background: #4b5563;
}
@media (max-width: 1024px) {
  .title {
    font-size: 22px;
  }
  .label {
    font-size: 17px;
  }
  .amount {
    font-size: 20px;
  }
  .progress-label {
    font-size: 17px;
  }
  .progress-value {
    font-size: 20px;
  }
  .action-button,
  .sync-button {
    font-size: 18px;
  }
}
@media (max-width: 768px) {
  .asset-card {
    padding: 16px 64px;
  }
  .title {
    font-size: 20px;
  }
  .asset-info {
    gap: 8px;
    margin-bottom: 20px;
  }
  .asset-item {
    padding: 20px 16px;
    margin: 10px 0;
  }
  .label {
    font-size: 15px;
  }
  .amount {
    font-size: 18px;
  }
  .progress-section {
    padding: 14px 16px;
    margin-bottom: 20px;
    margin-top: 0px;
  }
  .progress-label {
    font-size: 15px;
  }
  .progress-value {
    font-size: 18px;
  }
  .action-button,
  .sync-button {
    font-size: 16px;
    padding: 20px;
    margin-bottom: 20px;
  }
}
@media (max-width: 480px) {
  .asset-card {
    padding: 12px;
  }
  .title {
    font-size: 18px;
    margin-bottom: 15px;
  }
  .asset-info {
    gap: 6px;
    margin-bottom: 15px;
  }
  .asset-item {
    padding: 12px 14px;
    margin: 8px 0;
  }
  .label {
    font-size: 13px;
  }
  .amount {
    font-size: 16px;
  }
  .progress-section {
    padding: 12px 14px;
    margin-top: 6px;
    margin-bottom: 15px;
  }
  .progress-label {
    font-size: 13px;
  }
  .progress-value {
    font-size: 16px;
  }
  .action-button,
  .sync-button {
    font-size: 14px;
    padding: 10px;
  }
}
</style>
