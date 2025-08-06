<script setup>
import { ref } from 'vue';
import { bankNameMap } from '@/utils/bankMap'; // 한글 은행명을 영문 파일명으로 매핑

// 부모 컴포넌트에서 전달되는 modelValue (v-model)
const props = defineProps({
  modelValue: Array,
});

// v-model 업데이트를 부모로 emit하기 위한 선언
const emit = defineEmits(['update:modelValue']);

// 현재 선택된 은행 목록을 로컬 상태로 보관
const selectedBanks = ref([...props.modelValue]);

// 은행 이름 목록을 map 돌려 아이콘 이름까지 포함한 배열로 생성
const bankList = Object.keys(bankNameMap).map((name) => ({
  name,
  iconName: bankNameMap[name],
}));

// 아이콘 이미지 파일들을 모두 가져오기
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
  eager: true,
  import: 'default',
});

// 은행 이름(영문)을 받아 아이콘 경로를 반환
const getBankIcon = (iconName) => {
  const match = Object.entries(iconModules).find(([path]) => path.includes(`/${iconName}.png`));
  return match ? match[1] : '';
};

// 버튼 클릭 시 은행 선택 토글 처리
function toggleBank(name) {
  const newValue = props.modelValue.includes(name)
    ? props.modelValue.filter((b) => b !== name)
    : [...props.modelValue, name];
  emit('update:modelValue', newValue);
  emit('change');
}
</script>

<template>
  <div class="bank-filter-wrapper">
    <div class="bank-scroll">
      <button
        v-for="bank in bankList"
        :key="bank.name"
        class="bank-box"
        :class="{ active: props.modelValue.includes(bank.name) }"
        @click="toggleBank(bank.name)"
      >
        <img :src="getBankIcon(bank.iconName)" :alt="bank.name" />
        <div class="bank-label">{{ bank.name }}</div>
      </button>
    </div>
  </div>
</template>

<style scoped>
.bank-filter-wrapper {
  overflow-x: auto;
  padding: 0 1rem;

  /* 스크롤바 항상 표시 */
  scrollbar-width: auto;
  -ms-overflow-style: auto;
}

.bank-filter-wrapper::-webkit-scrollbar {
  height: 8px; /* 스크롤바 높이 */
}

.bank-filter-wrapper::-webkit-scrollbar-track {
  background: #f0f0f0; /* 트랙 배경 */
  border-radius: 4px;
}

.bank-filter-wrapper::-webkit-scrollbar-thumb {
  background: #3573ee; /* 스크롤바 색상 */
  border-radius: 4px;
}

.bank-scroll {
  display: flex;
  gap: 1rem;
  padding: 0.5rem 0;
  min-width: max-content;
}

.bank-box {
  background: #ffffff;
  border: 1px solid #ddd;
  border-radius: 20px;
  width: 100px;
  height: 90px;
  padding: 0.5rem 0.3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: none;
  transition: box-shadow 0.2s, border-color 0.2s;
  flex-shrink: 0;
}

.bank-box:hover {
  cursor: pointer;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.bank-box img {
  width: 32px;
  height: 32px;
  object-fit: contain;
  margin-bottom: 0.4rem;
}

.bank-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: #333;
}

.bank-box.active {
  box-shadow: inset 0 0 5px #3573ee;
  border-color: #3573ee;
}
</style>
