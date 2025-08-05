<template>
  <div class="user-asset-chart">
    <div class="loading-state" v-if="loading">
      <p class="loading-message">자산 정보를 불러오는 중...</p>
    </div>

    <template v-else-if="hasAssetData">
      <div class="chart-container">
        <div class="chart-wrapper">
          <Doughnut ref="doughnutChartRef" :data="chartData" :options="chartOptions" :plugins="chartPlugins" />
        </div>
      </div>
      <div class="legend-container">
        <div
          class="legend-item"
          v-for="(item, index) in assetData"
          :key="index"
          :class="{ 'legend-hover': hoveredIndex === index }"
          @mouseover="hoveredIndex = index"
          @mouseleave="hoveredIndex = null"
        >
          <div class="legend-indicator">
            <div class="color-dot" :style="{ backgroundColor: item.color }"></div>
          </div>
          <div class="legend-content">
            <div class="asset-info">
              <span class="asset-type">{{ item.type }}</span>
              <span class="asset-percentage">({{ item.percentage.toFixed(1) }}%)</span>
            </div>
            <div class="asset-amount">{{ formatCurrency(item.amount) }}</div>
          </div>
        </div>
      </div>
    </template>

    <div class="empty-state" v-else>
      <p class="empty-message">등록된 자산이 없습니다</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'vue-chartjs';
import assetApi from '@/api/assetApi';

ChartJS.register(ArcElement, Tooltip, Legend);

// Props로 userId 받기 (부모 컴포넌트에서 전달)
const props = defineProps({
  userId: {
    type: String,
    required: true,
  },
});

const hoveredIndex = ref(null);
const loading = ref(false);
const error = ref(null);
const doughnutChartRef = ref(null);

// 계좌 타입별 색상 매핑 (그룹화된 타입 기준)
const accountTypeColors = {
  적금: '#A3E4E0',
  예금: '#87CEEB',
  청약: '#FFB6C1',
  펀드: '#DDA0DD',
  주식: '#F0E68C',
  채권: '#98FB98',
  기타: '#D3D3D3',
};

// API에서 가져온 실제 자산 데이터만 사용
const apiAssetData = ref([]);

// 자산 데이터가 있는지 확인
const hasAssetData = computed(() => {
  return apiAssetData.value.length > 0;
});

const totalAssets = computed(() => {
  if (!hasAssetData.value) return 0;
  return apiAssetData.value.reduce((sum, asset) => sum + asset.amount, 0);
});

const assetData = computed(() => {
  if (!hasAssetData.value) return [];
  return apiAssetData.value.map((item) => ({
    ...item,
    percentage: totalAssets.value > 0 ? (item.amount / totalAssets.value) * 100 : 0,
  }));
});

const chartData = computed(() => {
  if (!hasAssetData.value) {
    // 데이터가 없을 때 회색 원형 차트
    return {
      labels: ['자산 없음'],
      datasets: [
        {
          data: [1], // 임의의 값 1을 사용하여 전체 원을 그림
          backgroundColor: ['#E5E5E5'],
          borderColor: ['#E5E5E5'],
          borderWidth: 2,
        },
      ],
    };
  }

  return {
    labels: assetData.value.map((item) => item.type),
    datasets: [
      {
        data: assetData.value.map((item) => item.amount),
        backgroundColor: assetData.value.map((item) => item.color),
        borderColor: assetData.value.map((item) => item.color),
        borderWidth: 2,
      },
    ],
  };
});

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: true,
  aspectRatio: 1,
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: hasAssetData.value, // 데이터가 없을 때는 툴팁 비활성화
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      titleColor: '#ffffff',
      bodyColor: '#ffffff',
      borderColor: '#ffffff',
      borderWidth: 1,
      cornerRadius: 8,
      displayColors: true,
      callbacks: {
        label: function (context) {
          if (!hasAssetData.value) return '';
          const label = context.label || '';
          const value = formatCurrency(context.parsed);
          const percentage = assetData.value[context.dataIndex].percentage.toFixed(1);
          const accountCount = assetData.value[context.dataIndex].accountCount;
          return [`${label}: ${value} (${percentage}%)`, `계좌 수: ${accountCount}개`];
        },
      },
    },
  },
  elements: {
    arc: {
      borderWidth: 2,
      borderColor: '#ffffff',
    },
  },
  cutout: '60%',
  animation: {
    animateScale: true,
    animateRotate: true,
    duration: 1000,
  },
}));

