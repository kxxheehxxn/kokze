<template>
  <div class="user-asset-chart">
    <div class="chart-container">
      <div class="chart-wrapper">
        <Doughnut :data="chartData" :options="chartOptions" :plugins="chartPlugins" />
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'vue-chartjs';

ChartJS.register(ArcElement, Tooltip, Legend);

const hoveredIndex = ref(null);
const loading = ref(false);
const error = ref(null);

const defaultAssetData = ref([
  { type: '현금', amount: 2082970, color: '#A3E4E0' },
  { type: '투자금', amount: 1402300, color: '#F4C790' },
  { type: '적금', amount: 750000, color: '#D8B4FE' },
  { type: '예금', amount: 1500000, color: '#C7F5A3' },
]);

const apiAssetData = ref([]);

const totalAssets = computed(() => {
  const dataToUse = apiAssetData.value.length > 0 ? apiAssetData.value : defaultAssetData.value;
  return dataToUse.reduce((sum, asset) => sum + asset.amount, 0);
});

const assetData = computed(() => {
  const dataToUse = apiAssetData.value.length > 0 ? apiAssetData.value : defaultAssetData.value;
  return dataToUse.map((item) => ({
    ...item,
    percentage: totalAssets.value > 0 ? (item.amount / totalAssets.value) * 100 : 0,
  }));
});

const chartData = computed(() => ({
  labels: assetData.value.map((item) => item.type),
  datasets: [
    {
      data: assetData.value.map((item) => item.amount),
      backgroundColor: assetData.value.map((item) => item.color),
      borderColor: assetData.value.map((item) => item.color),
      borderWidth: 2,
    },
  ],
}));

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: true,
  aspectRatio: 1,
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: true,
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      titleColor: '#ffffff',
      bodyColor: '#ffffff',
      borderColor: '#ffffff',
      borderWidth: 1,
      cornerRadius: 8,
      displayColors: true,
      callbacks: {
        label: function (context) {
          const label = context.label || '';
          const value = formatCurrency(context.parsed);
          const percentage = assetData.value[context.dataIndex].percentage.toFixed(1);
          return `${label}: ${value} (${percentage}%)`;
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
      ctx.fillStyle = '#333';

      const centerX = width / 2;
      const centerY = height / 2;

      ctx.fillText('총 자산', centerX, centerY - fontSize / 2);
      ctx.font = `bold ${fontSize * 0.8}px Arial`;
      ctx.fillText(formatCurrency(totalAssets.value).replace(' 원', '원'), centerX, centerY + fontSize / 2);

      ctx.save();
    },
  },
];

const fetchUserAssetChart = async () => {
  loading.value = true;
  error.value = null;

  try {
    console.log('자산 차트 데이터 로드 완료 (샘플 데이터)');
  } catch (err) {
    console.error('Failed to fetch user asset chart data:', err);
    error.value = '자산 차트 정보를 불러오는 데 실패했습니다.';
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

onMounted(() => {
  fetchUserAssetChart();
});
</script>

<style scoped>
.user-asset-chart {
  background: #fbfbfb;
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
  /* gap: 30px 20px; 이 부분의 gap을 조정 */
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
  /* flex: 1 1 calc(50% - 10px); -> flex-basis로 유연하게 */
  /* max-width: calc(50% - 10px); -> max-width를 더 유연하게 조정 */
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

/* 1024px 미만 화면에서 .asset-amount 숨기기 */
@media (max-width: 1023px) {
  /* AssetCard.vue의 .asset-item .amount와 유사한 처리 */
  .asset-amount {
    /* .legend-item 내의 .asset-amount */
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
  }

  .chart-wrapper {
    width: 250px;
    height: 250px;
  }

  .legend-container {
    padding: 0; /* 모바일에서는 굳이 필요 없으므로 패딩 제거 */
    gap: 12px; /* 간격 조정 */
  }

  .legend-item {
    flex-basis: calc(50% - 6px); /* 2개씩 배치 (gap 12px의 절반인 6px을 뺌) */
    max-width: calc(50% - 6px);
    padding: 10px 12px; /* 패딩 추가 조정 */
    min-width: unset; /* 최소 너비 제한 해제하여 더 유연하게 */
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
    border: 1px solid #cfcfd0;
    justify-content: space-between;
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
