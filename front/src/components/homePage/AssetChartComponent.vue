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

// --- Chart.js 전역 설정 ---
// Chart.js에 도넛 차트에 필요한 요소들을 등록합니다.
// ArcElement: 도넛/파이 차트의 각 섹션 (호)
// Tooltip: 마우스를 올렸을 때 나타나는 정보 창
// Legend: 차트 범례 (여기서는 커스텀 범례를 사용하므로 Chart.js의 기본 범례는 비활성화됩니다.)

// Chart.js 등록
ChartJS.register(ArcElement, Tooltip, Legend);

// 반응형 데이터
const hoveredIndex = ref(null); // 범례 항목에 마우스 오버 시 인덱스 저장
const loading = ref(false); // 로딩 상태
const error = ref(null);

// --- 자산 더미 데이터 ---
// 각 객체는 자산 유형, 금액, 색상을 포함합니다.
const defaultAssetData = ref([
  { type: '현금', amount: 2082970, color: '#A3E4E0' },
  { type: '투자금', amount: 1402300, color: '#F4C790' },
  { type: '적금', amount: 750000, color: '#D8B4FE' },
  { type: '예금', amount: 1500000, color: '#C7F5A3' },
]);

// --- API에서 가져온 실제 데이터 (API Data) ---
// 백엔드 API에서 받아올 자산 데이터를 저장할 ref입니다.
// 현재는 빈 배열로 초기화되어 있으며, API 호출 시 여기에 데이터가 채워집니다.
const apiAssetData = ref([]);

// --- Computed 속성 (Computed Properties) ---
// 종속된 반응형 데이터가 변경될 때 자동으로 재계산되는 값들입니다.
const totalAssets = computed(() => {
  // apiAssetData가 있으면 apiAssetData를, 없으면 defaultAssetData를 사용
  const dataToUse = apiAssetData.value.length > 0 ? apiAssetData.value : defaultAssetData.value;
  // 모든 자산 금액의 합계를 계산하여 총 자산을 반환합니다.
  return dataToUse.reduce((sum, asset) => sum + asset.amount, 0);
});

const assetData = computed(() => {
  // totalAssets와 마찬가지로 사용할 데이터를 결정합니다.
  const dataToUse = apiAssetData.value.length > 0 ? apiAssetData.value : defaultAssetData.value;

  // 각 항목의 percentage를 동적으로 계산하여 추가
  // 총 자산이 0보다 클 때만 백분율을 계산하여 'NaN' 오류를 방지합니다.
  return dataToUse.map((item) => ({
    ...item,
    percentage: totalAssets.value > 0 ? (item.amount / totalAssets.value) * 100 : 0,
  }));
});

// --- Chart.js 데이터 구성 (Chart.js Data Configuration) ---
// Doughnut 차트에 실제로 전달될 데이터를 구성하는 computed 속성입니다.
const chartData = computed(() => ({
  labels: assetData.value.map((item) => item.type),
  datasets: [
    {
      data: assetData.value.map((item) => item.amount), // 각 자산의 금액을 데이터로 사용
      backgroundColor: assetData.value.map((item) => item.color), // 각 자산의 색상을 배경색으로 사용
      borderColor: assetData.value.map((item) => item.color), // 테두리 색상도 동일하게 설정
      borderWidth: 2, // 테두리 두께 설정
    },
  ],
}));

// --- Chart.js 옵션 설정 (Chart.js Options Configuration) ---
// Doughnut 차트의 시각적 및 상호작용 관련 설정을 정의하는 computed 속성입니다.
const chartOptions = computed(() => ({
  responsive: true, // 차트 컨테이너 크기에 반응하여 크기 조절
  maintainAspectRatio: true,
  aspectRatio: 1, // 1:1 비율 유지
  plugins: {
    legend: {
      display: false, // 커스텀 범례 사용하므로 비활성화
    },
    tooltip: {
      enabled: true,
      backgroundColor: 'rgba(0, 0, 0, 0.8)', // 툴팁 배경색
      titleColor: '#ffffff', // 툴팁 제목 색상
      bodyColor: '#ffffff', // 툴팁 본문 색상
      borderColor: '#ffffff', // 툴팁 테두리 색상
      borderWidth: 1, // 툴팁 테두리 두께
      cornerRadius: 8, // 툴팁 모서리 둥글기
      displayColors: true, // 툴팁에 색상 표시
      callbacks: {
        // 툴팁의 라벨을 커스터마이즈합니다.
        label: function (context) {
          const label = context.label || ''; // 라벨이 없을 경우 빈 문자열
          const value = formatCurrency(context.parsed); // 금액을 포맷팅
          const percentage = assetData.value[context.dataIndex].percentage.toFixed(1);
          return `${label}: ${value} (${percentage}%)`; // 라벨, 금액, 백분율을 포함한 문자열 반환
        },
      },
    },
  },
  elements: {
    arc: {
      borderWidth: 2, // 각 차트 조각의 테두리 두께
      borderColor: '#ffffff', // 테두리 색상 설정
    },
  },
  cutout: '60%', // 도넛 모양을 위한 중앙 공백
  animation: {
    animateScale: true, // 차트 크기 조정 애니메이션
    animateRotate: true, // 회전 애니메이션
    duration: 1000, // 애니메이션 지속 시간 (밀리초 단위)
  },
}));

