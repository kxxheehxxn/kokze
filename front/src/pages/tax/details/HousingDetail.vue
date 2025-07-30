<template>
  <div class="housing-detail">
    <h2 class="section-title">🏠 월세액 세액공제 안내</h2>
    <p class="section-desc">
      1년 동안 지출한 <strong>월세액의 일부를 세액에서 공제</strong>받을 수 있습니다. 총급여와 주택 조건을 충족하면 공제를 받을 수 있습니다.
    </p>

    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li><strong>총급여 8,000만원 이하</strong> (또는 종합소득금액 7,000만원 이하)</li>
      <li><strong>국민주택규모(85㎡ 이하)</strong> 또는 기준시가 <strong>4억원 이하</strong> 주택</li>
      <li>주거용 오피스텔, 고시원 포함</li>
      <li><strong>근로자 본인 또는 기본공제 대상자 명의</strong>의 임차 계약</li>
      <li>임대차계약 주소와 주민등록상 주소가 <strong>일치</strong>해야 공제 가능</li>
    </ul>

    <h3 class="sub-title">공제 혜택</h3>
    <ul class="info-list">
      <li><strong>총급여 5,500만원 이하</strong>: <strong>월세액의 17%</strong> 세액공제 (종합소득 4,500만원 이하)</li>
      <li><strong>총급여 5,500만원 초과 ~ 8,000만원 이하</strong>: <strong>월세액의 15%</strong> 세액공제 (단, 종합소득 7,000만원 초과자 제외)</li>
      <li>공제 대상 월세액은 <strong>연간 1,000만원 한도</strong></li>
    </ul>

    <h2 class="section-title">🏡 주택자금 소득공제 안내</h2>
    <p class="section-desc">
      주거 목적의 <strong>대출 원리금 또는 이자 상환액</strong>에 대해 조건을 충족하면 소득공제를 받을 수 있습니다.
    </p>

    <h3 class="sub-title">공제 대상</h3>

    <h4 class="mini-title">📌 주택임차차입금 원리금 상환액</h4>
    <ul class="info-list">
      <li><strong>무주택 세대의 세대주</strong>인 근로소득자</li>
      <li><strong>국민주택규모</strong>(주거전용면적 85㎡ 이하, 읍/면 지역은 100㎡ 이하)의 주택 (오피스텔 포함)</li>
      <li><strong>원리금 상환액의 40%</strong> 소득공제 (연 400만원 한도)</li>
    </ul>

    <h4 class="mini-title">📌 장기주택저당차입금 이자상환액</h4>
    <ul class="info-list">
      <li><strong>무주택 또는 1주택 보유</strong> 세대의 세대주</li>
      <li>취득 당시 <strong>기준시가 6억원 이하</strong> 주택</li>
      <li><strong>이자 상환액 전액</strong> 소득공제</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'HousingDetail'
}
</script>

<style scoped>
.housing-detail {
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

.mini-title {
  font-size: 15px;
  font-weight: 600;
  margin: 12px 0 6px;
  color: #374151;
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
