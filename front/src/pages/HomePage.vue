<script setup>
import UserAssetComponent from '@/components/homePage/UserAssetComponent.vue';
import AssetChartComponent from '@/components/homePage/AssetChartComponent.vue';
import AdComponent from '@/components/homePage/ADComponent.vue';
import QuizComponent from '@/components/homePage/QuizComponent.vue';
import NoticeSummaryComponent from '@/components/homePage/NoticeSummaryComponent.vue';
import HomeToTax from '@/components/homePage/HomeToTaxComponent.vue';
import HomeToProduct from '@/components/homePage/HomeToProductComponent.vue';
import HomeToGoal from '@/components/homePage/HomeToGoalComponent.vue';
import QuizModal from '@/components/QuizModal.vue';
import { ref, computed } from 'vue';
import { userAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const auth = userAuthStore();
const router = useRouter();
const showQuizModal = ref(false);
const chartComponentRef = ref(null);

const isUserLoggedIn = computed(() => {
  return auth.isLogin;
});

const openQuizModal = () => {
  if (auth.isLogin) {
    showQuizModal.value = true;
  } else {
    showLoginAlert();
  }
};

const closeQuizModal = () => {
  showQuizModal.value = false;
};

const handleNavCardClick = (path) => {
  if (auth.isLogin) {
    router.push(path);
  } else {
    showLoginAlert();
  }
};

const showLoginAlert = () => {
  const result = confirm('이용하려면 로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?');

  if (result) {
    router.push('/auth/login');
  }
};

const updateChart = () => {
  if (chartComponentRef.value && chartComponentRef.value.fetchUserAssetChart) {
    chartComponentRef.value.fetchUserAssetChart();
  }
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
};
</script>

<template>
  <div class="homepage">
    <!-- 메인 컨텐츠 -->
    <div class="main-content">
      <!-- 1. 사용자 자산 현황 + 차트 (상단) - 로그인 시에만 표시 -->
      <div class="asset-section" v-if="isUserLoggedIn">
        <div class="asset-info-component">
          <UserAssetComponent :userId="auth.userId" @asset-lookup="updateChart" />
        </div>
        <div class="chart-component">
          <AssetChartComponent :userId="auth.userId" ref="chartComponentRef" />
        </div>
      </div>

      <!-- 2. 광고 + (퀴즈/공지사항) (중간) -->
      <div class="middle-section" :class="{ 'no-asset-section': !isUserLoggedIn }">
        <div class="ad-component">
          <AdComponent />
        </div>
        <div class="right-section">
          <div class="quiz-component" @click="openQuizModal">
            <QuizComponent />
          </div>
          <div class="notice-component">
            <NoticeSummaryComponent />
          </div>
        </div>
      </div>

      <!-- 3. 서비스 소개 타이틀 -->
      <div class="service-title-section">
        <div class="service-title">신뢰와 믿음의 자산관리 “콕재”</div>
        <div class="service-content">MZ세대의 절세 전략과 목표 실현을 위한 스마트 자산관리</div>
      </div>

      <!-- 4. 하단 3개 컴포넌트 (세금 관리, 금융 상품 추천, 목표) -->
      <div class="bottom-navigation-section">
        <div class="nav-card" @click="handleNavCardClick('/tax-management')">
          <HomeToTax />
        </div>
        <div class="nav-card" @click="handleNavCardClick('/product')">
          <HomeToProduct />
        </div>
        <div class="nav-card" @click="handleNavCardClick('/goals')">
          <HomeToGoal />
        </div>
      </div>
    </div>

    <!-- ScrollToTop 버튼 -->
    <div class="scroll-top-btn" @click="scrollToTop">
      <div class="scroll-top-icon">
        <i class="fa-solid fa-chevron-up" style="color: #3573ee"></i>
      </div>
    </div>

    <!-- 퀴즈 모달 -->
    <QuizModal :show="showQuizModal" @close="closeQuizModal" />
  </div>
</template>

<style scoped>
.homepage {
  min-height: 100vh;
  background-color: #fbfbfb;
  padding: 0;
  margin: 0; /* 마진 제거 */
  width: 100vw; /* 뷰포트 전체 너비 */
  max-width: none; /* 최대 너비 제한 해제 */
  overflow-x: hidden;
  box-sizing: border-box; /* 박스 사이징 설정 */
}

.main-content {
  background-color: transparent;
  /* max-width: none; 최대 너비 제한 해제 */
  margin: 0 auto; /* 중앙 정렬 */
  margin-top: 15px;
  padding: 30px 70px;
  display: flex;
  flex-direction: column;
  gap: 30px;
  width: 100%;
  box-sizing: border-box;
  align-items: center; /* 내부 요소들 중앙 정렬 */
}

/* ===== 1. 자산 현황 + 차트 영역 ===== */
.asset-section {
  display: flex;
  gap: 0px; /* 자산 컴포넌트와 차트 컴포넌트 사이 간격 */
  width: 100%;
  /* height: 630px; */
  max-width: none; /* 최대 너비 제한 해제 */
  justify-content: center; /* 중앙 정렬 */
}

.asset-info-component {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  padding: 20px;
}

.chart-component {
  flex: 1.4;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  padding: 20px;
}

/* ===== 2. 광고 + 퀴즈/공지사항 영역 ===== */
.middle-section {
  display: flex;
  gap: 20px;
  min-height: 250px;
  width: 100%;
  padding-right: 20px;
  padding-left: 20px;
  margin-top: 70px; /* 상단 여백 */
  max-width: none;
  justify-content: center; /* 중앙 정렬 */
}

/* 자산 섹션이 없을 때 middle-section의 상단 여백 조정 */
.middle-section.no-asset-section {
  margin-top: 30px; /* 자산 섹션이 없을 때는 여백 줄임 */
}

.ad-component {
  flex: 1.6;
  background-color: #fbfbfb;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  font-size: 18px;
}

.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.quiz-component {
  flex: 2.7;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  min-height: 140px;
  cursor: pointer;
}

.quiz-component:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.notice-component {
  flex: 2.7;
  background-color: transparent;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  text-align: center;
  min-height: 95px;
  width: 100%;
  height: 100%;
}

/* ===== 3. 서비스 소개 타이틀 ===== */
.service-title-section {
  height: 80px;
  background-color: #fbfbfb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  font-size: 28px;
  width: 100%;
  margin-top: 70px; /* 상단 여백 */
  max-width: none;
  flex-direction: column; /* 세로 정렬 */
}

.service-title {
  font-size: 28px;
  font-weight: bold;
}

.service-content {
  font-size: 16px;
  margin-top: 15px;
}

/* ===== 4. 하단 네비게이션 카드 ===== */
.bottom-navigation-section {
  display: grid;
  padding-bottom: 80px;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 100%;
  max-width: none;
  justify-content: center; /* 그리드 중앙 정렬 */
  padding: 0 20px 80px 20px; /* 좌우 패딩 */
}

.nav-card {
  height: 250px;
  background-color: transparent;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  text-align: center;
  font-size: 14px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.nav-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* ===== ScrollToTop 버튼 ===== */
.scroll-top-btn {
  cursor: pointer;
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 56px;
  height: 56px;
  background-color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px solid #e5e7eb;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.scroll-top-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.scroll-top-icon {
  font-size: 20px;
  transition: transform 0.3s ease;
}

.scroll-top-btn:hover .scroll-top-icon {
  transform: translateY(-2px);
}

/* ===== 태블릿 반응형 (1024px 이하) ===== */
@media (max-width: 1024px) {
  .main-content {
    max-width: none;
    margin-top: 0px;
    padding: 25px 0px; /* 좌우 패딩 없음 */
    gap: 25px;
  }

  /* 모든 섹션이 main-content의 너비를 채우도록 */
  .asset-section,
  .middle-section,
  .service-title-section,
  .bottom-navigation-section {
    max-width: 100%;
  }

  .asset-section,
  .middle-section {
    gap: 15px;
  }

  /* 자산 섹션이 없을 때 여백 조정 */
  .middle-section.no-asset-section {
    margin-top: 0px;
  }

  .bottom-navigation-section {
    gap: 15px;
  }

  .nav-card {
    height: 250px;
  }

  .scroll-top-btn {
    width: 52px;
    height: 52px;
    bottom: 25px;
    right: 25px;
  }

  .ad-component {
    border-radius: 0 20px 20px 0; /* border-radius 유지 */
    /* 1024px 이하에서는 main-content의 좌우 패딩이 0이므로 음수 마진 필요 없음 */
    margin-left: 0;
    width: 100%; /* 부모 너비에 꽉 채움 */
  }
}

/* ===== 모바일 반응형 (768px 이하) ===== */
@media (max-width: 768px) {
  .main-content {
    margin: 15px 0px 0px 0px;
    padding: 10px 0px; /* 좌우 패딩 없음 */
    gap: 15px;
  }

  /* 자산 + 차트 세로 배치 */
  .asset-section {
    flex-direction: column;
    gap: 0px;
  }

  .asset-info-component {
    min-width: 160px;
    padding-bottom: 0px;
  }

  .chart-component {
    min-height: 160px;
  }

  /* 광고 + 퀴즈/공지 세로 배치 */
  .middle-section {
    flex-direction: column;
    gap: 15px;
    min-height: auto;
    justify-content: flex-start; /* 왼쪽 정렬 유지 */
  }

  /* 자산 섹션이 없을 때 여백 조정 */
  .middle-section.no-asset-section {
    margin-top: 15px;
  }

  .ad-component {
    height: 180px;
    font-size: 16px;
    border-radius: 0 20px 20px 0; /* border-radius 유지 */
    margin-left: 0; /* 좌우 패딩이 0이므로 음수 마진 필요 없음 */
    width: 100%; /* 부모 너비에 꽉 채움 */
  }

  .right-section {
    flex-direction: row; /* 퀴즈/공지사항은 가로 배치 유지 */
    gap: 15px;
  }

  .quiz-component,
  .notice-component {
    flex: 1;
    min-height: 120px;
    font-size: 13px;
  }

  /* 서비스 타이틀 */
  .service-title-section {
    height: 70px;
  }

  .service-title {
    font-size: 24px;
  }

  .service-content {
    font-size: 14px;
    margin-top: 10px;
  }

  /* 네비게이션 카드 - 모든 화면에서 가로 3개 유지 */
  .bottom-navigation-section {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }

  .nav-card {
    height: 220px;
    font-size: 12px;
    border-radius: 12px;
  }

  /* 스크롤 버튼 */
  .scroll-top-btn {
    width: 48px;
    height: 48px;
    bottom: 20px;
    right: 20px;
  }

  .scroll-top-icon {
    font-size: 18px;
  }
}

/* ===== 작은 모바일 반응형 (480px 이하) ===== */
@media (max-width: 480px) {
  .main-content {
    margin-top: 0px;
    gap: 15px;
  }

  .asset-info-component,
  .chart-component {
    min-height: 140px;
  }

  .chart-component {
    font-size: 14px;
  }

  /* 자산 섹션이 없을 때 여백 조정 */
  .middle-section.no-asset-section {
    margin-top: 10px;
  }

  .ad-component {
    height: 150px;
    font-size: 14px;
    border-radius: 0 20px 20px 0; /* border-radius 유지 */
    margin-left: 0; /* 좌우 패딩이 0이므로 음수 마진 필요 없음 */
    width: 100%; /* 부모 너비에 꽉 채움 */
  }

  .right-section {
    flex-direction: column;
    gap: 10px;
  }

  .quiz-component,
  .notice-component {
    min-height: 100px;
    font-size: 12px;
  }

  .service-title-section {
    height: 60px;
    font-size: 14px;
  }

  .service-title {
    font-size: 20px;
  }

  .service-content {
    font-size: 12px;
    margin-top: 5px;
  }

  .nav-card {
    height: 160px;
    font-size: 11px;
    border-radius: 10px;
  }

  .bottom-navigation-section {
    gap: 8px;
  }

  .scroll-top-btn {
    width: 44px;
    height: 44px;
    bottom: 15px;
    right: 15px;
  }

  .scroll-top-icon {
    font-size: 16px;
  }
}
</style>
