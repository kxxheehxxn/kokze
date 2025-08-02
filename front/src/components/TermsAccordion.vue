<template>
  <div class="terms-accordion">
    <div
      v-for="term in terms"
      :key="term.id"
      :class="['accordion-item', { selected: openedId === term.id }]"
    >
      <div class="accordion-title" @click="toggle(term.id)">
        <span class="icon">{{ openedId === term.id ? '▼' : '▶' }}</span>
        <span class="question">Q. {{ term.title }}</span>
      </div>
      <div v-if="openedId === term.id" class="accordion-content">
        <span class="answer">A. {{ term.description }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const props = defineProps({
  terms: Array,
})
const openedId = ref(null)
function toggle(id) {
  openedId.value = openedId.value === id ? null : id
}
</script>

<style scoped>
.terms-accordion {
  flex: 1;
  background: #f6f6f6;
}
.accordion-item {
  border: 2px solid transparent;
  border-radius: 18px;
  margin-bottom: 20px;
  background: #fff;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  transition: border 0.2s;
  padding: 0;
}
.accordion-item.selected {
  border: 2px solid #2563eb;
}
.accordion-title {
  padding: 24px 32px 18px 32px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 22px;
  gap: 12px;
  border-radius: 18px;
}
.icon {
  font-size: 20px;
  color: #222;
  width: 24px;
  display: inline-block;
}
.question {
  font-weight: bold;
}
.accordion-content {
  padding: 0 32px 24px 56px;
  font-size: 18px;
  color: #222;
  border-radius: 18px;
}
.answer {
  display: block;
  margin-top: 4px;
  line-height: 1.7;
}
</style>
