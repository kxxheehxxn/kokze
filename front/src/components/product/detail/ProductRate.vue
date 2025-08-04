<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  product: {
    type: Object,
    required: true,
  },
});

const depositAmount = ref('10000000');
const selectedTab = ref('max'); // 'max' or 'basic'
const taxRate = 0.154;

// 💰 금리 목록에서 최대/기본 금리 계산
const rates = computed(() => {
  const options = props.product.options || [];
  const basic = Math.max(...options.map((opt) => opt.intrRate ?? 0));
  const max = Math.max(...options.map((opt) => opt.intrRate2 ?? 0));
  return { basic, max };
});

// 👉 options 리스트
const productOptions = computed(() => props.product.options || []);

// 💬 우대조건 및 유형 정리
const extraInfo = computed(() => {
  const result = [];

  if (props.product.spclCnd) {
    result.push({ label: '조건', value: props.product.spclCnd });
  }

  const types = [
    ...new Set((props.product.options || []).map((o) => o.rsrvTypeNm)),
  ].join(', ');
  if (types) {
    result.push({ label: '유형', value: types });
  }

  return result;
});

// 🔢 입력 금액 처리
const parsedAmount = computed(() => {
  return Number(depositAmount.value.replace(/\D/g, '')) || 0;
});

const formattedAmount = computed(() => {
  const raw = depositAmount.value.replace(/\D/g, '');
  if (!raw) return '';
  return Number(raw).toLocaleString();
});

// 💸 계산 로직
const interest = computed(() => {
  const rate =
    selectedTab.value === 'max'
      ? rates.value.max / 100
      : rates.value.basic / 100;
  return Math.floor(parsedAmount.value * rate);
});

const tax = computed(() => Math.floor(interest.value * taxRate));
const afterTax = computed(
  () => parsedAmount.value + interest.value - tax.value
);

function onInputChange(e) {
  const onlyNumbers = e.target.value.replace(/\D/g, '');
  depositAmount.value = onlyNumbers;
}

function formatCurrency(val) {
  return val.toLocaleString('ko-KR') + '원';
}

function formatKoreanCurrency(num) {
  if (isNaN(num) || num <= 0) return '';

  const jo = Math.floor(num / 1_0000_0000_0000);
  const uk = Math.floor((num % 1_0000_0000_0000) / 1_0000_0000);
  const man = Math.floor((num % 1_0000_0000) / 10000);
  const rest = num % 10000;

  let result = '';
  if (jo > 0) result += `${jo}조`;
  if (uk > 0) result += `${uk}억`;
  if (man > 0) result += `${man}만`;
  if (rest > 0) result += rest.toLocaleString();
  result += '원';

  return result;
}

function clearInput() {
  depositAmount.value = '';
}
</script>

<template>
  <div class="rate-box">
    <h2 class="title">금리 안내</h2>
    <hr />
    <p class="subtitle">12개월 만기 시 세후수령액 (단리)</p>

    <!-- 예치금액 입력 -->
    <div class="amount-input">
      <input
        type="text"
        :value="formattedAmount"
        @input="onInputChange"
        placeholder="예치금액을 입력해주세요"
      />
      <span class="unit">원</span>
      <button class="clear-btn" @click="clearInput">×</button>
    </div>

    <p class="korean-text">{{ formatKoreanCurrency(parsedAmount) }}</p>

    <!-- 탭 버튼 -->
    <div class="tab-buttons">
      <button
        :class="{ active: selectedTab === 'max' }"
        @click="selectedTab = 'max'"
      >
        최고금리 <span>{{ rates.max.toFixed(2) }}%</span>
      </button>
      <button
        :class="{ active: selectedTab === 'basic' }"
        @click="selectedTab = 'basic'"
      >
        기본금리 <span>{{ rates.basic.toFixed(2) }}%</span>
      </button>
    </div>

    <!-- 세후 계산 요약 -->
    <div class="summary-box">
      <div>
        원금합계 <span>{{ formatCurrency(parsedAmount) }}</span>
      </div>
      <div>
        세전이자 <span>+{{ formatCurrency(interest) }}</span>
      </div>
      <div>
        이자과세(15.4%) <span>-{{ formatCurrency(tax) }}</span>
      </div>
      <hr />
      <div class="after-tax">
        세후수령액 <span>{{ formatCurrency(afterTax) }}</span>
      </div>
    </div>

    <!-- 금리 테이블 -->
    <table class="rate-table">
      <thead>
        <tr>
          <th>유형</th>
          <th>기간</th>
          <th>기본이율</th>
          <th>고객적용이율</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(opt, idx) in productOptions" :key="idx">
          <td>{{ opt.rsrvTypeNm }}</td>
          <td>{{ opt.saveTrm }}개월</td>
          <td>{{ opt.intrRate }}%</td>
          <td>{{ opt.intrRate2 }}%</td>
        </tr>
      </tbody>
    </table>

    <hr />

    <!-- 조건/유형 안내 -->
    <div class="extra">
      <div v-for="(item, idx) in extraInfo" :key="idx" class="info-row">
        <div class="label">{{ item.label }}</div>
        <div class="value" v-html="item.value.replaceAll('\n', '<br />')" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.rate-box {
  background: white;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: inset 0 0 12px #a1c4fd;
  margin-top: 1.5rem;
  font-size: 1rem;
}
.title {
  font-size: 1.5rem;
  font-weight: bold;
}
.subtitle {
  color: #666;
  margin: 0.5rem 0 1rem;
  font-size: 0.9rem;
}
.amount-input {
  position: relative;
  display: inline-block;
  margin-bottom: 1rem;
  width: 100%;
}
.amount-input input {
  width: 100%;
  box-sizing: border-box;
  font-size: 1.5rem;
  padding: 0.5rem 2.5rem 0.5rem 0.5rem;
  border: 1px solid #ccc;
  border-radius: 0.5rem;
  font-weight: bold;
}
.amount-input .unit {
  position: absolute;
  right: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: #333;
}
.amount-input .clear-btn {
  position: absolute;
  right: 0.2rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #aaa;
}
.tab-buttons {
  display: flex;
  border: 1px solid #ccc;
  border-radius: 0.5rem;
  overflow: hidden;
  margin-bottom: 1rem;
}
.tab-buttons button {
  flex: 1;
  padding: 0.5rem;
  font-weight: bold;
  background: #f8f8f8;
  border: none;
  cursor: pointer;
}
.tab-buttons button.active {
  background: #e8f0ff;
  color: #007aff;
}
.summary-box {
  background: #eef5ff;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 2rem;
  font-size: 0.95rem;
  line-height: 1.6;
}
.summary-box div {
  display: flex;
  justify-content: space-between;
}
.after-tax {
  font-weight: bold;
  font-size: 1.1rem;
}
.rate-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 2rem;
  font-size: 0.95rem;
}
.rate-table th,
.rate-table td {
  border: 1px solid #ccc;
  padding: 0.5rem;
  text-align: center;
}
.extra {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.extra .info-row {
  display: grid;
  grid-template-columns: 100px 1fr;
  gap: 1rem;
  align-items: start;
}
.extra .label {
  font-weight: bold;
  color: #111;
  white-space: nowrap;
}
.extra .value {
  color: #333;
  line-height: 1.6;
  word-break: keep-all;
}
</style>
