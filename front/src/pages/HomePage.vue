<script setup>
import UserAssetComponent from '@/components/homePage/UserAssetComponent.vue';

// 최상단으로 스크롤하는 함수
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
      <!-- 1. 사용자 자산 현황 + 차트 (상단) -->
      <div class="asset-section">
        <div class="asset-info-component">
          <UserAssetComponent />
        </div>
        <div class="chart-component">차트 컴포넌트</div>
      </div>

      <!-- 2. 광고 + (퀴즈/공지사항) (중간) -->
      <div class="middle-section">
        <div class="ad-component">광고 컴포넌트</div>
        <div class="right-section">
          <div class="quiz-component">퀴즈 컴포넌트</div>
          <div class="notice-component">공지사항 컴포넌트</div>
        </div>
      </div>

      <!-- 3. 서비스 소개 타이틀 -->
      <div class="service-title-section">서비스 소개 타이틀</div>

      <!-- 4. 하단 3개 컴포넌트 (세금 관리, 금융 상품 추천, 목표) -->
      <div class="bottom-navigation-section">
        <div class="nav-card">세금 관리 컴포넌트</div>
        <div class="nav-card">금융 상품 추천 컴포넌트</div>
        <div class="nav-card">목표 컴포넌트</div>
      </div>
    </div>

    <!-- ScrollToTop 버튼 -->
    <div class="scroll-top-btn" @click="scrollToTop">
      <div class="scroll-top-icon">
        <i class="fa-solid fa-chevron-up" style="color: #3573ee"></i>
      </div>
    </div>
  </div>
</template>

<style scoped>
.homepage {
  min-height: 100vh;
  background-color: #fbfbfb;
  padding: 0;
  width: 100%;
  overflow-x: hidden;
}

.main-content {
  background-color: transparent;
  max-width: 800px;
  margin: 0 auto; /* 중앙 정렬 */
  padding: 30px 20px;
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
  gap: 20px;
  width: 100%;
  max-width: 800px; /* 최대 너비 제한 */
  justify-content: center; /* 중앙 정렬 */
}

.asset-info-component {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.chart-component {
  flex: 1;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  min-height: 200px;
  font-size: 16px;
}

/* ===== 2. 광고 + 퀴즈/공지사항 영역 ===== */
.middle-section {
  display: flex;
  gap: 20px;
  min-height: 250px;
  width: 100%;
  max-width: 800px; /* 최대 너비 제한 */
  justify-content: center; /* 중앙 정렬 */
}

.ad-component {
  flex: 2;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  font-size: 18px;
}

.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.quiz-component {
  flex: 3;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  min-height: 140px;
  font-size: 14px;
}

.notice-component {
  flex: 2;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  text-align: center;
  padding: 15px;
  min-height: 95px;
  font-size: 14px;
}

/* ===== 3. 서비스 소개 타이틀 ===== */
.service-title-section {
  height: 80px;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  font-size: 18px;
  width: 100%;
  max-width: 800px; /* 최대 너비 제한 */
}

/* ===== 4. 하단 네비게이션 카드 ===== */
.bottom-navigation-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 100%;
  max-width: 800px; /* 최대 너비 제한 */
  justify-content: center; /* 그리드 중앙 정렬 */
}

.nav-card {
  height: 180px;
  background-color: #d1d5db;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px dashed #9ca3af;
  text-align: center;
  padding: 20px;
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
    max-width: 900px;
    padding: 25px 20px;
    gap: 25px;
  }

  .asset-section,
  .middle-section {
    gap: 15px;
  }

  .bottom-navigation-section {
    gap: 15px;
  }

  .nav-card {
    height: 160px;
    padding: 15px;
  }

  .scroll-top-btn {
    width: 52px;
    height: 52px;
    bottom: 25px;
    right: 25px;
  }
}

/* ===== 모바일 반응형 (768px 이하) ===== */
@media (max-width: 768px) {
  .main-content {
    padding: 20px 15px;
    gap: 20px;
  }

  /* 자산 + 차트 세로 배치 */
  .asset-section {
    flex-direction: column;
    gap: 15px;
  }

  .asset-info-component,
  .chart-component {
    min-height: 160px;
  }

  /* 광고 + 퀴즈/공지 세로 배치 */
  .middle-section {
    flex-direction: column;
    gap: 15px;
    min-height: auto;
  }

  .ad-component {
    height: 180px;
    font-size: 16px;
  }

  .right-section {
    flex-direction: row;
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
    font-size: 16px;
  }

  /* 네비게이션 카드 - 모든 화면에서 가로 3개 유지 */
  .bottom-navigation-section {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px; /* 간격 줄임 */
  }

  .nav-card {
    height: 120px; /* 높이 축소 */
    font-size: 12px; /* 폰트 크기 축소 */
    padding: 12px; /* 패딩 축소 */
    border-radius: 12px; /* 모서리 둥글기 축소 */
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
    padding: 15px 10px;
    gap: 15px;
  }

  .asset-info-component,
  .chart-component {
    min-height: 140px;
  }

  .chart-component {
    font-size: 14px;
  }

  .ad-component {
    height: 150px;
    font-size: 14px;
  }

  .right-section {
    flex-direction: column;
    gap: 10px;
  }

  .quiz-component,
  .notice-component {
    min-height: 100px;
    font-size: 12px;
    padding: 10px;
  }

  .service-title-section {
    height: 60px;
    font-size: 14px;
  }

  .nav-card {
    height: 100px; /* 더 작게 */
    font-size: 11px; /* 더 작은 폰트 */
    padding: 8px; /* 더 작은 패딩 */
    border-radius: 10px; /* 더 작은 모서리 */
    /* 간격도 더 줄임 */
  }

  .bottom-navigation-section {
    gap: 8px; /* 간격 더 줄임 */
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

/* ===== 매우 작은 화면 (320px 이하) ===== */
@media (max-width: 320px) {
  .main-content {
    padding: 10px 8px;
    gap: 12px;
  }

  .asset-info-component,
  .chart-component {
    min-height: 120px;
  }

  .chart-component {
    font-size: 13px;
  }

  .ad-component {
    height: 130px;
    font-size: 13px;
  }

  .quiz-component,
  .notice-component {
    min-height: 90px;
    font-size: 11px;
    padding: 8px;
  }

  .service-title-section {
    height: 50px;
    font-size: 13px;
  }

  .nav-card {
    height: 85px; /* 매우 작게 */
    font-size: 10px; /* 매우 작은 폰트 */
    padding: 6px; /* 최소 패딩 */
    border-radius: 8px; /* 작은 모서리 */
    line-height: 1.2; /* 줄 간격 조정 */
  }

  .bottom-navigation-section {
    gap: 6px; /* 최소 간격 */
  }

  .scroll-top-btn {
    width: 40px;
    height: 40px;
    bottom: 12px;
    right: 12px;
  }

  .scroll-top-icon {
    font-size: 14px;
  }
}
</style>
