<template>
  <div class="ad-card">
    <div class="text-content-top">
      <h2 class="main-title">청년도약계좌, 아직도 신청하지 않으셨나요?</h2>

      <div class="info-section">
        <div class="info-item">
          <span class="label">전화 :</span>
          <span class="value">1397 > 바로연결번호 3번</span>
        </div>

        <div class="info-item">
          <span class="label">카카오톡 :</span>
          <span class="value">청년도약계좌 채팅상담 바로가기</span>
        </div>

        <div class="info-item application-info">
          <div>가입신청은 취급은행* 앱(App)을 통해 매월 비대면 신청</div>
          <div class="bank-list">* 국민, 기업, 농협, 신한, 우리, 하나, 경남, 광주, 대구, 부산, 전북</div>
        </div>
      </div>
    </div>

    <div class="bottom-section-wrapper">
      <div class="image-container">
        <img src="@/assets/images/AdImage.svg" alt="청년도약계좌 문의 일러스트" class="ad-image" />
      </div>

      <button class="learn-more-button" @click="goToDetail">
        자세히 알아보기
        <svg class="arrow-icon" viewBox="0 0 24 24" fill="none">
          <path
            d="M9 18L15 12L9 6"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
// export default {
//   name: 'YouthLeapAccount',
//   methods: {
//     goToDetail() {
//       console.log('자세히 알아보기 클릭');
//     },
//   },
const goToDetail = () => {
  console.log('자세히 알아보기 클릭');
};
</script>

<style scoped>
.ad-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 48px 48px 0px 48px;
  width: 100%; /* 부모의 너비를 꽉 채우도록 설정 */
  /* max-width는 제거하거나, 특정 최대 너비가 필요하다면 설정 */
  min-height: 400px; /* 적절한 최소 높이 설정 */
  margin: 0 auto; /* 중앙 정렬 유지 */
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
}

/* 상단 텍스트 콘텐츠 */
.text-content-top {
  width: 100%;
  margin-bottom: 32px;
}

.main-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 32px;
  line-height: 1.3;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  color: #475569;
  font-size: 16px;
}

.label {
  font-weight: 600;
  margin-right: 4px;
}

.value {
  flex: 1;
}

.application-info {
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
  margin-top: 8px;
}

.bank-list {
  font-size: 14px;
  color: #64748b;
  line-height: 1.5;
}

/* 하단 섹션 래퍼: 사진과 버튼이 좌우로 나뉨 */
.bottom-section-wrapper {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
  align-items: flex-end;
}

/* 이미지 컨테이너 스타일 */
.image-container {
  flex: 1; /* 남은 공간을 차지하도록 설정 */
  display: flex;
  justify-content: flex-start;
  background-color: transparent;
  align-items: flex-end;
  padding-right: 20px;
  /* 이미지의 고정 너비 230px를 제거하거나 max-width와 함께 사용 */
  /* 필요하다면 flex-basis로 최소 너비 지정 가능 */
}

/* 이미지 자체의 스타일 */
.ad-image {
  /* 이미지 컨테이너의 크기에 맞춰 유연하게 조절되도록 합니다. */
  /* 고정 width: 230px; 를 삭제하고 max-width: 100%만 남겨둡니다. */
  width: 250px; /* 이미지 크기 조정 */
  max-width: 100%;
  height: auto;
  display: block;
  background-color: transparent; /* 배경색 제거 */
  transform: translateY(20px);
}

/* 자세히 알아보기 버튼 스타일 */
.learn-more-button {
  background: #d8eaff;
  color: rgb(0, 0, 0);
  padding: 8px 36px;
  margin-bottom: 36px;
  border-radius: 12px;
  border: none;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);

  position: static; /* Flexbox 내에서 배치 */
}

.learn-more-button:hover {
  background: #2563eb;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.arrow-icon {
  width: 20px;
  height: 20px;
}

/* 반응형 디자인 */
@media (max-width: 992px) {
  .ad-card {
    padding: 32px 24px;
    min-height: auto;
    border-radius: 0 20px 20px 0; /* 반응형에서도 오른쪽만 둥근 모서리 유지 */
    width: 100%; /* 반응형에서도 부모 너비 채우도록 */
  }

  .text-content-top {
    margin-bottom: 24px;
    text-align: center;
  }
  .info-item {
    flex-direction: column;
    align-items: center;
  }
  .label {
    min-width: auto;
    text-align: center;
    margin-right: 0;
    margin-bottom: 4px;
  }
  .value {
    text-align: center;
  }
  .application-info {
    align-items: center;
  }
  .main-title {
    font-size: 24px;
    text-align: center;
  }

  .bottom-section-wrapper {
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }

  .image-container {
    width: 80%;
    height: auto;
    padding-right: 0;
    justify-content: center;
  }
  .ad-image {
    transform: translateY(0);
  }

  .learn-more-button {
    width: 100%;
    max-width: 300px;
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .ad-card {
    padding: 24px 16px;
  }
  .learn-more-button {
    padding: 14px 24px;
    font-size: 15px;
  }
}
</style>
