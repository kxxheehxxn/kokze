<template>
  <nav class="navbar">
    <ul class="navbar-list">
      <li
        v-for="item in dynamicNavItems"
        :key="item.name"
        class="navbar-item"
        :class="{ active: item.route === currentRoute }"
      >
        <router-link :to="item.route" class="navbar-link" @click="selectItem(item.name)">
          {{ item.name }}
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { userAuthStore } from '@/stores/auth.js';

const router = useRouter();
const route = useRoute();
const authStore = userAuthStore();

// 현재 라우트 경로 추적
const currentRoute = computed(() => route.path);

// 로그인 상태에 따라 달라지는 내비게이션 아이템 정의
const defaultNavItems = [
  { name: '세금 관리', route: '/tax-management' },
  { name: '금융 상품 추천', route: '/financial-products' },
  { name: '목표', route: '/goals' },
  { name: '용어 설명', route: '/terms' },
];

const loggedOutItem = { name: '로그인', route: '/auth/login' };
const loggedInItem = { name: '마이페이지', route: '/userpage' };

// computed 속성을 사용하여 로그인 상태에 따라 다른 navItems를 반환
const dynamicNavItems = computed(() => {
  if (authStore.isLogin) {
    return [...defaultNavItems, loggedInItem]; // 로그인 시 마이페이지 추가
  } else {
    return [...defaultNavItems, loggedOutItem]; // 로그아웃 시 로그인 추가
  }
});

// 아이템 클릭 시 처리
const selectItem = (itemName) => {
  console.log(`${itemName} 클릭됨`);

  // 특별한 처리가 필요한 경우 (예: 로그아웃)
  if (itemName === '로그인') {
    // 로그인 페이지로 이동하기 전 처리
    console.log('로그인 페이지로 이동');
  } else if (itemName === '마이페이지') {
    // 마이페이지 접근 권한 확인 등
    console.log('마이페이지로 이동');
  }
};
</script>

<style scoped>
.navbar {
  /* 이미지에 보이는 둥근 바깥쪽 테두리 역할을 할 컨테이너 */
  background-color: #fbfbfb;
  border-radius: 50px; /* 아주 둥글게 */
  padding: 10px 20px; /* 내부 여백 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08); /* 그림자 효과 */
  display: flex; /* 내부 아이템 중앙 정렬을 위해 flex 사용 */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  max-width: fit-content; /* 내용물에 맞춰 너비 조절 */
  margin: 0 auto; /* 상위 컨테이너 내에서 중앙 정렬 (필요시) */
}

.navbar-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex; /* 리스트 아이템들을 가로로 나열 */
  gap: 100px; /* 아이템들 사이의 간격 */
}

.navbar-item {
  cursor: pointer;
  padding: 8px 15px; /* 각 아이템의 패딩 */
  transition: all 0.2s ease-in-out;
  border-radius: 30px; /* 호버/활성화 시 둥근 배경 */
  white-space: nowrap; /* 텍스트가 줄바꿈되지 않도록 */
}

.navbar-link {
  text-decoration: none;
  color: #555; /* 기본 텍스트 색상 */
  background-color: transparent;
  font-size: 16px;
  font-weight: 500;
  display: block; /* 링크 전체 영역 클릭 가능하게 */
  transition: color 0.2s ease-in-out;
}

/* 호버 상태 */
.navbar-item:hover {
  background-color: #f0f0f0; /* 호버 시 배경색 */
}

.navbar-item:hover .navbar-link {
  color: #3b82f6; /* 호버 시 텍스트 색상 */
}

/* 활성화된 아이템의 텍스트 색상을 파란색으로 변경 */
.navbar-item.active .navbar-link {
  color: #3b82f6; /* 파란색으로 변경 */
  font-weight: 600; /* 글자 굵기 강조 */
}

.navbar-item.active {
  background-color: #eff6ff; /* 활성화된 아이템 배경색 */
}

/* router-link-active 클래스 스타일 (Vue Router가 자동으로 추가) */
.navbar-link.router-link-active {
  color: #3b82f6;
  font-weight: 600;
}

/* 기존 flex-wrap: wrap 제거 */
@media (max-width: 768px) {
  .navbar {
    padding: 6px 12px; /* 패딩 더 줄임 */
  }
  .navbar-list {
    gap: 15px; /* 간격 줄임 */
    /* flex-wrap: wrap 삭제 */
  }
  .navbar-item {
    padding: 4px 8px; /* 패딩 더 줄임 */
  }
  .navbar-link {
    font-size: 13px; /* 폰트 크기 줄임 */
  }
}

@media (max-width: 480px) {
  .navbar {
    padding: 4px 8px; /* 패딩 더 줄임 */
  }
  .navbar-list {
    gap: 8px; /* 간격 더 줄임 */
  }
  .navbar-item {
    padding: 3px 6px; /* 패딩 더 줄임 */
  }
  .navbar-link {
    font-size: 11px; /* 폰트 크기 더 줄임 */
  }
}
</style>