// --- Chart.js 플러그인 (Chart.js Plugins) ---
// Chart.js의 기본 기능을 확장하는 커스텀 플러그인 배열입니다.
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

      // 총 자산 텍스트
      ctx.fillText('총 자산', centerX, centerY - fontSize / 2);
      ctx.font = `bold ${fontSize * 0.8}px Arial`;
      ctx.fillText(formatCurrency(totalAssets.value).replace(' 원', '원'), centerX, centerY + fontSize / 2);

      ctx.save();
    },
  },
];

// --- 메서드들 (Methods) ---
// 컴포넌트의 동작을 정의하는 함수들입니다.

// 사용자 자산 차트 데이터를 비동기로 불러오는 함수입니다.
const fetchUserAssetChart = async () => {
  loading.value = true;
  error.value = null;

  try {
    // API 호출 (여기서는 샘플 데이터 사용)

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

// 컴포넌트 마운트 시 실행
onMounted(() => {
  fetchUserAssetChart();
});
</script>

<style scoped>
.user-asset-chart {
  background: #f8f9fa;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  height: 100%;
  max-width: none;
  margin: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  display: flex; /* Flexbox 추가 */
  flex-direction: column; /* 세로 정렬 */
  align-items: center; /* 가운데 정렬 */
}

.chart-container {
  display: flex;
  justify-content: center;
  margin-top: 32px; /* 상단 여백 */
  margin-bottom: 32px; /* 하단 여백 */
}

.chart-wrapper {
  position: relative;
  width: 300px;
  height: 300px;
}

.legend-container {
  display: flex;
  flex-wrap: wrap; /* 항목이 넘치면 다음 줄로 */
  justify-content: center; /* 가로 중앙 정렬 */
  gap: 30px 20px; /* 세로 간격 10px, 가로 간격 20px */
  margin-bottom: 32px;
  margin-top: 48px; /* 상단 여백 */
  width: 100%; /* 부모 너비에 맞춤 */
}

.legend-item {
  display: flex;
  align-items: center;
  padding: 16px 24px; /* 패딩 추가 */
  border-radius: 12px;
  transition: background-color 0.2s ease;
  cursor: pointer;
  box-sizing: border-box; /* padding 포함 너비 계산 */
  flex: 1 1 calc(50% - 10px); /* 2개씩 배치 (gap 고려) */
  max-width: calc(50% - 10px); /* 2개씩 배치 */
  min-width: 150px; /* 너무 작아지지 않도록 최소 너비 설정 (조절 가능) */
  justify-content: flex-start; /* 왼쪽 정렬 */
}

.legend-item:hover,
.legend-hover {
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.legend-indicator {
  margin-right: 8px; /* 이미지에 맞게 여백 축소 */
}

.color-dot {
  width: 16px; /* 이미지에 맞게 크기 축소 */
  height: 16px; /* 이미지에 맞게 크기 축소 */
  border-radius: 50%;
  box-shadow: none; /* 이미지에 그림자 없어 보임 */
  border: 1px solid #eee; /* 테두리 추가 (필요시) */
  flex-shrink: 0; /* 공간이 부족해도 줄어들지 않도록 */
}

.legend-content {
  flex: 1; /* 남은 공간 차지 */
  display: flex;
  flex-direction: row; /* 가로로 정렬 */
  justify-content: space-between; /* 양 끝 정렬 */
  align-items: baseline; /* 텍스트 베이스라인 정렬 */
  gap: 16px; /* 항목 간 간격 */
}

.asset-info {
  display: flex;
  align-items: center; /* 세로 가운데 정렬 */
  gap: 4px; /* 유형과 백분율 사이 간격 축소 */
}

.asset-type {
  font-weight: 500; /* 이미지에 맞게 폰트 굵기 조정 */
  color: #333;
  font-size: 20px; /* 이미지에 맞게 폰트 크기 조정 */
  white-space: nowrap; /* 줄바꿈 방지 */
}

.asset-percentage {
  font-weight: 400; /* 이미지에 맞게 폰트 굵기 조정 */
  color: #666;
  font-size: 20px; /* 이미지에 맞게 폰트 크기 조정 */
  white-space: nowrap; /* 줄바꿈 방지 */
}

.asset-amount {
  font-weight: 600; /* 이미지에 맞게 폰트 굵기 조정 */
  color: #333;
  font-size: 20px; /* 이미지에 맞게 폰트 크기 조정 */
  text-align: right;
  white-space: nowrap; /* 줄바꿈 방지 */
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

  .legend-item {
    flex: 1 1 calc(50% - 10px);
    max-width: calc(50% - 10px);
  }

  .asset-type,
  .asset-amount {
    font-size: 14px; /* 모바일에서 폰트 크기 조정 */
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
    flex-direction: column; /* 작은 화면에서 세로로 다시 정렬 */
    gap: 8px; /* 세로 간격 조정 */
  }

  .legend-item {
    flex: 1 1 100%; /* 한 줄에 하나씩 */
    max-width: 100%;
    padding: 12px; /* 패딩 복원 */
    border: 1px solid #cfcfd0; /* 테두리 복원 */
    justify-content: space-between; /* 양 끝 정렬 */
  }

  .asset-info {
    flex-direction: row; /* 다시 가로로 */
    align-items: center;
    gap: 8px;
  }

  .legend-content {
    flex-direction: row;
    align-items: center;
    gap: 8px;
  }
}
</style>
