<script setup>
import { ref, computed, watch } from 'vue';
import BankFilter from './filter/BankFilter.vue';
import PeriodFilter from './filter/PeriodFilter.vue';
import TypeFilter from './filter/TypeFilter.vue';
import ConditionFilter from './filter/ConditionFilter.vue';

const props = defineProps({
  filters: Object,
});
const emit = defineEmits(['update:filters']);

// 입력 전용 상태 (UI용)
const localFilters = ref({
  banks: [],
  period: 0,
  amount: '',
  type: [],
  conditions: [],
});

// 외부 필터 active 상태
const activeFilter = ref(null);

// 외부 filters가 변경되면 localFilters도 초기화
watch(
  () => props.filters,
  (newVal) => {
    localFilters.value = {
      banks: newVal.bankNames || [],
      period: getPeriodFromRange(newVal.minSaveTrm, newVal.maxSaveTrm),
      amount: newVal.minAmount?.toString() || '',
      type: newVal.productType || [],
      conditions: newVal.spclCndKeywords || [],
    };
    activeFilter.value = newVal.__active || null;
  },
  { immediate: true }
);

function emitFilterDto() {
  const f = localFilters.value;
  emit('update:filters', {
    bankNames: f.banks || [], // 은행 필터
    productType: f.type || [],
    minSaveTrm: getMinSaveTrm(f.period),
    maxSaveTrm: getMaxSaveTrm(f.period),
    minAmount: parseAmount(f.amount),
    maxAmount: null,
    spclCndKeywords: f.conditions || [], // 우대조건 필터
    __active: activeFilter.value,
  });
}

// 저장 기간 매핑
function getMinSaveTrm(period) {
  if (!period || period === 0) return null;
  if (period === 25) return 25;
  return period;
}
function getMaxSaveTrm(period) {
  if (!period || period === 0) return null;
  if (period === 25) return null;
  return period;
}
function getPeriodFromRange(min, max) {
  if (!min && !max) return 0;
  if (min === 25) return 25;
  return min;
}
function parseAmount(amount) {
  const parsed = parseInt(amount);
  return isNaN(parsed) ? null : parsed;
}

// UI 표시용
const periodMap = {
  6: '6개월',
  12: '12개월',
  24: '24개월',
  25: '24개월 이상',
};

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

const chips = computed(() => {
  const f = localFilters.value;
  return [
    ...f.banks,
    periodMap[f.period] || null,
    f.amount ? formatKoreanCurrency(Number(f.amount)) : null,
    ...f.type,
    ...f.conditions,
  ].filter(Boolean);
});

function removeChip(chip) {
  const f = localFilters.value;
  f.banks = f.banks.filter((b) => b !== chip);
  const periodKey = Object.entries(periodMap).find(
    ([, label]) => label === chip
  )?.[0];
  if (periodKey) f.period = 0;
  if (chip === formatKoreanCurrency(Number(f.amount))) f.amount = '';
  f.type = f.type.filter((t) => t !== chip);
  f.conditions = f.conditions.filter((c) => c !== chip);
  emitFilterDto();
}
function resetFilters() {
  localFilters.value = {
    banks: [],
    period: 0,
    amount: '',
    type: [],
    conditions: [],
  };
  activeFilter.value = null;
  emitFilterDto(); // 필터 초기화 반영
}
</script>

<template>
  <div class="filter-bar-wrapper">
    <!-- 은행 필터 -->
    <BankFilter v-model="localFilters.banks" @change="emitFilterDto" />

    <!-- 토글 버튼 -->
    <div class="filter-toggle-row">
      <button
        @click="
          activeFilter = activeFilter === 'period' ? null : 'period';
          emitFilterDto();
        "
        :class="['filter-toggle', { active: activeFilter === 'period' }]"
      >
        기간·금액
        <span
          ><i
            :class="
              activeFilter === 'period'
                ? 'fa-solid fa-angle-up'
                : 'fa-solid fa-angle-down'
            "
          ></i
        ></span>
      </button>
      <button
        @click="
          activeFilter = activeFilter === 'type' ? null : 'type';
          emitFilterDto();
        "
        :class="['filter-toggle', { active: activeFilter === 'type' }]"
      >
        상품유형
        <span
          ><i
            :class="
              activeFilter === 'type'
                ? 'fa-solid fa-angle-up'
                : 'fa-solid fa-angle-down'
            "
          ></i
        ></span>
      </button>
      <button
        @click="
          activeFilter = activeFilter === 'condition' ? null : 'condition';
          emitFilterDto();
        "
        :class="['filter-toggle', { active: activeFilter === 'condition' }]"
      >
        우대조건
        <span
          ><i
            :class="
              activeFilter === 'condition'
                ? 'fa-solid fa-angle-up'
                : 'fa-solid fa-angle-down'
            "
          ></i
        ></span>
      </button>
      <button @click="resetFilters" class="reset-btn">필터 초기화</button>
    </div>

    <!-- 세부 필터 -->
    <div class="filter-row">
      <div v-if="activeFilter === 'period'" class="dropdown-panel">
        <PeriodFilter
          v-model:period="localFilters.period"
          v-model:amount="localFilters.amount"
          @update:period="emitFilterDto"
          @update:amount="emitFilterDto"
        />
      </div>
      <div v-if="activeFilter === 'type'" class="dropdown-panel">
        <TypeFilter v-model="localFilters.type" @change="emitFilterDto" />
      </div>
      <div v-if="activeFilter === 'condition'" class="dropdown-panel">
        <ConditionFilter
          v-model="localFilters.conditions"
          @change="emitFilterDto"
        />
      </div>
    </div>
    <hr />

    <!-- 필터 chips -->
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
  background: #ffffff;
  padding: 32px;
  border-radius: 20px;
  box-shadow: inset 0 0 12px #3573ee;
  text-align: center;
  margin-bottom: 32px;
}

.filter-toggle-row {
  display: flex;
  justify-content: flex-start;
  gap: 0.5rem;
  margin-top: 1rem;
}

.filter-toggle {
  border: 1px solid #ccc;
  border-radius: 20px;
  padding: 5px 12px;
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
  background: #0d3a95;
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
.reset-btn {
  padding: 5px 12px;
  background: #ef4444;
  color: #ffffff;
  border: none;
  border-radius: 20px;
  font-weight: 600;
  transition: all 0.2s ease;
}
.reset-btn:hover {
  cursor: pointer;
}
.reset-btn:active {
  background: #b91c1c; /* 누르고 있을 때 더욱 진한 빨강 */
}

@media (max-width: 1024px) {
  .filter-bar-wrapper {
    margin: 0 0 24px;
  }

  .filter-toggle {
    font-size: 13px;
  }

  .reset-btn {
    font-size: 13px;
  }

  .chip {
    font-size: 13px;
  }
}

@media (max-width: 768px) {
  .filter-toggle {
    font-size: 12px;
  }

  .reset-btn {
    font-size: 12px;
  }

  .chip {
    font-size: 12px;
  }
}
</style>
