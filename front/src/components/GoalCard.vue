<template>
  <div class="goal-card">
    <h3>{{ goal.title }}</h3>
    <hr></hr>
    <p>기간:<br />{{ goal.period1 }}<br />{{ goal.period2 }}</p>
    <p>금액: {{ goal.amount }}</p>
    <div class="progress-box">
      <div class="progress-bar">
        <div
          class="progress-fill"
          :style="{ width: goal.progress + '%' }"
        ></div>
      </div>
      <span>{{ goal.progress }}%</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GoalCard',
  props: {
    goal: Object,
  },
};
</script>

<style scoped>
.goal-card {
  background: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 12px;
  padding: 1rem;
  text-align: center;
}
.progress-box {
  margin-top: 0.5rem;
}
.progress-bar {
  height: 10px;
  background: #ddd;
  border-radius: 5px;
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background: #4caf50;
}

</style>
