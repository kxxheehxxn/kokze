<template>
  <div class="goal-add-card">
    <div class="plus-icon">+</div>
    <p>목표 추가하기</p>
  </div>
</template>

<script>
export default {
  name: 'GoalAddCard',
};
</script>

<style scoped>
.goal-add-card {
  background: #ffffff;
  border: 2px dashed #007bff;
  border-radius: 12px;
  padding: 2rem 1rem;
  text-align: center;
  color: #007bff;
  cursor: pointer;
  transition: background 0.3s;
}
.goal-add-card:hover {
  background: #f0f8ff;
}
.plus-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}
</style>
