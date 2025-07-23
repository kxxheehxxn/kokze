<template>
  <div class="asset-card">
    <!-- 헤더 -->
    <div class="header">
      <h2 class="title">✨ {{ userName }}의 현재 자산 상황 ✨</h2>
    </div>

    <!-- 자산 정보 -->
    <div class="asset-info">
      <!-- 총 자산 -->
      <div class="asset-item total-asset">
        <span class="label">총 자산</span>
        <span class="amount">{{ formatCurrency(totalAsset) }}</span>
      </div>

      <!-- 월 순수익 -->
      <div class="asset-item monthly-income">
        <span class="label">월 순수익</span>
        <span class="amount">{{ formatCurrency(monthlyIncome) }}</span>
      </div>
    </div>

    <!-- 진행률 -->
    <div class="progress-section">
      <div class="progress-info">
        <span class="progress-label">전체 목표 진행률</span>
        <span class="progress-value">{{ averageGoalRate }}%</span>
      </div>
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: averageGoalRate + '%' }"></div>
      </div>
    </div>

    <!-- 액션 버튼 -->
    <button class="action-button" @click="handleAssetLookup">현재 자산 조회 하기</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import userAssetApi from '@/api/userAssetApi';

// Props 정의 (필요한 경우)
// const props = defineProps({
//   userId: String
// })

// Emits 정의
const emit = defineEmits(['asset-lookup']);

// 반응형 데이터
const userName = ref('사용자');
const totalAsset = ref(0);
const monthlyIncome = ref(0);
const averageGoalRate = ref(0);
const loading = ref(false);
const error = ref(null);

// 현재 사용자 ID (로그인 시스템 구현 후 동적으로 변경)
const currentUserId = ref('550e8400-e29b-41d4-a716-446655440001');

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
    const data = await userAssetApi.getUserAssetSummary(currentUserId.value);
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
  background: #f8f9fa;
  border-radius: 20px;
  padding: 24px;
  max-width: 400px;
  margin: 0 auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 24px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  line-height: 1.4;
}

.asset-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 32px;
}

.asset-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
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
  font-size: 14px;
  font-weight: 500;
  background-color: transparent;
}

.amount {
  font-size: 18px;
  font-weight: 700;
  background-color: transparent;
}

.progress-section {
  margin-bottom: 24px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.progress-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.progress-value {
  font-size: 16px;
  font-weight: 700;
  color: #22c55e;
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
  background: #fd5757;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.action-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.3);
}

.action-button:active {
  transform: translateY(0);
}

/* 반응형 */
@media (max-width: 480px) {
  .asset-card {
    margin: 0 16px;
    padding: 20px;
  }

  .amount {
    font-size: 16px;
  }
}
</style>
