<template>
  <div class="over-income-detail">
    <h2 class="section-title">📛 소득기준 초과 부양가족</h2>
    <p class="section-desc">
      부양가족이라 하더라도 아래 요건 중 하나라도 미충족 시 인적공제 대상에서 제외됩니다.
    </p>
    <h3 class="sub-title">① 소득 요건</h3>
    <p class="section-desc">연 소득금액 100만원 이하 (근로소득만 있을 경우 총급여 500만원 이하)</p>
    <table class="info-table">
      <thead>
        <tr>
          <th>근로소득</th>
          <th>기타소득</th>
          <th>판단 기준</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>O</td>
          <td>X</td>
          <td>총급여 ≤ 500만 원</td>
        </tr>
        <tr>
          <td>O</td>
          <td>O</td>
          <td>소득금액 ≤ 100만 원</td>
        </tr>
        <tr>
          <td>X</td>
          <td>O</td>
          <td>소득금액 ≤ 100만 원</td>
        </tr>
      </tbody>
    </table>
    <h3 class="sub-title">② 나이 요건</h3>
    <p class="section-desc">
      - 부모: 만 60세 이상<br />
      - 자녀 및 형제자매: 만 20세 이하<br />
      <strong>※ 장애인의 경우 나이 요건 없음</strong>
    </p>
    <h3 class="sub-title">③ 동거 요건</h3>
    <p class="section-desc">
      - 본인·배우자·자녀: 거주지 무관<br />
      - 부모, 조부모: 별거 가능하나 실질적 부양 필요<br />
      - 형제자매 등: 주민등록상 동거 필수<br />
      <strong>※ 취학·요양 등 일시퇴거는 인정</strong>
    </p>
  </div>
</template>
<script>
export default {
  name: 'OverIncomeDetail'
}
</script>
<style scoped>
.over-income-detail {
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
  text-align: center;
}
.info-table th {
  background-color: #f3f4f6;
  font-weight: 600;
}
</style>
