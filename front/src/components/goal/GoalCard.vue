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
        params: { goalId: this.goal.id ?? this.goal.goal_id },
      })
    },

    // 어떤 형태든 Date 객체로 변환: [y,m,d] | "YYYY-MM-DD" | Date
    toDate(val) {
      if (!val) return null
      if (val instanceof Date) return val
      if (Array.isArray(val) && val.length === 3) {
        const [y, m, d] = val
        return new Date(Number(y), Number(m) - 1, Number(d))
      }
      if (typeof val === 'string') {
        const parts = val.split(/[-/.]/)
        if (parts.length >= 3) {
          return new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]))
        }
        const d = new Date(val)
        return isNaN(d) ? null : d
      }
      return null
    },

    formatDate(input) {
      const d = this.toDate(input)
      if (!d) return ''
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}년 ${m}월 ${day}일`
    },

    getPeriodDiff(start, end) {
      const s = this.toDate(start)
      const e = this.toDate(end)
      if (!s || !e) return ''

      let diffMonths =
        (e.getFullYear() - s.getFullYear()) * 12 +
        (e.getMonth() - s.getMonth())

      if (e.getDate() > s.getDate()) diffMonths += 1

      return diffMonths < 24
        ? `${diffMonths}개월`
        : `${Math.round(diffMonths / 12)}년`
    },
  },
}
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
