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
    <button @click="goTo(page - 1)" :disabled="page <= 1">＜</button>
    <button
      v-for="p in pages"
      :key="p"
      @click="goTo(p)"
      :class="{ active: p === page }"
    >
      {{ p }}
    </button>
    <button @click="goTo(page + 1)" :disabled="page >= totalPages">＞</button>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

button {
  padding: 0.4rem 0.8rem;
  border: 1px solid #ccc;
  background: white;
  border-radius: 0.3rem;
  cursor: pointer;
}

button.active {
  background: #1d4ed8;
  color: white;
  font-weight: bold;
}

button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
</style>
