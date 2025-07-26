<template>
  <div class="tax-page">
    <!-- Outer white container -->
    <div class="tax-wrapper">
      <!-- Grid and info side by side -->
      <div class="tax-content">
        <!-- Left: Tax cards grid -->
        <div class="tax-grid">
          <div v-for="(label, idx) in taxItems" :key="idx" class="tax-card">
            <p class="tax-label">{{ label }}</p>
            <p class="tax-amount">0원</p>
          </div>
        </div>

        <!-- Right: Info box -->
        <div class="tax-info">
          <h2 class="info-title">세금 정보 서비스 🐘</h2>
          <p class="info-subtitle">이용 전 반드시 확인해주세요!</p>
          <ul class="info-list">
            <li>본 서비스는 2024년 귀속 연말정산 PDF 자료를 기반으로, 항목별 작년 금액과 함께 세금 관련 도메인 지식 및 절세 방안을 요약하여 안내해드립니다.</li>
            <li>불러온 정보는 사용자가 홈택스 등에 제출한 자료를 바탕으로 하며, 실제와 다를 수 있습니다.</li>
            <li>→ 금액이나 항목 내용이 사실과 다를 경우, 반드시 원천 기관(예: 카드사, 의료기관 등)에 문의해주세요.</li>
            <li>각 항목별 절세 방안은 일반적인 정보 제공을 목적으로 하며, 개인의 세무 상황에 따라 적용 여부가 달라질 수 있습니다.</li>
            <li>본 서비스는 사용자의 편의를 위한 보조 도구이며, 최종 정산 결과는 사용자가 직접 확인해야 합니다.</li>
          </ul>
          <button class="info-button">작년 연말정산 조회하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxPage',
  data() {
    return {
      taxItems: [
        '소득기준 초과 부양가족','건강/고용보험','국민연금',
        '보험료','의료비','교육비',
        '신용카드','직불카드 등','현금영수증',
        '개인연금저축/연금계좌','주택자금/월세액','주택마련저축',
        '장기집합투자증권저축/벤처기업투자신탁','기부금'
      ]
    }
  }
}
</script>

<style scoped>
.tax-page {
  background-color: #ffffff;
  padding: 40px;
  min-height: 100vh;
  box-sizing: border-box;
}
.tax-wrapper {
  background-color: #ffffff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  max-width: 1280px;
  margin: 0 auto;
}
.tax-content {
    background-color: #ffffff;
  display: flex;
  gap: 24px;
  align-items: flex-start;
}
.tax-grid {
    background-color: #ffffff;
  flex: 1.618; /* 왼쪽 그리드가 오른쪽 안내 박스보다 1.618배 넓게 */
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.tax-card {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 16px;
  text-align: center;
}
.tax-label,
.tax-amount {
    background-color: #ffffff;
  margin: 0;
}
.tax-amount {
    background-color: #ffffff;
  font-size: 18px;
  font-weight: bold;
  color: #111827;
}
.tax-info {
  flex: 1; /* 오른쪽 안내 박스는 왼쪽의 1.618 대비 1 비율 */
  min-width: 280px; /* 너무 작아지지 않도록 최소 너비 지정 */
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 20px;
  box-sizing: border-box;
}
.info-title,
.info-subtitle {

  margin: 0;
  background-color: transparent;
}
.info-title {
    background-color: #ffffff;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
  color: #111827;
}
.info-subtitle {
    background-color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #1f2937;
}
.info-list {
    background-color: #ffffff;
  list-style: disc;
  padding-left: 16px;
  margin-bottom: 16px;
  font-size: 13px;
  color: #4b5563;
  line-height: 1.5;
}
.info-list li {
  margin-bottom: 8px;
  background-color: transparent;
}
.info-button {
  width: 100%;
  padding: 12px;
  background-color: #3b82f6;
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.info-button:hover {
  background-color: #2563eb;
}
</style>