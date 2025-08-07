<script setup>
import { ref, watch } from 'vue';
const props = defineProps({
  modelValue: Array,
});
const emit = defineEmits(['update:modelValue']);
const conditions = [
  '비대면가입',
  '은행앱사용',
  '급여연동',
  '연금',
  '우대',
  '카드사용',
  '첫거래',
  '입출금통장',
  '재예치',
];
const selectedConditions = ref([...props.modelValue]);
// 외부 변경 감지
watch(
  () => props.modelValue,
  (val) => {
    selectedConditions.value = [...val];
  }
);
// 클릭 시 토글
function toggleCondition(condition) {
  if (selectedConditions.value.includes(condition)) {
    selectedConditions.value = selectedConditions.value.filter(
      (c) => c !== condition
    );
  } else {
    selectedConditions.value.push(condition);
  }
  emit('update:modelValue', selectedConditions.value);
  emit('change');
}
</script>
<template>
  <div class="condition-filter">
    <div class="condition-grid">
      <div
        v-for="condition in conditions"
        :key="condition"
        :class="[
          'condition-cell',
          { selected: selectedConditions.includes(condition) },
        ]"
        @click="toggleCondition(condition)"
      >
        {{ condition }}
      </div>
    </div>
    <p class="footnote">*신협 상품에는 적용되지 않습니다</p>
  </div>
</template>
<style scoped>
.condition-filter {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.condition-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border: 1px solid #ddd;
  border-radius: 0.75rem;
  overflow: hidden;
}
.condition-cell {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 0.5rem;
  font-size: 1rem;
  border-right: 1px solid #eee;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.condition-cell:nth-child(3n) {
  border-right: none;
}
.condition-cell:nth-last-child(-n + 3) {
  border-bottom: none;
}
.selected {
  font-weight: bold;
  background-color: #e0f0ff;
  color: #1d4ed8;
}
.footnote {
  font-size: 0.75rem;
  color: #888;
  margin-left: 0.2rem;
}
</style>
