<script setup>
import { ref } from 'vue';
import NavBar from './NavBar.vue';
import HamburgerButton from './HamburgerButton.vue';

// 햄버거 메뉴 상태
const isHamburgerOpen = ref(false);

// 햄버거 메뉴 토글
const toggleHamburger = () => {
  isHamburgerOpen.value = !isHamburgerOpen.value;
};

// 햄버거 닫기
const closeHamburger = () => {
  isHamburgerOpen.value = false;
};

// 햄버거버튼 메뉴 클릭 핸들러
const handleHamburgerMenuClick = (menuType) => {
  console.log(`햄버거 리스트에서 ${menuType} 클릭됨`);

  // 실제 구현 시 각 메뉴별 라우팅 처리
  switch (menuType) {
    case '공지사항':
      // router.push('/notice')
      break;
    case '문의하기':
      // router.push('/inquiry')
      break;
    case '로그아웃':
      // 로그아웃 처리
      break;
  }
};
</script>

<template>
  <header class="header sticky-top">
    <div class="header-container container-xxl">
      <!-- 로고 영역 -->
      <router-link to="/" class="logo-section text-decoration-none">
        <div class="logo d-flex align-items-center">
          <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
        </div>
      </router-link>

      <!-- 네비게이션 컴포넌트 공간 -->
      <div class="navigation-space flex-grow-1 d-flex justify-content-center align-items-center">
        <NavBar />
      </div>

      <!-- 햄버거 메뉴 버튼 -->
      <div class="hamburger-section d-flex align-items-center">
        <button
          class="hamburger-btn btn d-flex align-items-center justify-content-center"
          @click="toggleHamburger"
          :class="{ active: isHamburgerOpen }"
        >
          <i class="fa-solid fa-bars" style="background-color: transparent; color: white"></i>
        </button>
      </div>
    </div>
  </header>

  <!-- Sidebar 컴포넌트 -->
  <HamburgerButton :is-open="isHamburgerOpen" @close="closeHamburger" @menu-click="handleHamburgerMenuClick" />
</template>

<style scoped>
.header {
  z-index: 1000;
  background-color: transparent;
  /* 안개 효과 추가 */
  backdrop-filter: blur(2px); /* 픽셀 값을 조절하여 블러 강도 변경 */
}

.header-container {
  padding: 0 15px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: transparent;
}

/* 로고 영역 */
.logo-section {
  cursor: pointer;
  transition: transform 0.2s ease;
  background-color: transparent;
}

.logo {
  background-color: transparent;
}

.logo-section:hover {
  transform: scale(1.05);
}

/* 로고 영역 */
.logo-icon {
  width: 54px; /* 햄버거 버튼보다 4px 크게 */
  height: 54px;
  background: transparent !important;
  border-radius: 50%;
  padding: 2px; /* 패딩 줄임 */
  object-fit: contain;
}

/* 네비게이션 공간 */
.navigation-space {
  margin: 0 40px;
  background-color: transparent;
}

.hamburger-section {
  background-color: transparent;
}

/* 사이드바 메뉴 */
.hamburger-btn {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  padding: 0;
}

/* 사이드바 버튼 내부 아이콘 스타일 */
.hamburger-btn i {
  color: white; /* 이미 추가하셨던 흰색 색상 */
  font-size: 24px; /* 원하는 크기로 조절하세요 (예: 24px) */
}

.hamburger-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.hamburger-btn:active {
  transform: translateY(0);
}

.hamburger-btn:focus {
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

/* 반응형 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 15px;
    height: 60px;
  }

  .navigation-space {
    margin: 0 20px;
  }

  .logo-icon {
    width: 46px; /* 햄버거보다 4px 크게 */
    height: 46px;
    padding: 2px;
  }

  .hamburger-btn {
    width: 42px;
    height: 42px;
  }

  .hamburger-btn i {
    font-size: 18px;
  }
}

@media (max-width: 480px) {
  .header-container {
    padding: 0 10px;
    height: 50px;
  }

  .navigation-space {
    margin: 0 15px;
  }

  .logo-icon {
    width: 39px; /* 햄버거보다 4px 크게 */
    height: 39px;
    padding: 2px;
  }

  .hamburger-btn {
    width: 35px;
    height: 35px;
  }

  .hamburger-btn i {
    font-size: 14px;
  }
}
</style>
