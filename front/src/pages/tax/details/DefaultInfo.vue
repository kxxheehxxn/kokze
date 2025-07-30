<template>
  <div class="tax-info">
    <!-- 안내 제목 -->
    <h2 class="section-title">🐘 세금 정보 서비스</h2>
    <p class="section-desc">
      2024년 귀속 연말정산 PDF 자료를 기반으로, 항목별 작년 금액과 세금 관련 안내를 제공합니다.
    </p>

    <!-- 안내 리스트 -->
    <ul class="info-list">
      <li>정보는 사용자가 홈택스 등에 제출한 자료를 바탕으로 하며, 실제와 다를 수 있습니다.</li>
      <li>금액·항목이 사실과 다를 경우 원천 기관(카드사·의료기관 등)에 확인하세요.</li>
      <li>절세 방안은 일반적인 안내이며, 개인 상황에 따라 적용 여부가 다를 수 있습니다.</li>
      <li>최종 정산 결과는 반드시 사용자가 직접 확인해야 합니다.</li>
    </ul>

    <!-- 버튼 -->
    <button class="info-button" @click="showModal = true">
      작년 연말정산 조회하기
    </button>

    <!-- 모달 -->
    <div v-if="showModal" class="popup-overlay">
      <div class="popup-content agreement-popup">
        <button class="popup-close" @click="closePopup">✕</button>

        <!-- 기본 동의 화면 -->
        <div v-if="step === 'agreement'">
          <h2 class="popup-title">개인(신용)정보 수집·이용 동의</h2>
          <p class="popup-desc">
            [필수] 전자금융거래 정보처리 동의<br />
            전자금융거래 서비스 제공·관리·개선 등을 목적으로 합니다.
          </p>

          <!-- 연도 선택 -->
          <section class="popup-section year-section">
            <label class="popup-label">연도 :</label>
            <select v-model="selectedYear" class="popup-select">
              <option disabled value="">연도를 선택하세요</option>
              <option v-for="year in yearOptions" :key="year" :value="year">{{ year }}</option>
            </select>
          </section>

          <!-- 동의 여부 -->
          <section class="popup-section agree-section">
            <p>위 개인정보 수집·이용에 동의하십니까?</p>
            <div class="radio-group">
              <label><input type="radio" value="Y" v-model="agree"> 동의함</label>
              <label><input type="radio" value="N" v-model="agree"> 동의안함</label>
            </div>
          </section>

          <div class="popup-actions">
            <button class="agree-btn" @click="startVerification">동의</button>
          </div>
        </div>

        <!-- 인증 진행중 화면 -->
        <div v-else-if="step === 'verifying'" class="center-content">
          <p class="verify-text">🔐 인증중입니다...<br />인증이 완료되면 "인증완료" 버튼을 눌러주세요.</p>
          <div class="popup-actions">
            <button class="close-btn" @click="closePopup">닫기</button>
            <button class="confirm-btn" @click="goToCompleted">인증완료</button>
          </div>
        </div>

        <!-- 인증 완료 화면 -->
        <div v-else-if="step === 'completed'" class="center-content">
          <p class="verify-complete">✅ 인증이 완료되었습니다.</p>
          <div class="popup-actions">
            <button class="close-btn" @click="closePopup">닫기</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "DefaultInfo",
    data() {
      return {
        showModal: false,
        step: "agreement", // agreement → verifying → completed
        selectedYear: "",
        yearOptions: [2022, 2023, 2024],
        agree: ""
      };
    },
    methods: {
      startVerification() {
        if (!this.selectedYear) {
          alert("연도를 선택해주세요.");
          return;
        }
        if (this.agree !== "Y") {
          alert("동의해야 진행 가능합니다.");
          return;
        }

        // 동의 → 인증 진행 화면으로 전환
        this.step = "verifying";

        // 여기서 인증 로직 실행 (카카오 인증 등)
        
        // setTimeout(() => {
        //   // 예시: 인증 완료 시
        //   this.step = "completed";
        // }, 2000);
      },
      goToCompleted() {
          this.step = 'completed'; // 인증완료 버튼 클릭 시 완료 화면
      },
      confirmVerification() {
        alert("인증이 완료되어 조회를 시작합니다.");
        this.closePopup();
      },
      closePopup() {
        this.showModal = false;
        this.step = "agreement"; // 초기화
      }
    }
  };
</script>

<style scoped>
.tax-info {
  font-family: 'Noto Sans KR', sans-serif;
  padding: 24px;
  background-color: #ffffff;
  color: #1f2937;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 12px;
}

.section-desc {
  font-size: 14px;
  margin-bottom: 12px;
  color: #4b5563;
}

.info-list {
  font-size: 13px;
  color: #4b5563;
  list-style-type: disc;
  padding-left: 20px;
  margin-bottom: 16px;
}

/* 안내 버튼 */
.info-button {
  width: 100%;
  padding: 12px;
  background-color: #3b82f6;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.info-button:hover {
  background-color: #2563eb;
}

/* 팝업 배경 */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 팝업 본문 */
.popup-content {
  background: white;
  width: 500px;
  max-height: 90%;
  overflow-y: auto;
  padding: 24px;
  border-radius: 12px;
  position: relative;
}
.popup-title {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
}

.popup-subtitle {
  text-align: center;
  font-size: 14px;
  margin-bottom: 8px;
}
.popup-desc {
  text-align: center;
  font-size: 13px;
  color: #4b5563;
}

/* 연도 선택 중앙 정렬 */
.year-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
}
.popup-label {
  font-weight: bold;
}
.popup-select {
  padding: 6px 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

/* 동의 여부 중앙정렬 */
.agree-section {
  text-align: center;
  margin-top: 16px;
}
.radio-group {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 8px;
}

/* 버튼 스타일 */
.popup-actions {
  margin-top: 24px;
  text-align: center;
}
.agree-btn {
  background: #3b82f6;
  color: white;
  padding: 10px 40px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  font-size: 14px;
}
.agree-btn:hover {
  background: #2563eb;
}

.popup-close {
  position: absolute;
  top: 12px;       /* 팝업 위쪽에서 12px */
  right: 12px;     /* 팝업 오른쪽에서 12px */
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #6b7280;  /* 회색 */
}

.popup-close:hover {
  color: #111827; /* 진한 회색으로 hover 효과 */
}
.center-content {
  text-align: center;
  padding: 20px;
}
.verify-text {
  font-size: 15px;
  color: #374151;
}
.verify-complete {
  font-size: 16px;
  font-weight: bold;
  color: #16a34a;
}
.popup-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 16px;
}
.close-btn {
  background: #6b7280;
  color: white;
  padding: 8px 20px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}
.confirm-btn {
  background: #3b82f6;
  color: white;
  padding: 8px 20px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}

</style>
