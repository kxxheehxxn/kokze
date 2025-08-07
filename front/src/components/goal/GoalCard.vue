<template>
  <div class="goal-card" @click="goToDetail">
    <h3 class="goal-title">{{ goal.title }}</h3>
    <hr />
    <div class="goal-info">
      <p class="goal-box">
        <span class="label">기간</span>
        <span class="value">
          {{ formatDate(goal.period1) }} ~ <br />
          {{ formatDate(goal.period2) }}<br />
          ( 약 {{ getPeriodDiff(goal.period1, goal.period2) }} )
        </span>
      </p>
      <p class="goal-box">
        <span class="label">금액</span>
        <span class="value">{{ goal.amount }}</span>
      </p>
    </div>
    <div class="goal-progress">
      <span class="progress-value">{{ goal.progress }}%</span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'GoalCard',
  props: {
    goal: Object,
  },
  methods: {
    goToDetail() {
      this.$router.push({
        name: 'GoalDetailPage',
        params: { goalId: this.goal.id },
      });
    },
    formatDate(dateStr) {
      if (!dateStr || dateStr.length !== 3) return '';
      const [y, m, d] = dateStr;
      return `${y}년 ${String(m).padStart(2, '0')}월 ${String(d).padStart(
        2,
        '0'
      )}일`;
    },
    getPeriodDiff(start, end) {
      if (!start || !end) return '';
      const startDate = new Date(start);
      const endDate = new Date(end);
      let diffMonths =
        (endDate.getFullYear() - startDate.getFullYear()) * 12 +
        (endDate.getMonth() - startDate.getMonth());
      // 일(day) 차이가 양수면 한 달 추가
      if (endDate.getDate() > startDate.getDate()) {
        diffMonths += 1;
      }
      if (diffMonths < 24) {
        return `${diffMonths}개월`;
      } else {
        const diffYears = diffMonths / 12;
        return `${Math.round(diffYears)}년`;
      }
    },
  },
};
</script>
<style scoped>
.goal-card {
  background: #fff;
  padding: 1.5rem 1.7rem;
  border-radius: 1rem;
  box-shadow: inset 0 0 12px #3573ee;
  cursor: pointer;
  transition: transform 0.15s ease;
  height: 100%;
  min-height: 150px;
  box-sizing: border-box;
}
.goal-card:hover {
  box-shadow: inset 0 0 20px #3573ee;
}
.goal-title {
  font-weight: bold;
  font-size: 1.5rem;
  margin-bottom: 0.8rem;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
  max-width: 100%;
}
.goal-info {
  font-size: 1rem;
  color: #333;
  line-height: 1.4;
  margin-bottom: 1.5rem;
}
.goal-box {
  display: flex;
  align-items: flex-start;
}
.goal-box .label {
  width: 2.8rem;
  font-weight: 600;
  color: #555;
}
.goal-box .value {
  display: inline-block;
}
.goal-progress {
  text-align: right;
}
.progress-value {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
