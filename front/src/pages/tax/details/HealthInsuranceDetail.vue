<template>
  <div class="insurance-detail">
    <h2 class="section-title">💊 건강 / 고용 보험료 소득공제 안내</h2>
    <p class="section-desc">
      근로자가 의무적으로 납부하는 <strong>건강보험(장기요양 포함)</strong> 및 <strong>고용보험료</strong>는 급여에서 자동 공제되며, <strong>전액 소득공제</strong> 대상입니다.
    </p>

    <h3 class="sub-title">건강보험료 계산 방식 (2025년 기준)</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>항목</th>
          <th>내용</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>건강보험료</td>
          <td>보수월액 × 7.09% × 50%</td>
        </tr>
        <tr>
          <td>장기요양보험료</td>
          <td>건강보험료 × 0.9182 / 7.09</td>
        </tr>
        <tr>
          <td>상·하한액</td>
          <td>
            상한: 보수월액 9,008,340원<br />
            하한: 19,780원
          </td>
        </tr>
      </tbody>
    </table>

    <h3 class="sub-title">고용보험료 계산 방식</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>항목</th>
          <th>내용</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>고용보험료</td>
          <td>보수월액 × 0.9%</td>
        </tr>
      </tbody>
    </table>

    <p class="note">
      ※ <strong>보수월액</strong>이란 비과세 금액을 제외한 근로소득으로 산정되며, 건강·고용보험료의 산정 기준이 됩니다.
    </p>
  </div>
</template>

<script>
export default {
  name: 'HealthInsuranceDetail'
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
  margin: 16px 0 8px;
}

.section-desc {
  font-size: 14px;
  margin-bottom: 12px;
  color: #4b5563;
}

.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  background-color: #f9fafb;
}

.info-table th,
.info-table td {
  border: 1px solid #d1d5db;
  padding: 12px;
  text-align: left;
  font-size: 14px;
  vertical-align: top;
}

.info-table thead {
  background-color: #f3f4f6;
  font-weight: bold;
}

.note {
  font-size: 13px;
  color: #6b7280;
  margin-top: 8px;
}
</style>
