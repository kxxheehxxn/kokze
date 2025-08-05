<template>
  <div class="goal-add-card" @click="goToCreate">
    <div class="plus-icon">+</div>
    <p>목표 추가하기</p>
  </div>
</template>

<script>
export default {
  methods: {
    goToCreate() {
      this.$router.push({ name: 'GoalCreatePage' });
    },
  },
};
</script>

<style scoped>
.goal-add-card {
  background: #fff;
  border-radius: 1rem;
  border: 2px dashed #007bff;
  padding: 2rem 1rem;
  text-align: center;
  color: #007bff;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
  height: 100%;
  min-height: 150px;
  box-sizing: border-box;
}

.goal-add-card:hover {
  background: #f0f8ff;
}
.plus-icon {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}
</style>
