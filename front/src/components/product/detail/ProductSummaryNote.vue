<script setup>
const props = defineProps({
  product: { type: Object, required: true },
  loadingSummary: { type: Boolean, default: false },
})
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
      <p v-if="loadingSummary || !product?.summary">요약 생성 중…</p>
      <p v-else>{{ product.summary }}</p>
    </div>
  </div>
</template>

<style scoped>
.summary-note-box {
  background: #fff;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-top: 1.5rem;
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
  font-weight: 700;
  font-size: 1.25rem;
}
.desc {
  font-size: 0.9rem;
  color: #666;
  white-space: nowrap;
}
.content p {
  white-space: pre-wrap;
  line-height: 1.6;
  color: #222;
}
</style>
