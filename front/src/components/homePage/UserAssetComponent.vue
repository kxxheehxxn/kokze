<template>
  <div class="asset-card">
    <div class="title-header">
      <h2 class="title">✨ {{ userName }}의 현재 자산 상황 ✨</h2>
    </div>

    <div class="asset-info">
      <div class="asset-item total-asset">
        <span class="label">총 자산</span>
        <span class="amount">{{ formatCurrency(totalAsset) }}</span>
      </div>

      <div class="asset-item monthly-income">
        <span class="label">월 순수익</span>
        <span class="amount">{{ formatCurrency(monthlyIncome) }}</span>
      </div>
    </div>

    <div class="progress-section">
      <div class="progress-info">
        <span class="progress-label">전체 목표 진행률</span>
        <span class="progress-value">{{ averageGoalRate }}%</span>
      </div>
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: averageGoalRate + '%' }"></div>
      </div>
    </div>

    <button class="action-button" @click="handleAssetLookup">현재 자산 조회 하기</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import assetApi from '@/api/assetApi';

// Props로 userId 받기 (부모 컴포넌트에서 전달)
const props = defineProps({
  userId: {
    type: String,
    required: true,
  },
});

// Emits 정의
const emit = defineEmits(['asset-lookup']);

// 반응형 데이터
const userName = ref('사용자');
const totalAsset = ref(0);
const monthlyIncome = ref(0);
const averageGoalRate = ref(0);
const loading = ref(false);
const error = ref(null);

// Computed 속성
const formattedAverageGoalRate = computed(() => {
  if (averageGoalRate.value === null || averageGoalRate.value === undefined) {
    return '0.0';
  }
  return averageGoalRate.value.toFixed(1);
});

// 메서드들
const fetchUserAssetData = async () => {
  loading.value = true;
  error.value = null;

  try {
    const data = await assetApi.getUserAssetSummary(props.userId);
    console.log('Fetched User Asset Data:', data);

    // 데이터 할당
    userName.value = data.name;
    totalAsset.value = data.totalAssets;
    monthlyIncome.value = data.monthlyNetIncome;
    averageGoalRate.value = data.averageGoalRate;
  } catch (err) {
    console.error('Failed to fetch user asset data:', err);
    error.value = '자산 정보를 불러오는 데 실패했습니다.';

    // 에러 발생 시 기본값 설정
    userName.value = '데이터 없음';
    totalAsset.value = 0;
    monthlyIncome.value = 0;
    averageGoalRate.value = 0;
  } finally {
    loading.value = false;
  }
};

const formatCurrency = (amount) => {
  const value = amount === null || amount === undefined ? 0 : amount;
  return (
    new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
    })
      .format(value)
      .replace('₩', '') + ' 원'
  );
};

const handleAssetLookup = () => {
  emit('asset-lookup');
  console.log('자산 조회 버튼 클릭됨');
  fetchUserAssetData();
};

// 컴포넌트 마운트 시 실행
onMounted(() => {
  fetchUserAssetData();
});
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
  display: flex; /* 내부 콘텐츠를 세로로 정렬 */
  flex-direction: column;
  justify-content: space-between; /* 요소들 사이 공간 균등 분배 */
}

.title-header {
  text-align: center;
  margin-bottom: 20px; /* 마진 조정 */
}

.title {
  font-size: 24px; /* 폰트 크기 조정 */
  font-weight: 600;
  background-color: transparent;
  color: #333;
  text-align: center;
  margin-top: 10px; /* 마진 조정 */
  line-height: 1.4;
}

.asset-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px; /* 마진 조정 */
}

.asset-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 20px; /* 패딩 조정 */
  margin: 20px 0; /* 상하 마진 조정 */
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
  font-size: 18px; /* 폰트 크기 조정 */
  font-weight: 500;
  background-color: transparent;
  white-space: nowrap; /* 텍스트 줄바꿈 방지 */
}

.amount {
  font-size: 22px; /* 폰트 크기 조정 */
  font-weight: 700;
  background-color: transparent;
  white-space: nowrap; /* 텍스트 줄바꿈 방지 */
}

.progress-section {
  margin-top: 12px;
  margin-bottom: 24px; /* 마진 조정 */
  border: 1px solid #cfcfd0;
  border-radius: 12px;
  padding: 18px 20px; /* 패딩 조정 */
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.progress-label {
  font-size: 18px; /* 폰트 크기 조정 */
  color: #666;
  font-weight: 500;
  white-space: nowrap; /* 텍스트 줄바꿈 방지 */
}

.progress-value {
  font-size: 22px; /* 폰트 크기 조정 */
  font-weight: 700;
  color: #22c55e;
  white-space: nowrap; /* 텍스트 줄바꿈 방지 */
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

.action-button {
  width: 100%;
  padding: 16px;
  margin-top: auto; /* 하단으로 자동 배치 */
  background: #fd5757;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 20px; /* 폰트 크기 조정 */
  font-weight: 600;
  cursor: pointer;
  flex-shrink: 0; /* 버튼이 줄어들지 않도록 */
}

.action-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.3);
}

.action-button:active {
  transform: translateY(0);
}

@media (max-width: 1023px) {
  .title {
    font-size: 22px; /* 폰트 크기 조정 */
  }
  .label {
    font-size: 17px; /* 폰트 크기 조정 */
  }
  .amount {
    font-size: 20px; /* 폰트 크기 조정 */
  }
  .progress-label {
    font-size: 17px; /* 폰트 크기 조정 */
  }
  .progress-value {
    font-size: 20px; /* 폰트 크기 조정 */
  }
  .action-button {
    font-size: 18px; /* 폰트 크기 조정 */
  }
}

/* 반응형 디자인 추가 (필요시 더 세분화) */
@media (max-width: 768px) {
  .asset-card {
    padding: 20px;
  }
  .title {
    font-size: 22px;
  }
  .label,
  .progress-label {
    font-size: 16px;
  }
  .amount,
  .progress-value {
    font-size: 20px;
  }
  .action-button {
    font-size: 18px;
    padding: 14px;
  }
}

/* 768px 미만 화면 (태블릿 세로, 모바일 가로) */
@media (max-width: 768px) {
  .asset-card {
    padding: 16px;
  }
  .title {
    font-size: 20px;
  }
  .asset-info {
    gap: 8px;
    margin-bottom: 20px;
  }
  .asset-item {
    padding: 14px 16px;
    margin: 10px 0;
  }
  .label {
    font-size: 15px; /* 폰트 크기 더 줄임 */
  }
  .amount {
    font-size: 18px; /* 폰트 크기 더 줄임 */
  }
  .progress-section {
    padding: 14px 16px;
    margin-top: 8px;
    margin-bottom: 20px;
  }
  .progress-label {
    font-size: 15px; /* 폰트 크기 더 줄임 */
  }
  .progress-value {
    font-size: 18px; /* 폰트 크기 더 줄임 */
  }
  .action-button {
    font-size: 16px;
    padding: 12px;
  }
}

/* 480px 미만 화면 (모바일 세로) */
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
    font-size: 13px; /* 폰트 크기 더 줄임 */
  }
  .amount {
    font-size: 16px; /* 폰트 크기 더 줄임 */
  }
  .progress-section {
    padding: 12px 14px;
    margin-top: 6px;
    margin-bottom: 15px;
  }
  .progress-label {
    font-size: 13px; /* 폰트 크기 더 줄임 */
  }
  .progress-value {
    font-size: 16px; /* 폰트 크기 더 줄임 */
  }
  .action-button {
    font-size: 14px;
    padding: 10px;
  }
}
</style>
