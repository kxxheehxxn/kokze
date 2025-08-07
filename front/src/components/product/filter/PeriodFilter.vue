<script setup>
import { ref, watch, computed } from 'vue';
const props = defineProps({
    period: Number,
    amount: String,
});
const emit = defineEmits(['update:period', 'update:amount']);
// 선택된 기간과 금액을 로컬 상태로 보관
const localPeriod = ref(props.period);
const localAmount = ref(props.amount || '');
const confirmedAmount = ref('');
// localPeriod → 숫자(0~4), emit할 때 변환해서 넘김
watch([localPeriod, confirmedAmount], () => {
    const periodValue = {
        0: 0,
        1: 6,
        2: 12,
        3: 24,
        4: 25, // 24개월 이상은 25로 표현
    }[localPeriod.value];
    // 로컬 변경 → 부모 반영
    emit('update:period', periodValue);
    emit('update:amount', confirmedAmount.value);
});
// 외부에서 6, 12, 24, 25가 들어왔을 때 localPeriod 값으로 매핑
watch(
    () => props.period,
    (val) => {
        const reverseMap = {
            0: 0,
            6: 1,
            12: 2,
            24: 3,
            25: 4,
        };
        localPeriod.value = reverseMap[val] ?? 0;
    }
);
watch(
    () => props.amount,
    (val) => {
        localAmount.value = val ?? '';
        confirmedAmount.value = val ?? '';
    }
);
// 금액을 숫자로 파악하는
const amountValue = computed(() =>
    Number(localAmount.value.replaceAll(',', ''))
);
// 유효성 상태 확인용
const isAmountValid = computed(() => amountValue.value >= 100000);
const showConfirmHint = computed(() => {
    if (!localAmount.value) return '10만원 이상부터 가능해요';
    if (amountValue.value < 10000) return '10만원 이상부터 가능해요';
    return `${formatKoreanCurrency(amountValue.value)} 예치`;
});
// 한국어 금액 포맷 함수
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
// 금액 확인
function confirmAmount() {
    confirmedAmount.value = localAmount.value;
}
// 금액 초기화
function clearAmount() {
    localAmount.value = '';
}
</script>
<template>
    <div class="period-filter">
        <!-- 기간 입력 -->
        <div class="slider-container">
            <!-- range 값: 0=전체, 1=6개월, 2=12개월, 3=24개월, 4=24개월 이상 -->
            <input
                type="range"
                min="0"
                max="4"
                step="1"
                v-model.number="localPeriod"
                class="slider"
            />
            <div class="slider-labels">
                <span :class="{ active: localPeriod == 0 }">전체</span>
                <span :class="{ active: localPeriod == 1 }">6개월</span>
                <span :class="{ active: localPeriod == 2 }">12개월</span>
                <span :class="{ active: localPeriod == 3 }">24개월</span>
                <span :class="{ active: localPeriod == 4 }">24개월 이상</span>
            </div>
        </div>
        <!-- 금액 입력 -->
        <div class="amount-container">
            <div class="input-wrapper">
                <input
                    v-model="localAmount"
                    type="text"
                    placeholder="금액을 입력해주세요"
                />
                <button
                    v-if="localAmount"
                    class="clear-button"
                    @click="clearAmount"
                >
                    ×
                </button>
            </div>
            <button
                :disabled="!isAmountValid"
                @click="confirmAmount"
                class="confirm-button"
            >
                확인
            </button>
        </div>
        <p
            :class="[
                'amount-hint',
                { error: amountValue < 100000 && localAmount },
            ]"
        >
            {{ showConfirmHint }}
        </p>
    </div>
</template>
<style scoped>
/* 전체 */
.period-filter {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 1rem;
}
/* 기간 입력 */
.slider-container {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}
.slider {
    width: 100%;
}
.slider-labels {
    display: flex;
    justify-content: space-between;
}
.slider-labels span {
    font-size: 0.9rem;
    color: #666;
}
.slider-labels span.active {
    font-weight: bold;
    color: #1d4ed8;
}
/* 금액 입력 */
.amount-container {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}
.input-wrapper {
    position: relative;
    flex: 1;
}
.input-wrapper input {
    width: 100%;
    padding: 0.5rem 2rem 0.5rem 0.5rem;
    border: 1px solid #1d4ed8;
    border-radius: 0.5rem;
    font-size: 1rem;
}
/* X 버튼을 input 내부 우측에 위치 */
.clear-button {
    position: absolute;
    right: 0.6rem;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    color: #666;
    font-size: 1.2rem;
    cursor: pointer;
}
/* 확인 버튼 */
.confirm-button {
    padding: 0.7rem 4rem;
    border: none;
    border-radius: 0.5rem;
    background: #1d4ed8;
    color: white;
    cursor: pointer;
}
.confirm-button:disabled {
    background: #ccc;
    color: #666;
    cursor: not-allowed;
}
/* hint 텍스트 */
.amount-hint {
    font-size: 0.8rem;
    color: #888;
    margin-top: 0.3rem;
    margin-left: 0.2rem;
    text-align: left;
}
.amount-hint.error {
    color: red;
}
</style>
