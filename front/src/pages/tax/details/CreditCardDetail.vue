<template>
  <div class="card-detail">
    <h2 class="section-title">💳 신용카드 세액공제 안내</h2>
    <p class="section-desc">
      카드, 현금영수증을 통해 지출한 금액이 <strong>연 소득액의 25%를 초과</strong>하면 초과분에 대해 소득공제를 받을 수 있습니다.
    </p>
    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li>총 급여액의 25%를 초과한 사용 금액에 대해서만 공제 적용</li>
    </ul>
    <h3 class="sub-title">소득공제 한도</h3>
    <table class="card-table">
      <thead>
        <tr>
          <th>총 급여액</th>
          <th>공제 한도</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>7,000만원 이하</td>
          <td>
            300만원<br />
            (문화비 30%, 최대 100만원 한도 포함)
          </td>
        </tr>
        <tr>
          <td>7,000만원 초과 ~ 1.2억원 이하</td>
          <td>250만원</td>
        </tr>
        <tr>
          <td>1.2억원 초과</td>
          <td>200만원</td>
        </tr>
      </tbody>
    </table>
    <h3 class="sub-title">추가 공제</h3>
    <ul class="info-list">
        <li>2024년의 신용·체크·직불·선불카드, 현금영수증 사용금액이 2023년도 사용금액보다 5%를 초과해 늘어났을 때는
            <strong>5%를 초과한 소비증가분의 10%에 대해 추가로 소득공제(100만 원 한도)</strong>도 받을 수 있습니다.
        </li>
    </ul>
    <h3 class="sub-title">공제 혜택</h3>
    <ul class="info-list">
      <li>신용카드: 초과분의 15% 소득공제</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'CardDetail'
}
</script>
<style scoped>
.card-detail {
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
.card-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  background-color: #f9fafb;
}
.card-table th,
.card-table td {
  border: 1px solid #d1d5db;
  padding: 12px;
  text-align: left;
  vertical-align: top;
  font-size: 14px;
}
.card-table thead {
  background-color: #f3f4f6;
  font-weight: bold;
}
</style>
