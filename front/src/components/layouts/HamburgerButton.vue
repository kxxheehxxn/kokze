<script setup>
import { userAuthStore } from '@/stores/auth.js';
import { useRouter } from 'vue-router';
// Pinia store 사용
const authStore = userAuthStore();
const router = useRouter();
// Props 정의
const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
});

// Emits 정의
const emit = defineEmits(['close', 'menu-click']);

// 로그인 상태는 store에서 가져오기
const isLoggedIn = authStore.isLogin;
const userEmail = authStore.email;

// 사이드바 닫기
const closeSidebar = () => {
  emit('close');
};

// 메뉴 클릭 핸들러
const handleMenuClick = (menuType) => {
  console.log(`${menuType} 클릭됨`);

  // 로그아웃 처리
  if (menuType === '로그아웃') {
    authStore.logout();
  }
  if (menuType === '문의하기') {
    router.push({ name: 'inquiryList' }); // ← 여기에 해당 이름 맞게 설정
  }
  // 부모 컴포넌트로 메뉴 클릭 이벤트 전달
  emit('menu-click', menuType);

  // 사이드바 닫기
  closeSidebar();
};
</script>

<template>
  <!-- 햄버거 메뉴 (오버레이 제거) -->
  <div class="hamburgerbar" :class="{ 'hamburgerbar-open': isOpen }">
    <div class="hamburgerbar-content">
      <!-- 로그인된 사용자 정보 표시 -->
      <div v-if="isLoggedIn" class="user-info">
        <div class="user-email">{{ userEmail }}</div>
      </div>

      <!-- 햄버거 메뉴 항목들 -->
      <div class="hamburgerbar-menu">
        <div
          class="hamburgerbar-menu-item"
          @click="handleMenuClick('공지사항')"
        >
          공지사항
        </div>
        <div
          class="hamburgerbar-menu-item"
          @click="handleMenuClick('문의하기')"
        >
          문의하기
        </div>

        <!-- 로그인된 사용자만 보이는 메뉴 -->
        <div
          v-if="isLoggedIn"
          class="hamburgerbar-menu-item"
          @click="handleMenuClick('로그아웃')"
        >
          로그아웃
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 사용자 정보 영역 */
.user-info {
  padding: 15px 20px;
  border-bottom: 2px solid #e5e7eb;
  background-color: #f8f9fa;
  border-radius: 12px 12px 0 0;
}

.user-email {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
  text-align: center;
  word-break: break-all;
}

/* 햄버거 메뉴 - 위에서 아래로 드롭다운 */
.hamburgerbar {
  position: fixed;
  top: 80px; /* 헤더 바로 아래 고정 위치 */
  right: 20px;
  width: 200px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  border: 1px solid #e5e7eb;
  z-index: 1002;

  /* 드롭다운 애니메이션 효과 */
  max-height: 0;
  overflow: hidden;
  opacity: 0;
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

.hamburgerbar-open {
  max-height: 500px; /* 충분한 높이값 설정 */
  opacity: 1;
  transform: translateY(0);
}

.hamburgerbar-content {
  padding: 15px 0;
  height: auto;
  border-radius: 10px;
}

.hamburgerbar-menu {
  display: flex;
  flex-direction: column;
}

.hamburgerbar-menu-item {
  padding: 15px 20px;
  border-bottom: 1px solid #f1f3f4;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  transition: all 0.2s ease;
  text-align: center;
}

.hamburgerbar-menu-item:hover {
  background-color: #f8f9fa;
  color: #3b82f6;
  padding-left: 30px;
}

.hamburgerbar-menu-item:last-child {
  border-bottom: none;
  border-radius: 0 0 12px 12px;
}

.hamburgerbar-menu-item:first-child {
  border-radius: 12px 12px 0 0;
}

.login-item:hover {
  background-color: #f0fdf4;
  color: #16a34a;
}

/* 반응형 */
@media (max-width: 480px) {
  .hamburgerbar {
    width: 150px;
    right: -150px;
    top: 50px;
  }

  .hamburgerbar-open {
    right: 10px;
  }

  .hamburgerbar-menu-item {
    padding: 12px 15px;
    font-size: 13px;
  }

  .hamburgerbar-menu-item:hover {
    padding-left: 20px;
  }
}
</style>
