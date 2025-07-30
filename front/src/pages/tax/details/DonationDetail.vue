<template>
  <div class="donation-detail">
    <h2 class="section-title">🎁 기부금 세액공제 안내</h2>
    <p class="section-desc">
      <strong>국가가 인정한 단체에 기부</strong>하면 <strong>소득공제 또는 세액공제</strong> 혜택을 받을 수 있습니다.
    </p>

    <h3 class="sub-title">공제 대상 단체</h3>
    <ul class="info-list">
      <li>사회복지단체 (아동·장애인·노인 등)</li>
      <li>학교, 장학재단</li>
      <li>종교단체 (교회·성당·절 등)</li>
      <li>공익재단 및 긴급재난 기부</li>
      <li>유니세프, 세이브더칠드런 등 공인 단체</li>
    </ul>

    <h3 class="sub-title">공제 방식 비교</h3>
    <table class="donation-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>설명</th>
          <th>예시</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>세액공제</td>
          <td>세금에서 직접 차감</td>
          <td>기부 10만원 → 세금 15,000원 덜 냄</td>
        </tr>
        <tr>
          <td>소득공제</td>
          <td>과세 소득을 줄여 세금 감소</td>
          <td>연봉이 낮아진 것처럼 세금 계산</td>
        </tr>
      </tbody>
    </table>

    <h3 class="sub-title">공제 금액</h3>
    <table class="donation-table">
      <thead>
        <tr>
          <th>기부 금액</th>
          <th>공제율</th>
          <th>공제 방식</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>10만원 이하</td>
          <td>15%</td>
          <td>세액공제</td>
        </tr>
        <tr>
          <td>10만원 초과</td>
          <td>15~30% (단체에 따라 다름)</td>
          <td>세액공제</td>
        </tr>
        <tr>
          <td>지정기부단체 외 일반 단체</td>
          <td>기준 없음</td>
          <td>소득공제</td>
        </tr>
      </tbody>
    </table>

    <h3 class="sub-title">공제 조건</h3>
    <ul class="info-list">
      <li>기부금 영수증 제출 필수</li>
      <li>연말정산 또는 종합소득세 신고 시 등록</li>
      <li>현금, 카드, 계좌이체 모두 가능 (단, 영수증 필요)</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'DonationDetail'
}
</script>

<style scoped>
.donation-detail {
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

.donation-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  background-color: #f9fafb;
}

.donation-table th,
.donation-table td {
  border: 1px solid #d1d5db;
  padding: 12px;
  text-align: left;
  vertical-align: top;
  font-size: 14px;
}

.donation-table thead {
  background-color: #f3f4f6;
  font-weight: bold;
}
</style>
