<script setup>
import { ref, computed } from 'vue';

const depositAmount = ref('10000000');
const selectedTab = ref('max'); // 'max' or 'basic'
const taxRate = 0.154;

// 기본, 최대 금리
const rates = {
    max: 2.45,
    basic: 2.15,
};

// 더미 데이터
const productOptions = [
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'S',
        rsrv_type_nm: '정액적립식',
        save_trm: 12,
        intr_rate: 2.15,
        intr_rate2: 3.55,
    },
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'S',
        rsrv_type_nm: '정액적립식',
        save_trm: 24,
        intr_rate: 2.15,
        intr_rate2: 3.55,
    },
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'S',
        rsrv_type_nm: '정액적립식',
        save_trm: 36,
        intr_rate: 2.15,
        intr_rate2: 3.55,
    },
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'F',
        rsrv_type_nm: '자유적립식',
        save_trm: 12,
        intr_rate: 2.15,
        intr_rate2: 3.67,
    },
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'F',
        rsrv_type_nm: '자유적립식',
        save_trm: 24,
        intr_rate: 2.15,
        intr_rate2: 3.78,
    },
    {
        fin_prdt_cd: 'WR0001F',
        intr_rate_type: 'S',
        intr_rate_type_nm: '단리',
        rsrv_type: 'F',
        rsrv_type_nm: '자유적립식',
        save_trm: 36,
        intr_rate: 2.15,
        intr_rate2: 3.55,
    },
];
// 더미 데이터
const extraInfo = [
    {
        label: '조건',
        value: '급여이체 실적 등 우대조건 있음',
    },
    {
        label: '유형',
        value: '정액적립식, 자유적립식',
    },
];

// 입력된 금액 문자열에서 숫자만 추출해 숫자로 변환한 값
const parsedAmount = computed(() => {
    return Number(depositAmount.value.replace(/\D/g, '')) || 0;
});

// 입력된 금액을 쉼표(,)로 구분된 문자열 형식으로 반환
const formattedAmount = computed(() => {
    const raw = depositAmount.value.replace(/\D/g, '');
    if (!raw) return '';
    return Number(raw).toLocaleString();
});

// 선택된 금리에 따른 세전 이자 금액 계산
const interest = computed(() => {
    const rate = rates[selectedTab.value] / 100;
    return Math.floor(parsedAmount.value * rate);
});

// 세전 이자에 세율을 곱해 이자 과세 금액 계산
const tax = computed(() => Math.floor(interest.value * taxRate));

// 원금 + 세전이자 - 이자과세 = 세후 수령액 계산
const afterTax = computed(
    () => parsedAmount.value + interest.value - tax.value
);

// 입력값에서 숫자만 추출해 depositAmount에 저장
function onInputChange(e) {
    const onlyNumbers = e.target.value.replace(/\D/g, '');
    depositAmount.value = onlyNumbers;
}

// 숫자를 한국 통화 형식으로 포맷팅 (예: 1,000원)
function formatCurrency(val) {
    return val.toLocaleString('ko-KR') + '원';
}

// 숫자를 ‘억’, ‘만’ 등 단위로 나눠 한글 화폐 표기 문자열로 반환
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

// 입력값 초기화
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
                최고금리 <span>{{ rates.max }}%</span>
            </button>
            <button
                :class="{ active: selectedTab === 'basic' }"
                @click="selectedTab = 'basic'"
            >
                기본금리 <span>{{ rates.basic }}%</span>
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
                    <td>{{ opt.rsrv_type_nm }}</td>
                    <td>{{ opt.save_trm }}개월</td>
                    <td>{{ opt.intr_rate }}%</td>
                    <td>{{ opt.intr_rate2 }}%</td>
                </tr>
            </tbody>
        </table>

        <hr />

        <!-- 조건별, 유형 -->
        <div class="extra">
            <div v-for="(item, idx) in extraInfo" :key="idx" class="info-row">
                <div class="label">{{ item.label }}</div>
                <div
                    class="value"
                    v-html="item.value.replaceAll('\n', '<br />')"
                />
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
