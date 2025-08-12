<template>
  <h2 class="section-title">🐘 세금 정보 서비스</h2>
  <p class="section-desc">
    2024년 귀속 연말정산 PDF 자료를 기반으로, 항목별 작년 금액과 세금 관련
    안내를 제공합니다.
  </p>
  <ul class="info-list">
    <li>
      정보는 사용자가 홈택스 등에 제출한 자료를 바탕으로 하며, 실제와 다를 수
      있습니다.
    </li>
    <li>
      금액·항목이 사실과 다를 경우 원천 기관(카드사·의료기관 등)에 확인하세요.
    </li>
    <li>
      절세 방안은 일반적인 안내이며, 개인 상황에 따라 적용 여부가 다를 수
      있습니다.
    </li>
    <li>최종 정산 결과는 반드시 사용자가 직접 확인해야 합니다.</li>
  </ul>
  <button class="info-button" @click="showModal = true">
    작년 연말정산 조회하기
  </button>
  <div v-if="showModal" class="popup-overlay">
    <div class="popup-content agreement-popup">
      <button class="popup-close" @click="closePopup">✕</button>
      <div v-if="step === 'agreement'">
        <h2 class="popup-title">개인(신용)정보 수집·이용 동의</h2>
        <p class="popup-desc">
          [필수] 전자금융거래 정보처리 동의<br />
          전자금융거래 서비스 제공·관리·개선 등을 목적으로 합니다.
        </p>
        <section class="popup-section year-section">
          <label class="popup-label">연도 :</label>
          <select v-model="selectedYear" class="popup-select">
            <option disabled value="">연도를 선택하세요</option>
            <option v-for="year in yearOptions" :key="year" :value="year">
              {{ year }}
            </option>
          </select>
        </section>
        <section class="popup-section agree-section">
          <p>위 개인정보 수집·이용에 동의하십니까?</p>
          <div class="radio-group">
            <label
              ><input type="radio" value="Y" v-model="agree" /> 동의함</label
            >
            <label
              ><input type="radio" value="N" v-model="agree" /> 동의안함</label
            >
          </div>
        </section>
        <div class="popup-actions">
          <button class="agree-btn" @click="startVerification">동의</button>
        </div>
      </div>
      <div v-else-if="step === 'verifying'" class="center-content">
        <p class="verify-text">
          🔐 인증중입니다...<br />인증이 완료되면 "인증완료" 버튼을 눌러주세요.
        </p>
        <div class="popup-actions">
          <button class="close-btn" @click="closePopup">닫기</button>
          <button class="confirm-btn" @click="goToCompleted">인증완료</button>
        </div>
      </div>
      <div v-else-if="step === 'completed'" class="center-content">
        <p class="verify-complete">✅ 인증이 완료되었습니다.</p>
        <div class="popup-actions">
          <button class="close-btn" @click="closePopup">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { userAuthStore } from '@/stores/auth';
import { storeToRefs } from 'pinia';
export default {
  name: 'DefaultInfo',
  data() {
    return {
      showModal: false,
      step: 'agreement',
      selectedYear: '',
      yearOptions: [2022, 2023, 2024],
      agree: '',
      isVerified: false,
    };
  },
  methods: {
    async startVerification() {
      if (!this.selectedYear) {
        alert('연도를 선택해주세요.');
        return;
      }
      if (this.agree !== 'Y') {
        alert('동의해야 진행 가능합니다.');
        return;
      }
      this.step = 'verifying';
      try {
        const authStore = userAuthStore();
        const { userId } = storeToRefs(userAuthStore());
        const uid = userId.value; // :흰색_확인_표시: ref에서 값 꺼내기
        if (!userId.value) {
          alert('로그인 후 이용해주세요.');
          this.step = 'agreement';
          return;
        }
        const res = await fetch(
          `/tax/auth?userId=${encodeURIComponent(uid)}&year=${this.selectedYear}`
        );
        if (!res.ok) {
          const text = await res.text(); // JSON 아닌 경우 대비
          console.error('서버 오류 응답:', text);
          alert('서버 오류 발생');
          this.step = 'agreement';
          return;
        }
        const data = await res.json();
        console.log('카카오 인증 응답:', data);
        if (data.result?.code === 'CF-00000') {
          alert('카카오 인증 성공!');
          this.isVerified = true;
          this.$emit('update-tax-data', data.data);
        } else {
          alert(`인증 실패: ${data.result?.message || '알 수 없는 오류'}`);
          this.step = 'verifying';
          this.isVerified = false;
        }
        // 3초마다 인증 상태 확인
      } catch (err) {
        console.error(err);
        alert('서버 요청 중 오류 발생');
        this.isVerified = false;
        this.step = 'agreement';
      }
    },
    goToCompleted() {
      if (!this.isVerified) {
        alert('인증을 완료해주세요.'); // :흰색_확인_표시: 인증 안 됐으면 알림
        return; // 화면 유지
      }
      this.step = 'completed'; // :흰색_확인_표시: 인증 완료 시에만 완료 화면으로 이동
    },
    closePopup() {
      this.showModal = false;
      this.step = 'agreement';
    },
  },
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
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}
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
  top: 12px;
  right: 12px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #6b7280;
}
.popup-close:hover {
  color: #111827;
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
