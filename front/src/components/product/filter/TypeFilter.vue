<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: Array,
});
const emit = defineEmits(['update:modelValue']);

const types = [
  '예금',
  '적금',
  '누구나가입',
  '청년적금',
  '군인적금',
  '주택청약',
  '자유적금',
  '정기적금',
  '청년도약계좌',
];

const selectedTypes = ref([...props.modelValue]);

// 외부 변경 감지
watch(
  () => props.modelValue,
  (val) => {
    selectedTypes.value = [...val];
  }
);

// 클릭 시 토글
function toggleType(type) {
  if (selectedTypes.value.includes(type)) {
    selectedTypes.value = selectedTypes.value.filter((t) => t !== type);
  } else {
    selectedTypes.value.push(type);
  }
  emit('update:modelValue', selectedTypes.value);
  emit('change');
}
</script>

<template>
  <div class="type-grid">
    <div
      v-for="type in types"
      :key="type"
      :class="['type-cell', { selected: selectedTypes.includes(type) }]"
      @click="toggleType(type)"
    >
      {{ type }}
    </div>
  </div>
</template>

<style scoped>
.type-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border: 1px solid #ddd;
  border-radius: 0.75rem;
  overflow: hidden;
}
.type-cell {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 0.5rem;
  font-size: 1rem;
  border-right: 1px solid #eee;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  position: relative;
}
.type-cell:nth-child(3n) {
  border-right: none;
}
.type-cell:nth-last-child(-n + 3) {
  border-bottom: none;
}
.selected {
  font-weight: bold;
  background-color: #e0f0ff;
  color: #1d4ed8;
}
</style>
