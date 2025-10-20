<template>
  <div class="insurance-detail">
    <h2 class="section-title">🛡️ 보험료 세액공제</h2>
    <p class="section-desc">
      근로자가 민간 보험사에 자발적으로 납입한 <strong>보장성 보험료</strong>에 대해 연말정산 시 세액공제를 받을 수 있습니다.
    </p>
    <h3 class="sub-title">공제 대상</h3>
    <p class="section-desc">
      보험계약자와 피보험자가 본인이거나, 피보험자가 기본공제 대상자일 경우 공제 가능합니다.
    </p>
    <h3 class="sub-title">공제 항목 및 한도</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>공제 대상 보험료</th>
          <th>공제한도</th>
          <th>공제율</th>
          <th>조건</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>보장성 보험료</td>
          <td>생명, 상해, 실손 등</td>
          <td>연 100만원</td>
          <td>12%</td>
          <td>기본공제 대상자를 피보험자로 한 경우</td>
        </tr>
        <tr>
          <td>장애인전용 보험료</td>
          <td>장애인을 피보험자로 한 보장성 보험</td>
          <td>연 100만원 (추가 한도)</td>
          <td>15%</td>
          <td>기본공제 대상자 중 장애인</td>
        </tr>
      </tbody>
    </table>
    <h3 class="sub-title">기본공제 대상자 기준</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>대상</th>
          <th>공제요건</th>
          <th>공제금액</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>본인</td>
          <td>제한 없음</td>
          <td>150만원</td>
        </tr>
        <tr>
          <td>배우자</td>
          <td>연소득 100만원 이하</td>
          <td>150만원</td>
        </tr>
        <tr>
          <td>직계존속</td>
          <td>연소득 100만원 이하, 만 60세 이상</td>
          <td>150만원</td>
        </tr>
        <tr>
          <td>직계비속 / 형제자매</td>
          <td>연소득 100만원 이하, 만 20세 이하</td>
          <td>150만원</td>
        </tr>
        <tr>
          <td>위탁아동 / 수급자</td>
          <td>연소득 100만원 이하<br/>+ 양육 또는 수급 확인</td>
          <td>150만원</td>
        </tr>
      </tbody>
    </table>
    <h3 class="sub-title">주의사항</h3>
    <ul class="info-list">
      <li>납입일이 과세기간 내라면 선납금도 공제 대상입니다.</li>
      <li>중도 해지한 경우에도 납입된 금액은 공제 가능</li>
      <li>자동차보험도 보장성 보험으로 세액공제 대상입니다.</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'InsuranceDetail'
}
</script>
<style scoped>
.insurance-detail {
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
  margin: 20px 0 8px;
}
.section-desc {
  font-size: 14px;
  margin-bottom: 12px;
  color: #4b5563;
}
.info-list {
  list-style-type: disc;
  padding-left: 20px;
  font-size: 14px;
  color: #374151;
}
.info-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  margin-bottom: 16px;
}
.info-table th,
.info-table td {
  border: 1px solid #d1d5db;
  padding: 8px;
  text-align: left;
}
.info-table th {
  background-color: #f3f4f6;
  font-weight: 600;
}
</style>
