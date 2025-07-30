<template>
  <div class="education-detail">
    <h2 class="section-title">📘교육비 세액공제 안내</h2>
    <p class="section-desc">
      근로소득자가 본인 또는 부양가족의 교육비를 지출했을 때, <strong>교육비의 15%</strong>에 해당하는 금액을 산출세액에서 차감합니다.
    </p>

    <h3 class="sub-title">공제 대상 및 금액</h3>
    <table class="edu-table">
      <thead>
        <tr>
          <th>구분</th>
          <th>세액공제 대상금액 한도</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td rowspan="2">
            일반 교육비<br />
            <span class="note">(나이제한 없음)</span>
          </td>
          <td>
            본인: 한도 없음
          </td>
        </tr>
        <tr>
          <td>
            부양가족: 
            <ul class="inline-list">
              <li>취학 전 아동, 초·중·고등학생: 1명당 연 300만원</li>
              <li>대학생: 1명당 연 900만원 (대학원생은 공제대상 아님)</li>
            </ul>
          </td>
        </tr>
        <tr>
          <td>장애인 특수교육비<br /><span class="note">(직계존속 포함, 소득제한 없음)</span></td>
          <td>한도 없음</td>
        </tr>
      </tbody>
    </table>

    <p class="section-desc footnote">
      * 직계존속의 일반교육비는 공제대상이 아님
    </p>

    <p class="section-desc">
      <strong>조건:</strong> 교육비 납입증명서(연말정산 간소화 서비스에서 조회 가능) → 연말정산 시 회사에 제출
    </p>
  </div>
</template>

<script>
export default {
  name: 'EducationDetail'
}
</script>

<style scoped>
.education-detail {
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

.footnote {
  font-size: 13px;
  color: #6b7280;
  margin-top: 8px;
}

.edu-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  background-color: #f9fafb;
}

.edu-table th,
.edu-table td {
  border: 1px solid #d1d5db;
  padding: 12px;
  text-align: left;
  vertical-align: top;
  font-size: 14px;
}

.edu-table thead {
  background-color: #f3f4f6;
  font-weight: bold;
}

.inline-list {
  padding-left: 16px;
  list-style-type: disc;
  margin: 8px 0 0 0;
}

.note {
  font-size: 13px;
  color: #6b7280;
}
</style>
