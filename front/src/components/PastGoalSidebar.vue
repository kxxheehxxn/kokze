<template>
  <div class="sidebar-wrapper">
    <!-- 어두운 배경 오버레이 -->
    <div class="overlay" @click="$emit('close')"></div>

    <!-- 사이드바 본체 -->
    <div class="sidebar">
      <div class="sidebar-header">
        <h3>🌟 김콕재님의 지난 목표 리스트 🌟</h3>
      </div>

      <div class="past-goals">
        <div
          v-for="(goal, index) in pastGoals"
          :key="index"
          class="past-goal-card"
        >
          <div class="title">목표 : {{ goal.title }}</div>
          <hr />
          <div class="period">
            {{ goal.period1 }}<br />
            {{ goal.period2 }}
          </div>
          <div class="amount">{{ goal.amount }}</div>
          <div :class="['status', goal.success ? 'success' : 'fail']">
            {{ goal.success ? '성공' : '실패' }}
          </div>
        </div>
      </div>

      <div class="close-bottom-wrapper">
        <button class="close-btn" @click="$emit('close')">[ 닫기 ✕ ]</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PastGoalSidebar',
  data() {
    return {
      pastGoals: [
        {
          title: '노후준비',
          period1: '2025년 07월 15일 ~',
          period2: '2100년 01월 01일 (약 75년)',
          amount: '100억 원',
          success: false,
        },
        {
          title: '노후준비',
          period1: '2025년 07월 15일 ~',
          period2: '2100년 01월 01일 (약 75년)',
          amount: '100억 원',
          success: true,
        },
        {
          title: '노후준비',
          period1: '2025년 07월 15일 ~',
          period2: '2100년 01월 01일 (약 75년)',
          amount: '100억 원',
          success: true,
        },
        {
          title: '노후준비',
          period1: '2025년 07월 15일 ~',
          period2: '2100년 01월 01일 (약 75년)',
          amount: '100억 원',
          success: false,
        },
      ],
    };
  },
};
</script>

<style scoped>
.sidebar-wrapper {
  position: fixed;
  top: 0;
  right: 0;
  width: auto;
  height: 100vh;
  z-index: 1000;
  display: flex;
  justify-content: flex-end;
  pointer-events: none; /* 클릭 막음 */
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.08); /* 💡 약간 어두운 정도 */
  backdrop-filter: blur(1px); /* 💡 흐림 효과 */
  z-index: 999;
  pointer-events: auto;
  transition: none; /* 💡 즉시 흐려지게 */
}

.sidebar {
  width: 500px;
  height: 100%;
  background: #f9f9f9;
  padding: 1.5rem 1rem 4rem;
  border-radius: 12px;
  box-shadow: -3px 0 6px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  z-index: 1001;
  pointer-events: auto;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  margin-bottom: 1rem;
  text-align: center;
  font-size: 1.3rem;
  font-weight: bold;
}

.past-goals {
  margin-top: 1rem;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.past-goal-card {
  background: #fff;
  border: 1px solid #a2c3ff;
  border-radius: 12px;
  padding: 1.2rem;
  text-align: center;
  font-size: 1rem;
  box-shadow: 0 0 6px rgba(0, 120, 255, 0.15);
}

.title {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.period,
.amount {
  text-align: left;
}

.period {
  margin: 0.5rem 0;
  line-height: 1.4;
}
.amount {
  margin-bottom: 0.5rem;
}
.status {
  margin-top: 0.5rem;
  font-weight: bold;
  padding: 0.4rem;
  border-radius: 4px;
}
.success {
  color: green;
  background: #e6ffe6;
}
.fail {
  color: red;
  background: #ffeaea;
}
.close-bottom-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: auto;
  padding-top: 1.5rem;
}
.close-btn {
  background: #666;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 18px;
  border: none;
  font-size: 0.85rem;
  font-weight: bold;
  cursor: pointer;
}
</style>
