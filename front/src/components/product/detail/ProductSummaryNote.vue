<script setup>
const props = defineProps({
  product: Object,
});

// AI 요약이 없다면 안내 메시지로 대체
const summaryText =
  props.product.summary?.trim() ||
  `이 상품에 대한 요약 정보가 존재하지 않습니다.`;
</script>

<template>
  <div class="summary-note-box">
    <div class="header">
      <div class="title-row">
        <span>✨</span>
        <span class="title">상품 3줄 요약</span>
        <span>✨</span>
      </div>
      <div class="desc">
        해당 요약은 AI가 자동 생성한 설명입니다. 실제 상품 약관과는 다를 수
        있습니다.
      </div>
    </div>
    <hr />
    <div class="content">
      <p>{{ summaryText }}</p>
    </div>
  </div>
</template>

<style scoped>
.summary-note-box {
  background: white;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: inset 0 0 12px #a1c4fd;
  margin-top: 1.5rem;
  font-size: 1rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 1rem;
}

.title-row {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
  font-weight: bold;
  font-size: 1.5rem;
}

.desc {
  font-size: 0.9rem;
  color: #666;
  white-space: nowrap;
}

.content p {
  white-space: pre-wrap;
  line-height: 1.6;
  font-size: 1rem;
  color: #222;
}
</style>
