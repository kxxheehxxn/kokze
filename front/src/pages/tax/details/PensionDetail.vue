<template>
  <div class="pension-account-detail">
    <h2 class="section-title">📦 개인연금저축 / 연금계좌 세액공제</h2>
    <p class="section-desc">
      2001년 이전 가입한 <strong>개인연금저축</strong>과 현재 가능한 <strong>연금계좌(IRP, 연금저축)</strong>를 통해
      노후 준비와 세액공제를 동시에 받을 수 있습니다.
    </p>
    <h3 class="sub-title">개념</h3>
    <ul class="desc-list">
      <li><strong>개인연금저축</strong> : 2001년 이전 가입 상품, 현재 신규 가입 불가</li>
      <li><strong>연금계좌</strong> : 2001년 이후 도입된 세액공제형 노후 준비 상품</li>
    </ul>
    <h3 class="sub-title">연금계좌 종류</h3>
    <ul class="desc-list">
      <li>연금저축계좌 (보험사, 증권사, 은행, 자산운용사)</li>
      <li>개인형 퇴직연금(IRP) (은행, 증권사, 보험사)</li>
    </ul>
    <h3 class="sub-title">세액공제 조건</h3>
    <ul class="desc-list">
      <li>연금저축: 누구나 가입 가능 (소득 무관)</li>
      <li>IRP: 소득자, 퇴직금 수령자만 가입 가능</li>
    </ul>
    <h3 class="sub-title">세액공제 혜택</h3>
    <ul class="desc-list">
      <li>총급여 5,500만원 이하: 공제율 16.5% (최대 1,485,000원)</li>
      <li>총급여 5,500만원 초과: 공제율 13.2% (최대 1,188,000원)</li>
      <li>연금저축 한도: 600만원, IRP 포함 합산 한도: 900만원</li>
    </ul>
    <h3 class="sub-title">수령 시 혜택</h3>
    <ul class="desc-list">
      <li>연금수령 시 <strong>연금소득세 3.3~5.5%</strong> 적용</li>
      <li>연금소득 연 1,200만원 이하 → 분리과세, 종합소득 신고 불필요</li>
      <li>퇴직소득을 연금계좌로 이체 시 퇴직소득세 30~40% 감면</li>
    </ul>
    <h3 class="sub-title">연금저축 / IRP 비교</h3>
    <table class="info-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>연금저축</th>
          <th>IRP</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>가입대상</td>
          <td>누구나</td>
          <td>소득자만</td>
        </tr>
        <tr>
          <td>세액공제한도</td>
          <td>400만원</td>
          <td>700만원</td>
        </tr>
        <tr>
          <td>납입한도</td>
          <td>600만원</td>
          <td>900만원</td>
        </tr>
        <tr>
          <td>위험자산한도</td>
          <td>제한없음</td>
          <td>70%</td>
        </tr>
        <tr>
          <td>계좌수</td>
          <td>여러개 가능</td>
          <td>1인 1계좌</td>
        </tr>
        <tr>
          <td>중도인출</td>
          <td>비교적 자유</td>
          <td>제한적</td>
        </tr>
        <tr>
          <td>상품선택</td>
          <td>해당 금융사만</td>
          <td>모든 금융기관</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
export default {
  name: 'PensionAccountDetail'
}
</script>
<style scoped>
.pension-account-detail {
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
.desc-list {
  margin: 0 0 12px 18px;
  padding: 0;
  font-size: 14px;
  color: #374151;
}
.desc-list li {
  margin-bottom: 4px;
}
.info-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  margin-top: 8px;
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
