<template>
  <h2 class="section-title"> 🐘 : 세금 정보 서비스</h2>
  <p class="section-desc">2024년 귀속 연말정산 PDF 자료를 기반으로, 항목별 작년 금액과 세금 관련 안내를 제공합니다.</p>
  <ul class="info-list">
    <li>정보는 사용자가 홈택스 등에 제출한 자료를 바탕으로 하며, 실제와 다를 수 있습니다.</li>
    <li>금액·항목이 사실과 다를 경우 원천 기관(카드사·의료기관 등)에 확인하세요.</li>
    <li>절세 방안은 일반적인 안내이며, 개인 상황에 따라 적용 여부가 다를 수 있습니다.</li>
    <li>최종 정산 결과는 반드시 사용자가 직접 확인해야 합니다.</li>
  </ul>
  <button class="info-button" @click="showModal = true">작년 연말정산 조회하기</button>
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
            <option v-for="year in yearOptions" :key="year" :value="year">{{ year }}</option>
          </select>
        </section>
        <section class="popup-section agree-section">
          <p>위 개인정보 수집·이용에 동의하십니까?</p>
          <div class="radio-group">
            <label><input type="radio" value="Y" v-model="agree" /> 동의함</label>
            <label><input type="radio" value="N" v-model="agree" /> 동의안함</label>
          </div>
        </section>
        <div class="popup-actions">
          <button class="agree-btn" @click="startVerification">동의</button>
        </div>
      </div>
      <div v-else-if="step === 'verifying'" class="center-content">
        <p class="verify-text">🔐: 인증중입니다...<br />인증이 완료되면 "인증완료" 버튼을 눌러주세요.</p>
        <div class="popup-actions">
          <button class="close-btn" @click="closePopup">닫기</button>
          <button class="confirm-btn" @click="goToCompleted">인증완료</button>
        </div>
      </div>
      <div v-else-if="step === 'completed'" class="center-content">
        <p class="verify-complete">✅: 인증이 완료되었습니다.</p>
        <div class="popup-actions">
          <button class="close-btn" @click="closePopup">닫기</button>
        </div>
      </div>
    </div>
  </div>
  <!-- ▼▼▼ 하단: 간이 예상세액 섹션 ▼▼▼ -->
  <section v-if="estimate" class="estimate-box">
    <h3 class="estimate-title">📌 간이 예상세액</h3>

    <div class="salary-input">
      <label>총급여(연봉):</label>
      <input
        type="number"
        v-model.number="salary"
        @change="recomputeEstimate"
        min="0"
        step="100000"
        placeholder="예: 30000000"
      />
      <small>※ 필요시 수정 가능 (원 단위)</small>
    </div>

    <div class="estimate-grid">
      <div>총급여</div><div>{{ fmt(estimate.salary) }}</div>
      <div>근로소득공제</div><div>- {{ fmt(estimate.wageDeduction) }}</div>
      <div class="sep"></div><div class="sep"></div>
      <div>근로소득금액</div><div>{{ fmt(estimate.earnedIncome) }}</div>

      <div class="sep"></div><div class="sep"></div>
      <div>국민연금 소득공제</div><div>- {{ fmt(estimate.pension) }}</div>
      <div>건강보험 소득공제</div><div>- {{ fmt(estimate.health) }}</div>
      <div>교육비 소득공제</div><div>- {{ fmt(estimate.education) }}</div>
      <div>의료비(총급여 3% 초과분)</div><div>- {{ fmt(estimate.medicalDeductible) }}</div>
      <div>신용/체크/현금 공제</div><div>- {{ fmt(estimate.cardDeduction) }}</div>

      <div class="sep"></div><div class="sep"></div>
      <div>소득공제 합계</div><div>- {{ fmt(estimate.totalDeductions) }}</div>
      <div>과세표준</div><div>{{ fmt(estimate.taxableBase) }}</div>
      <div>산출세액</div><div>{{ fmt(estimate.incomeTax) }}</div>
      <div>결정세액(세액공제 미반영)</div><div><b>{{ fmt(estimate.finalTax) }}</b></div>
    </div>

    <p class="estimate-note">
      ※ 간이 계산입니다. 근로소득세액공제/자녀세액공제 등 세액공제는 미반영되어 실제와 차이날 수 있습니다.
    </p>
  </section>
