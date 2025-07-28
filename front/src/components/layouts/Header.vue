<script setup>
import { ref } from 'vue';
import NavBar from './NavBar.vue';
import HamburgerButton from './HamburgerButton.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
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
      router.push({ name: 'noticeList' });
      break;
    case '문의하기':
      router.push({ name: 'inquiryList' });
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
      <div
        class="navigation-space d-flex justify-content-center align-items-center"
      >
        <NavBar />
      </div>

      <!-- 햄버거 메뉴 버튼 (오른쪽 고정) -->
      <div class="hamburger-section d-flex align-items-center">
        <button
          class="hamburger-btn btn d-flex align-items-center justify-content-center"
          @click="toggleHamburger"
          :class="{ active: isHamburgerOpen }"
        >
          <i
            class="fa-solid fa-bars"
            style="background-color: transparent; color: white"
          ></i>
        </button>
      </div>
    </div>
  </header>

  <!-- Sidebar 컴포넌트 -->
  <HamburgerButton
    :is-open="isHamburgerOpen"
    @close="closeHamburger"
    @menu-click="handleHamburgerMenuClick"
  />
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

/* 1023px 이하 (데스크탑 -> 태블릿 전환 지점) */
@media (max-width: 1023px) {
  .header-container {
    height: 70px; /* 헤더 높이 약간 줄임 */
  }

  .logo-section {
    margin-left: 15px; /* 왼쪽 여백 줄임 */
  }

  .logo-icon {
    width: 48px; /* 로고 아이콘 크기 줄임 */
    height: 48px;
  }

  /* 네비게이션 바는 이 크기부터는 숨김 (햄버거 메뉴로 대체) */
  .navigation-space {
    /* 기존 display: none !important; 를 제거하고 flex 아이템으로 동작하도록 변경 */
    position: static; /* absolute 포지셔닝 해제 */
    left: auto; /* 기존 absolute 속성 제거 */
    transform: none; /* 기존 absolute 속성 제거 */
    flex-grow: 1; /* 남은 공간을 채우도록 설정하여 로고와 햄버거 버튼 사이에 배치 */
    justify-content: center; /* NavBar 내부 정렬 (NavBar가 flex 컨테이너라고 가정) */
    align-items: center;
  }

  .hamburger-section {
    margin-right: 15px; /* 오른쪽 여백 줄임 */
  }

  .hamburger-btn {
    width: 45px; /* 햄버거 버튼 크기 줄임 */
    height: 45px;
  }

  .hamburger-btn i {
    font-size: 22px; /* 햄버거 아이콘 크기 줄임 */
  }
}

/* 768px 이하 (태블릿 -> 모바일 전환 지점) */
@media (max-width: 768px) {
  .header-container {
    height: 60px; /* 헤더 높이 더 줄임 */
  }

  .logo-section {
    margin-left: 10px; /* 왼쪽 여백 더 줄임 */
  }

  .logo-icon {
    width: 40px; /* 로고 아이콘 크기 더 줄임 */
    height: 40px;
  }

  .hamburger-section {
    margin-right: 10px; /* 오른쪽 여백 더 줄임 */
  }

  .hamburger-btn {
    width: 40px; /* 햄버거 버튼 크기 더 줄임 */
    height: 40px;
  }

  .hamburger-btn i {
    font-size: 20px; /* 햄버거 아이콘 크기 더 줄임 */
  }
}

/* 480px 이하 (모바일) */
@media (max-width: 480px) {
  .header-container {
    height: 50px; /* 헤더 높이 최소화 */
  }

  .logo-section {
    margin-left: 8px; /* 왼쪽 여백 최소화 */
  }

  .logo-icon {
    width: 35px; /* 로고 아이콘 크기 최소화 */
    height: 35px;
  }

  .hamburger-section {
    margin-right: 8px; /* 오른쪽 여백 최소화 */
  }

  .hamburger-btn {
    width: 35px; /* 햄버거 버튼 크기 최소화 */
    height: 35px;
  }

  .hamburger-btn i {
    font-size: 18px; /* 햄버거 아이콘 크기 최소화 */
  }
}
</style>
