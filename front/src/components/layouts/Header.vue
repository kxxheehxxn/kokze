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
  <header class="header">
    <div class="header-container">
      <!-- 로고 영역 (왼쪽 고정) -->
      <router-link to="/" class="logo-section text-decoration-none">
        <div class="logo d-flex align-items-center">
          <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
        </div>
      </router-link>

      <!-- 네비게이션 컴포넌트 공간 (중앙) -->
      <div class="navigation-space d-flex justify-content-center align-items-center">
        <NavBar />
      </div>

      <!-- 햄버거 메뉴 버튼 (오른쪽 고정) -->
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
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: #fff;
  width: 100%;
  padding: 0;
  margin: 0;
}

.header-container {
  width: 100%;
  max-width: none !important;
  padding: 0;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  position: relative;
}

/* 로고 영역 (왼쪽 고정) */
.logo-section {
  cursor: pointer;
  transition: transform 0.2s ease;
  background-color: transparent;
  flex-shrink: 0;
  margin-left: 20px; /* 로고에만 왼쪽 여백 */
  
}

.logo {
  background-color: transparent;
}

.logo-section:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 54px;
  height: 54px;
  background: transparent !important;
  border-radius: 50%;
  padding: 2px;
  object-fit: contain;
}

/* 네비게이션 공간 (중앙) */
.navigation-space {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  background-color: transparent;
}

/* 햄버거 버튼 (오른쪽 고정) */
.hamburger-section {
  background-color: transparent;
  flex-shrink: 0;
  margin-right: 20px; /* 햄버거 버튼에만 오른쪽 여백 */
}

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

.hamburger-btn i {
  color: white;
  font-size: 24px;
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
</style>
