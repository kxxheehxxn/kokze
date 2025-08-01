<script setup>
import { ref, computed, watch } from 'vue';
import BankFilter from './filter/BankFilter.vue';
import PeriodFilter from './filter/PeriodFilter.vue';
import TypeFilter from './filter/TypeFilter.vue';
import ConditionFilter from './filter/ConditionFilter.vue';

// 필터 토글
const activeFilter = ref(null);

// 모든 필터 상태를 하나의 객체로 관리
const filters = ref({
    banks: [],
    period: 0,
    amount: '',
    type: [],
    conditions: [],
});

// 라벨 변환 함수
const periodMap = {
    6: '6개월',
    12: '12개월',
    24: '24개월',
    25: '24개월 이상',
};

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

// 선택된 필터를 chip으로 변환
const chips = computed(() => {
    const { banks, period, amount, type, conditions } = filters.value;

    return [
        ...banks,
        periodMap[period] || null,
        amount ? formatKoreanCurrency(Number(amount)) : null,
        ...type,
        ...conditions,
    ].filter(Boolean);
});

// chip 제거
function removeChip(chip) {
    // 은행
    filters.value.banks = filters.value.banks.filter((b) => b !== chip);

    // 기간
    const periodKey = Object.entries(periodMap).find(
        ([, label]) => label === chip
    )?.[0];
    if (periodKey) {
        filters.value.period = 0;
    }

    // 금액
    if (chip === formatKoreanCurrency(Number(filters.value.amount))) {
        filters.value.amount = '';
    }

    // 유형
    filters.value.type = filters.value.type.filter((t) => t !== chip);

    // 조건
    filters.value.conditions = filters.value.conditions.filter(
        (c) => c !== chip
    );
}

// 외부로 emit
const emit = defineEmits(['update:filters']);
watch(
    filters,
    () => {
        emit('update:filters', { ...filters.value });
    },
    { deep: true }
);
</script>

<template>
    <div class="filter-bar-wrapper">
        <!-- 은행 필터 -->
        <BankFilter v-model="filters.banks" />

        <!-- 세부 필터 토글 -->
        <div class="filter-toggle-row">
            <button
                @click="
                    activeFilter = activeFilter === 'period' ? null : 'period'
                "
                :class="[
                    'filter-toggle',
                    { active: activeFilter === 'period' },
                ]"
            >
                기간·금액
                <span>{{ activeFilter === 'period' ? '▲' : '▼' }}</span>
            </button>

            <button
                @click="activeFilter = activeFilter === 'type' ? null : 'type'"
                :class="['filter-toggle', { active: activeFilter === 'type' }]"
            >
                상품유형 <span>{{ activeFilter === 'type' ? '▲' : '▼' }}</span>
            </button>

            <button
                @click="
                    activeFilter =
                        activeFilter === 'condition' ? null : 'condition'
                "
                :class="[
                    'filter-toggle',
                    { active: activeFilter === 'condition' },
                ]"
            >
                우대조건
                <span>{{ activeFilter === 'condition' ? '▲' : '▼' }}</span>
            </button>
        </div>

        <!-- 세부 필터 -->
        <div class="filter-row">
            <div v-if="activeFilter === 'period'" class="dropdown-panel">
                <PeriodFilter
                    v-model:period="filters.period"
                    v-model:amount="filters.amount"
                />
            </div>
            <div v-if="activeFilter === 'type'" class="dropdown-panel">
                <TypeFilter v-model="filters.type" />
            </div>
            <div v-if="activeFilter === 'condition'" class="dropdown-panel">
                <ConditionFilter v-model="filters.conditions" />
            </div>
        </div>

        <hr />

        <!-- 선택된 필터 태그 -->
        <div class="chips">
            <span class="chip" v-for="(chip, idx) in chips" :key="idx">
                {{ chip }}
                <button @click="removeChip(chip)">×</button>
            </span>
        </div>
    </div>
</template>

<style scoped>
.filter-bar-wrapper {
    background: white;
    padding: 2rem;
    border-radius: 1.5rem;
    box-shadow: inset 0 0 12px #3573ee;
    text-align: center;
    margin-bottom: 2rem;
}

.filter-toggle-row {
    display: flex;
    justify-content: flex-start;
    gap: 0.5rem;
    margin-top: 1rem;
}

.filter-toggle {
    border: 1px solid #ccc;
    border-radius: 2rem;
    padding: 0.6rem 1rem;
    background: white;
    font-weight: 600;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 0.25rem;
    color: black;
}

.filter-toggle.active {
    border-color: #1d4ed8;
    color: #1d4ed8;
}

.dropdown-panel {
    margin-top: 1rem;
    background: #fff;
    border-radius: 1rem;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    padding: 1rem;
}

.chips {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    margin-top: 1rem;
}

.chip {
    background: #316be7;
    color: white;
    padding: 0.4rem 0.8rem;
    border-radius: 2rem;
    font-size: 0.9rem;
}
.chip button {
    background: none;
    border: none;
    color: white;
    margin-left: 0.5rem;
    cursor: pointer;
}
</style>
