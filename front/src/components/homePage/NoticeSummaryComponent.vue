<template>
  <div class="notice-summary-component">
    <div class="top">
      <div class="bell-logo">
        <i class="fa-solid fa-bell"></i>
      </div>
      <h2 class="title">알려드립니다.</h2>
    </div>
    <div class="middle">
      <div class="content">
        <div class="date-info">
          <span class="date-text">2025년 00월 00일 </span>
          <span class="time-text">00시부터 00시까지</span>
        </div>
        <div class="notice-text">시스템 정기점검 예정입니다.</div>
        <div class="description">
          보다 안정된 서비스를 제공하기 위해 전자금융 서비스가<br />
          일시 중단 되오니 이용에 참고하시기 바랍니다.
        </div>
      </div>
    </div>
    <div class="bottom"></div>
  </div>
</template>

<script setup></script>

<style scoped>
.notice-summary-component {
  background-color: #dff9f2;
  width: 100%;
  height: 100%;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.top {
  display: flex;
  align-items: center;
  gap: 16px;
}

.bell-logo {
  border-radius: 50%;
  width: 50px;
  height: 50px;
  background-color: #3573ee;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bell-logo i {
  color: white;
  font-size: 20px;
}

.title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
  background-color: transparent;
}

.middle {
  flex: 1;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.date-info {
  font-size: 24px;
  font-weight: 700;
  color: #333;
}

.date-text {
  color: #333;
}

.time-text {
  color: #ff4444;
}

.notice-text {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.description {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .notice-summary-component {
    padding: 24px;
  }

  .date-info {
    font-size: 20px;
  }

  .notice-text {
    font-size: 20px;
  }

  .description {
    font-size: 14px;
  }
}
</style>