const chartPlugins = [
  {
    id: 'centerText',
    beforeDraw: function (chart) {
      const { width, height, ctx } = chart;
      ctx.restore();

      const fontSize = Math.min(width, height) / 16;
      ctx.font = `bold ${fontSize}px Arial`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillStyle = hasAssetData.value ? '#333' : '#999'; // 데이터가 없을 때 회색 텍스트

      const centerX = width / 2;
      const centerY = height / 2;

      if (hasAssetData.value) {
        ctx.fillText('총 자산', centerX, centerY - fontSize / 2);
        ctx.font = `bold ${fontSize * 0.8}px Arial`;
        ctx.fillText(formatCurrency(totalAssets.value).replace(' 원', '원'), centerX, centerY + fontSize / 2);
      } else {
        ctx.fillText('자산 없음', centerX, centerY);
      }

      ctx.save();
    },
  },
];

// 계좌 타입을 그룹화하는 함수
const getGroupedAccountType = (accountType) => {
  if (accountType.includes('적금')) {
    return '적금';
  } else if (accountType.includes('예금')) {
    return '예금';
  } else if (accountType.includes('청약')) {
    return '청약';
  } else if (accountType.includes('펀드')) {
    return '펀드';
  } else if (accountType.includes('주식')) {
    return '주식';
  } else if (accountType.includes('채권')) {
    return '채권';
  } else {
    return accountType; // 기타 타입은 그대로 유지
  }
};

