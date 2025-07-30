<template>
  <div class="pension-detail">
    <h2 class="section-title">📘 국민연금 소득공제</h2>
    <p class="section-desc">
      직장인이 의무적으로 가입하는 <strong>국민연금</strong>은 본인 명의로 납부한 경우, <strong>전액 소득공제</strong>가 가능합니다.
    </p>

    <h3 class="sub-title">공제 대상 및 방식</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>종류</th>
          <th>공제 방식</th>
          <th>공제 한도/조건</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>국민연금</td>
          <td>공적 연금</td>
          <td>전액 소득공제</td>
          <td>한도 없음</td>
        </tr>
        <tr>
          <td>연금저축 / IRP</td>
          <td>개인 연금</td>
          <td>세액공제</td>
          <td>연 400/300만 원 한도, 소득에 따라 공제율 다름</td>
        </tr>
      </tbody>
    </table>

    <h3 class="sub-title">연금 수령 시 과세 기준</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>과세 여부</th>
          <th>설명</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>노령연금</td>
          <td>과세됨</td>
          <td>일정 소득공제와 세율 혜택이 적용됨</td>
        </tr>
        <tr>
          <td>장애연금, 유족연금</td>
          <td>비과세</td>
          <td>전액 비과세 처리</td>
        </tr>
        <tr>
          <td>일시금 (반환일시금 등)</td>
          <td>일부 과세 가능</td>
          <td>퇴직소득세로 분리 과세될 수 있음</td>
        </tr>
      </tbody>
    </table>

    <h3 class="sub-title">노령연금 과세대상 비율 (나이에 따른 차등 적용)</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>수령자 나이</th>
          <th>과세대상 비율</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>만 70세 미만</td>
          <td>40%</td>
        </tr>
        <tr>
          <td>만 70~79세</td>
          <td>30%</td>
        </tr>
        <tr>
          <td>만 80세 이상</td>
          <td>20%</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'NationalPensionDetail'
}
</script>

<style scoped>
.pension-detail {
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
  text-align: left;
}

.info-table th {
  background-color: #f3f4f6;
  font-weight: 600;
}
</style>
