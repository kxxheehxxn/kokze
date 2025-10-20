<template>
  <div class="notice-summary-component">
    <div class="top">
      <div class="bell-logo">
        <i class="fa-solid fa-bell"></i>
      </div>
      <h2 class="title">알려드립니다.</h2>
      <div class="spacer"></div>
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
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  background-color: transparent;
  flex-grow: 1;
  min-width: 0;
  word-break: break-word;
}
.spacer {
  width: 20px;
}
.middle {
  flex: 1;
}
.content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.date-info {
  font-size: 17px;
  font-weight: 700;
  color: #333;
  word-break: break-word;
}
.date-text {
  color: #333;
}
.time-text {
  color: #ff4444;
}
.notice-text {
  font-size: 17px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
  word-break: break-word;
}
.description {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  word-break: keep-all;
  overflow-wrap: break-word;
}
@media (max-width: 1024px) {
  .notice-summary-component {
    padding: 18px;
    gap: 20px;
  }
  .bell-logo {
    width: 45px;
    height: 45px;
  }
  .bell-logo i {
    font-size: 18px;
  }
  .title {
    font-size: 17px;
  }
  .date-info {
    font-size: 18px;
  }
  .notice-text {
    font-size: 18px;
  }
  .description {
    font-size: 14px;
  }
}
@media (max-width: 768px) {
  .notice-summary-component {
    padding: 16px;
    gap: 12px;
  }
  .content {
    gap: 20px;
  }
  .bell-logo {
    width: 40px;
    height: 40px;
  }
  .bell-logo i {
    font-size: 16px;
  }
  .title {
    font-size: 15px;
  }
  .date-info {
    font-size: 16px;
  }
  .notice-text {
    font-size: 16px;
  }
  .description {
    font-size: 13px;
  }
}
@media (max-width: 480px) {
  .notice-summary-component {
    padding: 12px;
    gap: 10px;
  }
  .bell-logo {
    width: 35px;
    height: 35px;
  }
  .bell-logo i {
    font-size: 14px;
  }
  .title {
    font-size: 13px;
  }
  .date-info {
    font-size: 14px;
  }
  .notice-text {
    font-size: 14px;
  }
  .description {
    font-size: 12px;
  }
}
</style>