// 백엔드에서 계좌 데이터를 가져와서 accountType별로 그룹화
const fetchUserAssetChart = async () => {
  loading.value = true;
  error.value = null;

  try {
    // 백엔드 API 호출
    const accounts = await assetApi.getUserBankAccounts(props.userId);

    if (accounts && accounts.length > 0) {
      // accountType별로 그룹화하고 합계 계산
      const groupedData = accounts.reduce((acc, account) => {
        const originalType = account.accountType;
        const groupedType = getGroupedAccountType(originalType); // 그룹화된 타입 사용
        const amount = parseInt(account.balance) || 0;

        if (!acc[groupedType]) {
          acc[groupedType] = {
            type: groupedType,
            amount: 0,
            accountCount: 0,
            color: accountTypeColors[groupedType] || '#D3D3D3', // 정의되지 않은 타입은 회색
            originalTypes: new Set(), // 원본 타입들을 추적
          };
        }

        acc[groupedType].amount += amount;
        acc[groupedType].accountCount += 1;
        acc[groupedType].originalTypes.add(originalType); // 원본 타입 추가

        return acc;
      }, {});

      // 객체를 배열로 변환하고 originalTypes는 제거
      apiAssetData.value = Object.values(groupedData).map((item) => {
        const { originalTypes, ...rest } = item;
        return rest;
      });
    } else {
      apiAssetData.value = []; // 빈 배열로 설정
    }
  } catch (err) {
    console.error('계좌 데이터 조회 실패:', err);
    error.value = '자산 차트 정보를 불러오는 데 실패했습니다.';
    apiAssetData.value = []; // 에러 시에도 빈 배열로 설정
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

defineExpose({
  fetchUserAssetChart,
});

onMounted(() => {
  fetchUserAssetChart();
});
</script>

<style scoped>
.user-asset-chart {
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
  align-items: center;
}

.chart-container {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  margin-bottom: 32px;
  width: 100%; /* 부모 너비에 맞춤 */
}

.chart-wrapper {
  position: relative;
  width: 300px;
  height: 300px;
  /* 부모 컨테이너에 맞춰 스케일링 */
  max-width: 100%;
  max-height: 100%;
}

.legend-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* 가로 중앙 정렬 */
  gap: 16px; /* 모든 방향으로 동일한 간격 */
  margin-bottom: 32px;
  margin-top: 48px;
  width: 100%; /* 부모 너비에 맞춤 */
  padding: 0 10px; /* 좌우 패딩을 추가하여 아이템이 가장자리에서 튀어나오지 않도록 함 */
  box-sizing: border-box; /* 패딩을 너비에 포함 */
}

.legend-item {
  display: flex;
  align-items: center;
  padding: 12px 16px; /* 패딩을 약간 줄여서 공간 확보 */
  border-radius: 12px;
  transition: background-color 0.2s ease;
  cursor: pointer;
  box-sizing: border-box;
  flex-basis: calc(50% - 8px); /* 2개씩 배치 (gap 16px의 절반인 8px을 뺌) */
  max-width: calc(50% - 8px);
  min-width: 140px; /* 최소 너비를 약간 줄임 */
  justify-content: flex-start;
}

.legend-item:hover,
.legend-hover {
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.legend-indicator {
  margin-right: 8px;
}

.color-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  box-shadow: none;
  border: 1px solid #eee;
  flex-shrink: 0;
}

.legend-content {
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: baseline;
  gap: 8px; /* 항목 간 간격 조정 */
}

.asset-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.asset-type {
  font-weight: 500;
  color: #333;
  font-size: 16px; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.asset-percentage {
  font-weight: 400;
  color: #666;
  font-size: 14px; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.asset-amount {
  font-weight: 600;
  color: #333;
  font-size: 16px; /* 폰트 크기 조정 */
  text-align: right;
  white-space: nowrap;
}

/* 빈 상태 스타일 */
.empty-state {
  margin-top: 20px;
  margin-bottom: 32px;
}

.empty-message {
  color: #999;
  font-size: 16px;
  text-align: center;
  margin: 0;
}

/* 로딩 상태 스타일 */
.loading-state {
  margin-top: 20px;
  margin-bottom: 32px;
}

.loading-message {
  color: #666;
  font-size: 16px;
  text-align: center;
  margin: 0;
}

/* 1024px 미만 화면에서 .asset-amount 숨기기 */
@media (max-width: 1024px) {
  /* AssetCard.vue의 .asset-item .amount와 유사한 처리 */
  .asset-amount {
    display: none;
  }
  .legend-content {
    justify-content: flex-start; /* 금액이 사라지면 왼쪽으로 정렬 */
  }
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .user-asset-chart {
    padding: 16px;
    flex-direction: row;
  }

  .chart-wrapper {
    width: 250px;
    height: 250px;
  }

  .legend-container {
    padding: 0; /* 모바일에서는 굳이 필요 없으므로 패딩 제거 */
    gap: 12px; /* 간격 조정 */
    flex-direction: column; /* 세로로 정렬 */
  }

  .legend-item {
    flex-basis: calc(50% - 6px); /* 2개씩 배치 (gap 12px의 절반인 6px을 뺌) */
    max-width: none;
    padding: 10px 12px; /* 패딩 추가 조정 */
    min-width: unset; /* 최소 너비 제한 해제하여 더 유연하게 */
  }

  .asset-amount {
    font-size: 14px; /* 폰트 크기 줄임 */
    display: contents;
  }

  .asset-type,
  .asset-amount {
    font-size: 14px;
  }
  .asset-percentage {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .chart-wrapper {
    width: 200px;
    height: 200px;
  }

  .legend-container {
    flex-direction: column;
    gap: 8px;
  }

  .legend-item {
    flex-basis: 100%; /* 한 줄에 하나씩 */
    max-width: 100%;
    padding: 12px;
    justify-content: space-between;
  }

  .asset-amount {
    display: none; /* 모바일에서는 금액 숨김 */
  }

  .asset-info {
    flex-direction: row;
    align-items: center;
    gap: 8px;
  }

  .legend-content {
    flex-direction: row;
    align-items: center;
    gap: 8px;
    justify-content: space-between; /* 다시 양 끝 정렬 (amount가 숨겨졌어도) */
  }
}
</style>
