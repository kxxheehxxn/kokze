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
  gap: 18px; /* 컴포넌트 내부 요소 간의 간격 */
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
  /* flex-shrink: 0; // 크기가 줄어들지 않도록 고정 - 이 속성을 제거하여 벨 로고가 줄어들 수 있도록 함 */
}

.bell-logo i {
  color: white;
  font-size: 20px;
}

.title {
  font-size: 16px; /* 1023px 이상에서 폰트 크기 더 줄임 */
  font-weight: 600;
  color: #333;
  margin: 0;
  background-color: transparent;
  flex-grow: 1; /* 남은 공간을 채우도록 설정 */
  min-width: 0; /* 내용이 넘칠 경우 줄어들 수 있도록 허용 */
  word-break: break-word; /* 긴 단어가 영역을 벗어나지 않도록 */
}

/* 새로 추가된 spacer에 flex-grow를 적용합니다. */
.spacer {
  width: 20px; /* spacer의 너비를 설정 */
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
  font-size: 17px; /* 1023px 이상에서 폰트 크기 더 줄임 */
  font-weight: 700;
  color: #333;
  /* white-space: nowrap; // 줄바꿈 없이 한 줄로 표시 - 제거 */
  /* overflow: hidden; // 내용이 넘칠 경우 숨김 - 제거 */
  /* text-overflow: ellipsis; // 숨겨진 내용에 ... 표시 - 제거 */
  word-break: break-word; /* 긴 단어가 영역을 벗어나지 않도록 */
}

.date-text {
  color: #333;
}

.time-text {
  color: #ff4444;
}

.notice-text {
  font-size: 17px; /* 1023px 이상에서 폰트 크기 더 줄임 */
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
  /* white-space: nowrap; // 줄바꿈 없이 한 줄로 표시 - 제거 */
  /* overflow: hidden; // 내용이 넘칠 경우 숨김 - 제거 */
  /* text-overflow: ellipsis; // 숨겨진 내용에 ... 표시 - 제거 */
  word-break: break-word; /* 긴 단어가 영역을 벗어나지 않도록 */
}

.description {
  font-size: 13px; /* 1023px 이상에서 폰트 크기 더 줄임 */
  color: #666;
  line-height: 1.5;
  word-break: keep-all; /* 한글 단어 단위로 줄바꿈 */
  overflow-wrap: break-word; /* 긴 단어가 영역을 벗어나지 않도록 */
}

/* 1024px 이하 (데스크탑 -> 태블릿 전환 지점) */
@media (max-width: 1024px) {
  .notice-summary-component {
    padding: 18px; /* 기존 패딩 유지 */
    gap: 20px; /* 기존 간격 유지 */
  }

  .bell-logo {
    width: 45px;
    height: 45px;
  }

  .bell-logo i {
    font-size: 18px;
  }

  .title {
    font-size: 17px; /* 폰트 크기 조정 */
  }

  .date-info {
    font-size: 18px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .notice-text {
    font-size: 18px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .description {
    font-size: 14px; /* 폰트 크기 조정 */
  }
}

/* 768px 이하 (태블릿 -> 모바일 전환 지점) */
@media (max-width: 768px) {
  .notice-summary-component {
    padding: 16px;
    gap: 12px;
  }

  .content {
    gap: 20px; /* 컴포넌트 내부 요소 간의 간격 */
  }

  .bell-logo {
    width: 40px;
    height: 40px;
  }

  .bell-logo i {
    font-size: 16px;
  }

  .title {
    font-size: 15px; /* 폰트 크기 조정 */
  }

  .date-info {
    font-size: 16px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .notice-text {
    font-size: 16px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .description {
    font-size: 13px; /* 폰트 크기 조정 */
  }
}

/* 480px 이하 (모바일) */
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
    font-size: 13px; /* 폰트 크기 조정 */
  }

  .date-info {
    font-size: 14px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .notice-text {
    font-size: 14px; /* 폰트 크기 조정 */
    /* white-space, overflow, text-overflow 제거 */
  }

  .description {
    font-size: 12px; /* 폰트 크기 조정 */
  }
}
</style>
