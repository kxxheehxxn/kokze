<template>
  <div class="user-page">
    <div class="user-card">
      <div class="user-mbti">{{ user.mbti }}</div>
      <div class="user-name">
        {{ user.name }} 님
        <span class="user-point">{{ point.toLocaleString() }} P</span>
      </div>
      <ul class="user-menu">
        <li @click="goTo('/user/asset')">자산 정보 수정</li>
        <li @click="goTo('/user/mbti')">나의 금융 MBTI 수정</li>
        <li @click="goTo('/user/password')">비밀번호 수정</li>
        <li @click="goTo('/user/withdraw')">회원 탈퇴</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserInfo, getUserPoints } from '@/api/userApi';

const user = ref({ name: '', mbti: '', user_id: null });
const point = ref(0);
const router = useRouter();

function goTo(path) {
  router.push(path).catch((err) => {
    console.error('Navigation error:', err);
    // TODO: optionally show a user-facing notification
  });
}

onMounted(async () => {
  try {
    const [userInfo, userPoints] = await Promise.all([getUserInfo(), getUserPoints()]);
    user.value = userInfo;
    point.value = userPoints;
  } catch (error) {
    console.error('Failed to load user data:', error);
    // Handle error appropriately
  }
});
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  background: #f6f6f6;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}
.user-card {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  margin: 48px auto 0 auto;
  padding: 64px 80px;
  width: 70vw;
  max-width: 1100px;
  min-width: 400px;
  border: 4px solid #189eff;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.user-mbti {
  background-color: #fff;
  color: #222;
  font-size: 24px;
  margin-bottom: 24px;
  font-weight: 400;
  text-align: left;
}
.user-name {
  background-color: #fff;
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 48px;
  display: flex;
  align-items: center;
  gap: 18px;
}
.user-point {
  background: #f6f6f6;
  color: #888;
  border-radius: 18px;
  padding: 8px 24px;
  font-size: 28px;
  font-weight: 500;
  margin-left: 12px;
}
.user-menu {
  background-color: #fff;
  list-style: none;
  padding: 0;
  margin: 0;
  color: #222;
  font-size: 24px;
}
.user-menu li {
  background-color: #fff;
  margin-bottom: 32px;
  cursor: pointer;
  font-weight: 400;
}
.user-menu li:last-child {
  color: #bdbdbd;
}
</style>
