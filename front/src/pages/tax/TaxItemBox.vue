<template>
  <div class="col">
    <button
      class="btn w-100"
      :class="active ? 'btn-primary' : 'btn-outline-secondary'"
      @click="$emit('click')"
    >
      <div>{{ label }}</div>
      <div><strong>{{ amount }}</strong></div>
    </button>
  </div>
</template>

<script setup>
defineProps({
  label: String,
  amount: String,
  active: Boolean,
})
</script>
