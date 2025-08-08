<template>
  <div class="container">
    <div class="user-page">
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>사용자 정보를 불러오는 중...</p>
      </div>
      <div v-else-if="error" class="error">
        <p>{{ error }}</p>
        <button @click="loadUserData" class="retry-btn">다시 시도</button>
      </div>
      <div v-else class="user-card">
        <div class="user-mbti">{{ user.mbti || '미입력' }}</div>
        <div class="user-name">
          {{ user.name || '사용자' }} 님
          <span class="user-point" @click="goTo('/user/point')"
            >{{ point.toLocaleString() }} P</span
          >
        </div>
        <ul class="user-menu">
          <li @click="goTo('/user/asset')">자산 정보 수정</li>
          <li @click="goTo('/user/mbti')">나의 금융 MBTI 수정</li>
          <li @click="goTo('/user/password')">비밀번호 수정</li>
          <li @click="goTo('/user/withdraw')">회원 탈퇴</li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserInfo, getUserPoints } from '@/api/userApi';
const user = ref({ name: '', mbti: '', user_id: null });
const point = ref(0);
const loading = ref(true);
const error = ref(null);
const router = useRouter();
function goTo(path) {
  router.push(path).catch((err) => {
    console.error('Navigation error:', err);
  });
}
async function loadUserData() {
  loading.value = true;
  error.value = null;
  try {
    const [userInfo, userPoints] = await Promise.all([
      getUserInfo(),
      getUserPoints(),
    ]);
    user.value = userInfo;
    point.value = userPoints;
  } catch (err) {
    console.error('Failed to load user data:', err);
    error.value = '사용자 정보를 불러올 수 없습니다. 다시 시도해주세요.';
    user.value = { name: '사용자', mbti: '미입력', user_id: null };
    point.value = 0;
  } finally {
    loading.value = false;
  }
}
onMounted(() => {
  loadUserData();
});
</script>
<style scoped>
.container {
  background-color: #fbfbfb;
}
.user-page {
  display: flex;
  justify-content: center;
  padding-top: 60px;
  padding-bottom: 30px;
  margin: 0px 30px;
}
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  color: #666;
}
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3573ee;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
.error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  color: #e74c3c;
}
.retry-btn {
  margin-top: 16px;
  padding: 8px 16px;
  background: #3573ee;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.retry-btn:hover {
  background: #3573ee;
}
.user-card {
  width: 920px;
  height: 570px;
  background-color: #fff;
  border-radius: 28px;
  padding: 3rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.user-mbti {
  background-color: #fff;
  color: #222;
  font-size: 20px;
  margin-bottom: 24px;
  font-weight: 400;
  text-align: left;
}
.user-name {
  background-color: #fff;
  font-size: 45px;
  font-weight: bold;
  margin-bottom: 48px;
  display: flex;
  align-items: center;
  gap: 18px;
}
.user-point {
  background: #e8f0ff;
  color: #3573ee;
  border-radius: 28px;
  padding: 8px 24px;
  font-size: 23px;
  font-weight: 500;
  margin-left: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.user-point:hover {
  background: #ccdcfb;
  color: #0d3a95;
  transform: translateY(-2px);
}
.user-menu {
  background-color: #fff;
  list-style: none;
  padding: 0;
  margin: 0;
  color: #222;
  font-size: 22px;
}
.user-menu li {
  background-color: #fff;
  margin-bottom: 32px;
  cursor: pointer;
  font-weight: 400;
}
.user-menu li:last-child {
  font-size: 16px;
  color: #bdbdbd;
}
.user-id {
  font-size: 14px;
  color: #888;
  text-align: center;
  margin-top: 16px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 8px;
}
@media (max-width: 1024px) and (orientation: portrait) {
  .user-card {
    min-height: 80vh; /* 화면 높이의 80% 이상 확보 */
  }
}
</style>
