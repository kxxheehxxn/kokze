<template>
  <div class="debit-card-detail">
    <h2 class="section-title">💳 직불 & 체크카드 세액공제 안내</h2>
    <p class="section-desc">
      직불, 체크카드 등 사용 금액이 <strong>총 급여의 25%를 초과</strong>하면, 초과분에 대해 소득공제를 받을 수 있습니다.
    </p>
    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li>총 급여액의 25%를 초과한 사용 금액에 대해 공제 적용</li>
    </ul>
    <h3 class="sub-title">소득공제 한도</h3>
    <table class="debit-table">
      <thead>
        <tr>
          <th>총급여액</th>
          <th>공제 한도</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>7,000만원 이하</td>
          <td>
            300만원<br />
            <span class="note">문화비 30%, 최대 100만원 한도 포함 가능</span>
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
        <tr>
          <td colspan="2">
            ※ 전통시장 및 대중교통 이용금액: <strong>40%</strong> 공제, 100만원 한도 별도 적용
          </td>
        </tr>
      </tbody>
    </table>
    <h3 class="sub-title">추가 공제</h3>
    <ul class="info-list">
      <li>
        <strong>2024년</strong>에 사용한 신용·체크·직불·선불카드, 현금영수증 금액이
        <strong>2023년 대비 5% 초과 증가</strong>한 경우, 증가분의 <strong>10%</strong>에 대해
        <strong>최대 100만원까지 추가 소득공제</strong>를 받을 수 있습니다.
      </li>
    </ul>
    <h3 class="sub-title">공제 혜택</h3>
    <ul class="info-list">
      <li>체크카드 및 현금영수증: 초과분의 <strong>30%</strong> 소득공제</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'DebitCardDetail'
}
</script>
<style scoped>
.debit-card-detail {
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
.debit-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  background-color: #f9fafb;
}
.debit-table th,
.debit-table td {
  border: 1px solid #d1d5db;
  padding: 12px;
  text-align: left;
  vertical-align: top;
  font-size: 14px;
}
.debit-table thead {
  background-color: #f3f4f6;
  font-weight: bold;
}
.note {
  font-size: 13px;
  color: #6b7280;
}
</style>
