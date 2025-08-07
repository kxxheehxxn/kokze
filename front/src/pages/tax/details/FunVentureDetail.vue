<template>
  <div class="funventure-detail">
    <h2 class="section-title">📈 장기집합투자증권저축 / 벤처기업투자신탁 세액공제</h2>
    <p class="section-desc">
      국가가 지원하는 <strong>조건부 투자 상품</strong>으로, <strong>세액공제 또는 비과세 혜택</strong>을 받을 수 있습니다.
    </p>
    <h3 class="sub-title">장기집합투자증권저축 세액공제</h3>
    <ul class="info-list">
      <li><strong>공제 대상</strong>: 총급여 5,000만원 이하 근로자</li>
      <li><strong>공제율</strong>: 납입금액의 40%</li>
      <li><strong>공제한도</strong>: 연 600만원 납입 기준, 최대 240만원 세액공제</li>
      <li><strong>요건</strong>: 3년 이상 유지 필요, 중도 해지 시 공제액 추징</li>
    </ul>
    <h3 class="sub-title">벤처기업투자신탁 비과세 혜택</h3>
    <ul class="info-list">
      <li><strong>대상</strong>: 소득 제한 없이 누구나 가능</li>
      <li><strong>투자 조건</strong>: 벤처기업에 50% 이상 투자하는 펀드</li>
      <li><strong>보유 기간</strong>: 3년 이상</li>
      <li><strong>비과세 한도</strong>: 최대 3천만원 투자분까지 수익에 대해 비과세</li>
      <li><strong>예시</strong>: 3천만원 투자 → 4천만원 수익 발생 시, <strong>1천만원 이익에 대해 세금 0원</strong></li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'FunVentureDetail'
}
</script>
<style scoped>
.funventure-detail {
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
.sub-title {
  font-size: 16px;
  font-weight: 700;
  margin: 16px 0 8px;
}
.section-desc {
  font-size: 14px;
  margin-bottom: 12px;
  color: #4b5563;
}
.info-list {
  list-style-type: disc;
  padding-left: 20px;
  margin-bottom: 16px;
  color: #374151;
  font-size: 14px;
}
</style>
