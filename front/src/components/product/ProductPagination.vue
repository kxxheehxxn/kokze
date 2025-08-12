<script setup>
import { computed } from 'vue';
const props = defineProps({
  totalPages: Number,
  page: Number,
});
const emit = defineEmits(['update:page']);
const pages = computed(() => {
  const arr = [];
  for (let i = 1; i <= props.totalPages; i++) {
    arr.push(i);
  }
  return arr;
});
function goTo(page) {
  if (page >= 1 && page <= props.totalPages) {
    emit('update:page', page);
  }
}
</script>

<template>
  <div class="pagination">
    <!-- << 맨 처음 페이지 -->
    <button @click="goTo(1)" :disabled="page === 1">
      <i class="fa-solid fa-angles-left" />
    </button>
    <!-- 이전 페이지 -->
    <button @click="goTo(page - 1)" :disabled="page <= 1">
      <i class="fa-solid fa-angle-left" />
    </button>

    <button
      v-for="p in pages"
      :key="p"
      @click="goTo(p)"
      :class="{ active: p === page }"
    >
      {{ p }}
    </button>
    <!-- 다음 페이지 -->
    <button @click="goTo(page + 1)" :disabled="page >= totalPages">
      <i class="fa-solid fa-angle-right" />
    </button>
    <!-- >> 맨 마지막 페이지 -->
    <button @click="goTo(totalPages)" :disabled="page === totalPages">
      <i class="fa-solid fa-angles-right" />
    </button>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 0.3rem;
  margin-top: 2rem;
}
button {
  padding: 0.4rem 0.8rem;
  border: none;
  background: white;
  border-radius: 0.3rem;
  cursor: pointer;
}
button.active {
  color: black;
  font-weight: bold;
}
button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
</style>
