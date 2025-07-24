<template>
  <div class="terms-sidebar">
    <ul>
      <li
        v-for="cat in categories"
        :key="cat"
        :class="{ active: modelValue === cat }"
        @click="$emit('update:modelValue', cat)"
      >
        {{ cat }}
      </li>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  categories: Array,
  modelValue: String,
})
const emit = defineEmits(['update:modelValue'])
</script>

<style scoped>
.terms-sidebar {
  min-width: 140px;
  background: #fff;
  border-radius: 0 18px 18px 0;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  padding: 36px 0 36px 0;
  margin: 40px 0 0 0;
}
.terms-sidebar ul {
  list-style: none;
  padding: 0 0 0 24px;
  margin: 0;
}
.terms-sidebar li {
  padding: 16px 0;
  cursor: pointer;
  color: #222;
  font-size: 20px;
  font-weight: 500;
  transition: color 0.2s;
}
.terms-sidebar li.active {
  color: #2563eb;
  font-weight: bold;
}
</style>
