<template>
  <div class="user-asset-chart">
    <div class="loading-state" v-if="loading">
      <p class="loading-message">자산 정보를 불러오는 중...</p>
    </div>
    
    <template v-else>
      <div class="chart-container" :class="{ 'empty-chart': !hasAssetData }">
        <div class="chart-wrapper">
          <Doughnut ref="doughnutChartRef" :data="chartData" :options="chartOptions" :plugins="chartPlugins" />
        </div>
      </div>
      
      <div class="legend-container" v-if="hasAssetData">
        <div
          class="legend-item"
          v-for="(item, index) in assetData"
          :key="index"
          :class="{ 'legend-hover': hoveredIndex === index }"
          @mouseover="hoveredIndex = index"
          @mouseleave="hoveredIndex = null">
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
      
      <div class="empty-state" v-else>
        <p class="empty-message">등록된 자산이 없습니다</p>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'vue-chartjs';
import assetApi from '@/api/assetApi';

ChartJS.register(ArcElement, Tooltip, Legend);

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

const accountTypeColors = {
  적금: '#A3E4E0',
  예금: '#87CEEB',
  청약: '#FFB6C1',
  펀드: '#DDA0DD',
  주식: '#F0E68C',
  채권: '#98FB98',
  기타: '#D3D3D3',
};

const apiAssetData = ref([]);

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
    return {
      labels: ['자산 없음'],
      datasets: [
        {
          data: [100],
          backgroundColor: ['#E8E8E8'],
          borderColor: ['#D0D0D0'],
          borderWidth: 1,
          hoverBackgroundColor: ['#E8E8E8'],
          hoverBorderColor: ['#D0D0D0'],
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
  
  interaction: hasAssetData.value ? {
    intersect: true,
    mode: 'point'
  } : {
    intersect: false,
    mode: false
  },
  
  events: hasAssetData.value
    ? ['mousemove', 'mouseout', 'click', 'touchstart', 'touchmove']
    : [],
    
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: hasAssetData.value,
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
      borderWidth: hasAssetData.value ? 2 : 1,
      borderColor: '#ffffff',
      hoverBorderWidth: hasAssetData.value ? 3 : 1,
    },
  },
  
  cutout: '60%',
  animation: {
    animateScale: true,
    animateRotate: true,
    duration: 1000,
  },
  
  onHover: hasAssetData.value ? (event, elements) => {
    if (event?.native?.target) {
      event.native.target.style.cursor = elements.length > 0 ? 'pointer' : 'default';
    }
  } : null,
}));

const chartPlugins = [
  {
    id: 'centerText',
    beforeDraw: function (chart) {
      const { width, height, ctx } = chart;
      ctx.restore();
      const fontSize = Math.min(width, height) / 16;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      
      const centerX = width / 2;
      const centerY = height / 2;
      
      if (hasAssetData.value) {
        ctx.fillStyle = '#333';
        ctx.font = `bold ${fontSize}px Arial`;
        ctx.fillText('총 자산', centerX, centerY - fontSize / 2);
        ctx.font = `bold ${fontSize * 0.8}px Arial`;
        ctx.fillText(formatCurrency(totalAssets.value).replace(' 원', '원'), centerX, centerY + fontSize / 2);
      } else {
        ctx.fillStyle = '#999';
        ctx.font = `${fontSize * 1.1}px Arial`;
        ctx.fillText('등록된', centerX, centerY - fontSize / 1.3);
        ctx.fillText('자산이 없음', centerX, centerY + fontSize / 1.3);
      }
      ctx.save();
    },
  },
];

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
    return '기타';
  }
};

const fetchUserAssetChart = async () => {
  loading.value = true;
  error.value = null;
  try {
    const accounts = await assetApi.getUserBankAccounts(props.userId);
    if (accounts && accounts.length > 0) {
      const groupedData = accounts.reduce((acc, account) => {
        const originalType = account.accountType;
        const groupedType = getGroupedAccountType(originalType);
        const amount = parseInt(account.balance) || 0;
        if (!acc[groupedType]) {
          acc[groupedType] = {
            type: groupedType,
            amount: 0,
            accountCount: 0,
            color: accountTypeColors[groupedType] || '#D3D3D3',
            originalTypes: new Set(),
          };
        }
        acc[groupedType].amount += amount;
        acc[groupedType].accountCount += 1;
        acc[groupedType].originalTypes.add(originalType);
        return acc;
      }, {});
      apiAssetData.value = Object.values(groupedData).map((item) => {
        const { originalTypes, ...rest } = item;
        return rest;
      });
    } else {
      apiAssetData.value = [];
    }
  } catch (err) {
    console.error('계좌 데이터 조회 실패:', err);
    error.value = '자산 차트 정보를 불러오는 데 실패했습니다.';
    apiAssetData.value = [];
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
  width: 100%;
}

.chart-container.empty-chart {
  opacity: 0.8;
}

.chart-container.empty-chart .chart-wrapper {
  filter: grayscale(10%);
}

.chart-wrapper {
  position: relative;
  width: 300px;
  height: 300px;
  max-width: 100%;
  max-height: 100%;
}

.legend-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start; 
  gap: 16px;
  margin-bottom: 12px;
  margin-top: 12px;
  width: 100%;
  padding: 0 10px;
  box-sizing: border-box;
  height: 140px;
  align-content: flex-start;
}

.legend-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 12px;
  transition: background-color 0.2s ease;
  cursor: pointer;
  box-sizing: border-box;
  flex-basis: calc(50% - 8px);
  max-width: calc(50% - 8px);
  min-width: 140px;
  justify-content: flex-start;
  min-height: 40px;
  flex-shrink: 1;
}

.legend-container:has(.legend-item:nth-child(5)) .legend-item {
  padding: 8px 12px !important;
  min-height: 28px !important;
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
  gap: 8px;
}

.asset-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.asset-type {
  font-weight: 500;
  color: #333;
  font-size: 16px;
  white-space: nowrap;
}

.asset-percentage {
  font-weight: 400;
  color: #666;
  font-size: 14px;
  white-space: nowrap;
}

.asset-amount {
  font-weight: 600;
  color: #333;
  font-size: 16px;
  text-align: right;
  white-space: nowrap;
}

.empty-state {
  margin-top: 20px;
  margin-bottom: 32px;
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 12px;
  border: 2px dashed #ddd;
  width: 100%;
  max-width: 400px;
}

.empty-message {
  color: #666;
  font-size: 18px;
  font-weight: 500;
  margin: 0;
  line-height: 1.4;
}
.loading-state {
  margin-top: 20px;
  margin-bottom: 32px;
  text-align: center;
  padding: 20px;
}

.loading-message {
  color: #666;
  font-size: 16px;
  text-align: center;
  margin: 0;
  position: relative;
}

.loading-message::after {
  content: "";
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-left: 10px;
  border: 2px solid #ddd;
  border-top: 2px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 1024px) {
  .asset-amount {
    display: none;
  }
  .legend-content {
    justify-content: flex-start;
  }
}

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
    justify-content: flex-start;
    padding: 0;
    gap: 12px;
    flex-direction: column;
    height: auto;
    align-content: normal;
  }
  .legend-item {
    flex-basis: calc(50% - 6px);
    max-width: none;
    padding: 10px 12px;
    min-width: unset;
    min-height: auto;
  }
  .asset-amount {
    font-size: 14px;
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
    flex-basis: 100%;
    max-width: 100%;
    padding: 12px;
    justify-content: space-between;
  }
  .asset-amount {
    display: none;
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
    justify-content: space-between;
  }
}
</style>