</template>
<script>
import { userAuthStore } from '@/stores/auth'   // ✅ 추가
import { storeToRefs } from 'pinia';
  export default {
  name: "DefaultInfo",
  emits: ['update-tax-data'], 
    data() {
      return {
        showModal: false,
        step: 'agreement',
        selectedYear: '',
        yearOptions: [2022, 2023, 2024],
        agree: '',
        isVerified: false,
        loading: false,
        rawKakaoData: null,
        // ▼ 예상세액 계산용
        salary: 30000000,         // 총급여(사용자 수정 가능)
        summaryMap: null,         // 백엔드 요약 맵 (code -> 금액)
        estimate: null            // 계산 결과 객체
      };
    },
  methods: {
      fmt(n) {
        const v = typeof n === 'number' ? n : Number(String(n).replace(/[^\d.-]/g, '')) || 0
        return v.toLocaleString('ko-KR') + '원'
      },
      num(v) {
        // "1,234원" 또는 숫자 → number 로
        return typeof v === 'number'
          ? v
          : Number(String(v).replace(/[^\d.-]/g, '')) || 0
      },
        // 근로소득공제(간단 공식) – 2024 기준 구간 반영
      calcWageDeduction(sal) {
        let d = 0
        if (sal <= 5000000) {
          d = sal * 0.7
        } else if (sal <= 15000000) {
          d = 3500000 + (sal - 5000000) * 0.4
        } else if (sal <= 45000000) {
          d = 7500000 + (sal - 15000000) * 0.15
        } else if (sal <= 100000000) {
          d = 12000000 + (sal - 45000000) * 0.1
        } else {
          d = 17500000 + (sal - 100000000) * 0.05
        }
        // (상한은 급여별로 존재하나 간이계산이므로 생략)
        return Math.max(0, Math.floor(d))
      },

      // 과세표준 → 산출세액(2024 누진세율)
      calcIncomeTax(base) {
        const b = Math.max(0, Math.floor(base))
        if (b <= 12000000) return Math.floor(b * 0.06)
        if (b <= 46000000) return Math.floor(720000 + (b - 12000000) * 0.15)
        if (b <= 88000000) return Math.floor(5820000 + (b - 46000000) * 0.24)
        if (b <= 150000000) return Math.floor(15900000 + (b - 88000000) * 0.35)
        if (b <= 300000000) return Math.floor(37600000 + (b - 150000000) * 0.38)
        if (b <= 500000000) return Math.floor(94600000 + (b - 300000000) * 0.40)
        if (b <= 1000000000) return Math.floor(174600000 + (b - 500000000) * 0.42)
        return Math.floor(384600000 + (b - 1000000000) * 0.45)
      },

      // 카드 공제 (총급여 25% 초과분: 카드 15%, 체크/현금 30%, 총합 한도 300만원)
      calcCardDeduction({ salary, credit, debit, cash }) {
        const threshold = salary * 0.25
        const totalUse = credit + debit + cash
        const over = Math.max(0, totalUse - threshold)
        if (over <= 0) return 0

        // 초과분부터 신용카드 15% → 남으면 체크/현금 30%
        const creditPortion = Math.min(over, credit)
        const otherPortion = Math.max(0, over - creditPortion) // 체크+현금
        const raw =
          Math.floor(creditPortion * 0.15) +
          Math.floor(Math.min(otherPortion, debit + cash) * 0.30)

        return Math.min(raw, 3000000) // 총 한도 300만원
      },

      computeEstimate() {
        if (!this.summaryMap) return

        const sal = this.salary

        // 코드 → 금액 취득 (없으면 0)
        const pension   = this.num(this.summaryMap['1']) // 국민연금
        const health    = this.num(this.summaryMap['0']) // 건강보험
        const medical   = this.num(this.summaryMap['3']) // 의료비 총액(본인부담)
        const education = this.num(this.summaryMap['4']) // 교육비
        const credit    = this.num(this.summaryMap['5']) // 신용카드
        const debit     = this.num(this.summaryMap['6']) // 직불/체크 등
        const cash      = this.num(this.summaryMap['7']) // 현금영수증

        const wageDeduction   = this.calcWageDeduction(sal)
        const earnedIncome    = Math.max(0, sal - wageDeduction)

        // 의료비: 총급여 3% 초과분만 공제
        const medThreshold       = Math.floor(sal * 0.03)
        const medicalDeductible  = Math.max(0, medical - medThreshold)

        const cardDeduction = this.calcCardDeduction({ salary: sal, credit, debit, cash })

        const totalDeductions =
          pension + health + education + medicalDeductible + cardDeduction

        const taxableBase = Math.max(0, earnedIncome - totalDeductions)
        const incomeTax   = this.calcIncomeTax(taxableBase)
        const finalTax    = incomeTax // (세액공제 미반영)

        this.estimate = {
          salary: sal,
          wageDeduction,
          earnedIncome,
          pension,
          health,
          education,
          medicalDeductible,
          cardDeduction,
          totalDeductions,
          taxableBase,
          incomeTax,
          finalTax
        }
      },

      recomputeEstimate() {
        // 총급여 변경 시 재계산
        this.computeEstimate()
      },

      async startVerification() {
        if (!this.selectedYear) return alert('연도를 선택해주세요.');
        if (this.agree !== 'Y') return alert('동의해야 진행 가능합니다.');
        this.step = 'verifying';

        try {
          const { userId } = storeToRefs(userAuthStore());
          const uid = userId.value;
          if (!uid) {
            alert('로그인 후 이용해주세요.');
            this.step = 'agreement';
            return;
          }

          const res = await fetch(
            `/api/tax/auth?userId=${encodeURIComponent(uid)}&year=${encodeURIComponent(this.selectedYear)}`,
            {
              method: 'GET',
              credentials: 'include',               // ★ 쿠키 포함 (세션/로그인 유지)
              headers: { Accept: 'application/json' }
            }
          );
          if (!res.ok) {
            console.error('서버 오류 응답:', await res.text());
            alert('서버 오류 발생');
            this.step = 'agreement';
            return;
          }

          const ct = res.headers.get('content-type') || '';
          if (!ct.includes('application/json')) {
            const html = await res.text();
            console.error('서버가 JSON이 아닌 응답을 반환:', res.status, ct, html.slice(0, 300));
            alert('서버가 JSON이 아닌 응답을 반환했습니다. (로그인이 필요한지 확인)');
            this.step = 'agreement';
            return;
          }
          const raw = await res.json();
          console.log("1차 파싱 결과:", raw);

          // 이게 다시 문자열이라면?
          const data = typeof raw === 'string' ? JSON.parse(raw) : raw;

          setTimeout(() => {
            if (data.result?.code === 'CF-00000') {
              this.isVerified = true;
              this.rawKakaoData = data.data || [];
              this.step = 'completed';
              this.fetchTaxSummary();

            } else {
              alert(`인증 실패: ${data.result?.message || '알 수 없는 오류'}`);
              this.step = 'verifying';
              this.isVerified = false;
            }
          }, 10000); 
        } catch (e) {
          console.error(e);
          alert('서버 요청 중 오류 발생');
          this.isVerified = false;
          this.step = 'agreement';
        }
      },
      async fetchTaxSummary() {
        if (!this.isVerified) return alert('인증을 먼저 완료해주세요.');
        this.loading = true;
        try {
          const { userId } = storeToRefs(userAuthStore());
          const uid = userId.value;

          const res = await fetch('/api/taxinfo/save-and-summary', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              userId: uid,
              year: this.selectedYear,
              data: this.rawKakaoData || []
            })
          });
          if (!res.ok) throw new Error(await res.text());
          const rawSum = await res.json();
          const summary = typeof rawSum === 'string' ? JSON.parse(rawSum) : rawSum;
          this.summaryMap = summary;  
          console.log("세금 요약 결과:", summary);
          // 부모(TaxPage)로 요약 전달
          const rows = Object.entries(summary).map(([code, total]) => ({
            code,
            total,
            totalLabel: Number(total).toLocaleString('ko-KR') + '원',
          }));
          console.log('[child] emit update-tax-data', rows)
          this.$emit('update-tax-data', rows);
          this.computeEstimate()
          this.closePopup();
        } catch (err) {
          console.error('세금 정보 조회 오류:', err);
          alert('세금 정보 조회에 실패했습니다.');
        } finally {
          this.loading = false;
        }
      },
      goToCompleted() {
        if (!this.isVerified) {
          alert('아직 인증이 완료되지 않았습니다.');
          return;
        }
        this.step = 'completed';
      },
      confirmVerification() {
        alert("인증이 완료되어 조회를 시작합니다.");
        this.closePopup();
      },
      closePopup() {
        this.showModal = false;
        this.step = "agreement";
      }
    }
  };
</script>
<style >
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
  margin-top: 24px;
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
/* ── 간이 예상세액 UI ─────────────────────────── */
.estimate-box {
  margin-top: 18px; padding: 16px;
  border: 1px solid #e5e7eb; border-radius: 12px;
}
.estimate-title { font-weight: 700; margin-bottom: 10px; }

.salary-input { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.salary-input input { width: 200px; padding: 6px 8px; border: 1px solid #ddd; border-radius: 6px; }

.estimate-grid {
  display: grid; grid-template-columns: 1fr 1fr;
  row-gap: 6px; column-gap: 8px; font-size: 14px;
}
.estimate-grid .sep {
  grid-column: 1 / span 2; height: 1px; background: #eee; margin: 4px 0;
}
.estimate-note { font-size: 12px; color: #6b7280; margin-top: 8px; }
</style>