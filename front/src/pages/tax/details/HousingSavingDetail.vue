<template>
  <div class="saving-detail">
    <h2 class="section-title">🏡 주택마련저축 소득공제 안내</h2>
    <p class="section-desc">
      근로자가 <strong>주택청약종합저축</strong>에 납입한 금액에 대해 <strong>연간 300만원 한도 내에서 40%</strong>를 소득공제 받을 수 있습니다.
    </p>
    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li><strong>총급여액 7,000만원 이하</strong>인 근로자 (2024년 기준)</li>
      <li><strong>본인 명의</strong>로 주택청약종합저축에 가입 및 납입</li>
    </ul>
    <h3 class="sub-title">공제 혜택</h3>
    <ul class="info-list">
      <li><strong>연 300만원 한도</strong> 납입액의 <strong>40%</strong>를 소득공제</li>
      <li>최대 <strong>120만원</strong>까지 공제 가능</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'HousingSavingDetail'
}
</script>
<style scoped>
.saving-detail {
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
