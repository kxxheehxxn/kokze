<template>
  <div class="medical-detail">
    <h2 class="section-title">🏥 의료비 세액공제 안내</h2>
    <p class="section-desc">
      본인, 배우자 및 부양가족을 위해 지출한 의료비 중 <strong>총급여의 3%를 초과</strong>한 금액에 대해 세액공제를 받을 수 있습니다.
    </p>

    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li>총급여의 3%를 초과하는 의료비에 한해 공제 가능</li>
      <li>지출 내역에는 병원, 약국, 한의원, 치과 등 포함</li>
    </ul>

    <h3 class="sub-title">공제 대상 금액</h3>
    <p class="section-desc">
      의료비 총액 - (총급여액 × 3%) = <strong>공제 대상 금액</strong>
    </p>

    <h3 class="sub-title">공제율</h3>
    <ul class="info-list">
      <li>일반 의료비: <strong>15%</strong></li>
      <li>미숙아 및 선천성 이상아 의료비: <strong>20%</strong></li>
      <li>난임 시술비: <strong>30%</strong></li>
    </ul>

    <h3 class="sub-title">공제 한도</h3>
    <ul class="info-list">
      <li>본인, 장애인, 65세 이상 부모, 6세 이하 자녀의 의료비는 <strong>한도 없이 전액 공제</strong> 가능 (의료비 몰아주기 가능)</li>
    </ul>

    <h3 class="sub-title">공제 제외 항목</h3>
    <ul class="info-list">
      <li>외국 병원 진료비</li>
      <li>성형수술 및 미용 목적 지출</li>
      <li>실손보험 등으로 보전받은 금액</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'MedicalDetail'
}
</script>

<style scoped>
.medical-detail {
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
