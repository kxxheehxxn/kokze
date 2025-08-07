<script setup>
import { ref, computed } from 'vue';
const props = defineProps({
  product: {
    type: Object,
    required: true,
  },
});
const isExpanded = ref(false);
// 백엔드에서 받은 데이터 기반으로 infoData 구성
const infoData = computed(() => [
  {
    label: '가입방법',
    value: props.product.joinWay || '정보 없음',
  },
  {
    label: '대상',
    value: props.product.joinMember || '정보 없음',
  },
  {
    label: '이자지급',
    value: props.product.mtrtInt || '정보 없음',
  },
  {
    label: '유의',
    value: props.product.etcNote || '정보 없음',
  },
  {
    label: '우대조건',
    value: props.product.spclCnd || '없음',
  },
  {
    label: '예금자 보호',
    value: '예금자보호법에 따라 예금 보호. 1인당 최고 5천만원까지 보호됨',
  },
]);
</script>
<template>
  <div class="info-box">
    <h2 class="title">상품 안내</h2>
    <hr />
    <div class="info-rows">
      <template v-if="isExpanded">
        <div v-for="item in infoData" :key="item.label" class="info-row">
          <div class="label">{{ item.label }}</div>
          <div class="value" v-html="item.value.replaceAll('\n', '<br />')" />
        </div>
      </template>
      <template v-else>
        <div v-for="item in infoData.slice(0, 4)" :key="item.label" class="info-row">
          <div class="label">{{ item.label }}</div>
          <div class="value" v-html="item.value.replaceAll('\n', '<br />')" />
        </div>
      </template>
    </div>
    <div class="more-btn-wrapper" v-if="!isExpanded">
      <button class="more-btn" @click="isExpanded = true">더보기<br></br><i class="fa-solid fa-chevron-down"></i></button>
    </div>
  </div>
</template>
<style scoped>
.info-box {
  background: white;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1.5rem;
  font-size: 1rem;
}
.title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}
.info-rows {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.info-row {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 1rem;
  align-items: start;
}
.label {
  font-weight: bold;
  color: #111;
  white-space: nowrap;
}
.value {
  color: #333;
  line-height: 1.6;
  word-break: keep-all;
}
.more-btn-wrapper {
  text-align: center;
  margin-top: 2rem;
}
.more-btn {
  border: 0 solid #ccc;
  padding: 0.6rem 1.2rem;
  border-radius: 0.5rem;
  background: white;
  font-weight: bold;
}
.more-btn:hover {
  cursor: pointer;
  transform: translateY(-2px);
  transition: all 0.2s ease;
}
</style>
