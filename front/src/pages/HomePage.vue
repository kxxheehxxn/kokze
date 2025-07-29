<script setup>
import UserAssetComponent from '@/components/homePage/UserAssetComponent.vue';
import AssetChartComponent from '@/components/homePage/AssetChartComponent.vue';
import AdComponent from '@/components/homePage/ADComponent.vue';
import QuizComponent from '@/components/homePage/QuizComponent.vue';
import NoticeSummaryComponent from '@/components/homePage/NoticeSummaryComponent.vue';
import { ref, computed } from 'vue';
import { userAuthStore } from '@/stores/auth';

// 현재 사용자 ID (실제로는 로그인한 사용자 정보에서 가져와야 함)
const auth = userAuthStore();

// 사용자가 로그인했는지 확인 (디버깅 추가)
const isUserLoggedIn = computed(() => {
  console.log('=== 로그인 상태 디버깅 ===');
  console.log('auth.state:', auth.state);
  console.log('auth.email:', auth.email);
  console.log('auth.userId:', auth.userId);
  console.log('auth.isLogin:', auth.isLogin);
  console.log('========================');

  return auth.isLogin; // auth store의 isLogin computed 속성 사용
});

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
    <!-- 디버깅 정보 (개발용) -->

    <!-- 메인 컨텐츠 -->
    <div class="main-content">
      <!-- 1. 사용자 자산 현황 + 차트 (상단) - 로그인 시에만 표시 -->
      <div class="asset-section" v-if="isUserLoggedIn">
        <div class="asset-info-component">
          <UserAssetComponent :userId="auth.userId" />
        </div>
        <div class="chart-component">
          <AssetChartComponent :userId="auth.userId" />
        </div>
      </div>

      <!-- 2. 광고 + (퀴즈/공지사항) (중간) -->
      <div class="middle-section" :class="{ 'no-asset-section': !isUserLoggedIn }">
        <div class="ad-component">
          <AdComponent />
        </div>
        <div class="right-section">
          <div class="quiz-component">
            <QuizComponent />
          </div>
          <div class="notice-component">
            <NoticeSummaryComponent />
          </div>
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
  margin-top: 100px; /* 상단 여백 */
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
  border-radius: 0 20px 20px 0;
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
  margin-top: 100px; /* 상단 여백 */
  max-width: none;
}

/* ===== 4. 하단 네비게이션 카드 ===== */
.bottom-navigation-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 100%;
  max-width: none;
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
    margin-top: 20px;
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
    font-size: 16px;
  }

  /* 네비게이션 카드 - 모든 화면에서 가로 3개 유지 */
  .bottom-navigation-section {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }

  .nav-card {
    height: 120px;
    font-size: 12px;
    padding: 12px;
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
    padding: 10px;
  }

  .service-title-section {
    height: 60px;
    font-size: 14px;
  }

  .nav-card {
    height: 100px;
    font-size: 11px;
    padding: 8px;
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
