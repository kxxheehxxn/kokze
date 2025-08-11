<template>
  <div class="home-to-tax-card">
    <div class="card-header">
      <div>2025년 연말정산</div>
      <h3 class="card-title">세금 관리</h3>
    </div>
    <div class="card-content">
      <img src="@/assets/images/duty_logo.svg" alt="세금 관리 로고" />
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
</script>
<style scoped>
.home-to-tax-card {
  background-color: #0d3a95;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  height: 100%;
  color: white;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(37, 99, 235, 0.3);
  display: flex;
  flex-direction: column;
}
.card-header {
  margin-bottom: 20px;
}
.card-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  color: white;
}
.card-content {
  height: 150px;
  width: 100%;
  display: flex;
  justify-content: center;
}
.card-header,
.card-content {
  position: relative;
  z-index: 1;
}
@media (max-width: 1024px) {
  .home-to-tax-card {
    padding: 20px;
  }
  .card-title {
    font-size: 18px;
  }
  .card-content {
    height: 120px;
    width: 100%;
  }
}
/* 반응형 디자인 */
@media (max-width: 768px) {
  .home-to-tax-card {
    padding: 20px;
  }
  .card-title {
    font-size: 18px;
  }
  .card-content {
    height: 100px;
    width: 100%;
  }
}
@media (max-width: 480px) {
  .home-to-tax-card {
    padding: 16px;
  }
  .card-title {
    font-size: 16px;
  }
  .card-content {
    height: 50px;
    width: 100%;
  }
}
</style>
