<template>
  <div class="point-history">
    <div class="point-summary">
      <h3>보유 포인트</h3>
      <div class="point-amount">{{ totalPoints.toLocaleString() }} P</div>
    </div>

    <div class="history-section">
      <div class="filter-section">
        <select v-model="selectedFilter" @change="filterHistory">
          <option value="all">전체</option>
          <option value="적립">적립</option>
          <option value="출금">출금</option>
        </select>
      </div>

      <div class="history-list">
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>포인트 내역을 불러오는 중...</p>
        </div>

        <div v-else-if="error" class="error">
          <p>{{ error }}</p>
          <button @click="loadPointHistory" class="retry-btn">다시 시도</button>
        </div>

        <div v-else-if="filteredHistory.length === 0" class="empty">
          <p>포인트 내역이 없습니다.</p>
        </div>

        <div v-else class="history-items">
          <div
            v-for="item in filteredHistory"
            :key="item.pointId"
            class="history-item"
            :class="{ 'deposit': item.type === 1, 'withdraw': item.type === 2 }"
          >
            <div class="item-header">
              <span class="item-type">{{ item.type === 1 ? '적립' : '출금' }}</span>
              <span class="item-date">{{ formatDate(item.createdAt) }}</span>
            </div>
            <div class="item-detail">{{ item.typeDetail }}</div>
            <div class="item-amount">
              <span :class="{ 'positive': item.type === 1, 'negative': item.type === 2 }">
                {{ item.type === 1 ? '+' : '-' }}{{ item.pointAmount.toLocaleString() }} P
              </span>
            </div>
            <div class="item-total">총 {{ item.totalAmount.toLocaleString() }} P</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getMyPoints, getMyPointHistory } from '@/api/userApi';

const totalPoints = ref(0);
const history = ref([]);
const loading = ref(true);
const error = ref(null);
const selectedFilter = ref('all');

const filteredHistory = computed(() => {
  if (selectedFilter.value === 'all') {
    return history.value;
  }
  const typeInt = selectedFilter.value === '적립' ? 1 : 2;
  return history.value.filter(item => item.type === typeInt);
});

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const loadPointHistory = async () => {
  loading.value = true;
  error.value = null;

  try {
    const [pointsData, historyData] = await Promise.all([
      getMyPoints(),
      getMyPointHistory()
    ]);

    if (pointsData.success) {
      totalPoints.value = pointsData.totalPoints;
    }

    if (historyData.success) {
      history.value = historyData.history;
    }
  } catch (err) {
    console.error('Failed to load point history:', err);
    error.value = '포인트 내역을 불러올 수 없습니다. 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
};

const filterHistory = () => {
  // 필터링은 computed 속성에서 자동으로 처리됨
};

onMounted(() => {
  loadPointHistory();
});
</script>

<style scoped>
.point-history {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.point-summary {
  text-align: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.point-summary h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 16px;
  font-weight: 500;
}

.point-amount {
  font-size: 32px;
  font-weight: bold;
  color: #189eff;
}

.history-section {
  margin-top: 16px;
}

.filter-section {
  margin-bottom: 16px;
}

.filter-section select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: #fff;
  font-size: 14px;
}

.history-list {
  min-height: 200px;
}

.loading, .error, .empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: #666;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #189eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.retry-btn {
  margin-top: 12px;
  padding: 8px 16px;
  background: #189eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.history-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fafafa;
}

.history-item.deposit {
  border-left: 4px solid #4caf50;
}

.history-item.withdraw {
  border-left: 4px solid #f44336;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-type {
  font-weight: bold;
  color: #333;
}

.item-date {
  font-size: 12px;
  color: #666;
}

.item-detail {
  margin-bottom: 8px;
  color: #555;
  font-size: 14px;
}

.item-amount {
  margin-bottom: 4px;
}

.item-amount .positive {
  color: #4caf50;
  font-weight: bold;
}

.item-amount .negative {
  color: #f44336;
  font-weight: bold;
}

.item-total {
  font-size: 12px;
  color: #666;
}
</style> 