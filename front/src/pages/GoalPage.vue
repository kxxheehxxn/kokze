<template>
  <div class="goal-page">
    <section class="goal-summary">
      <div class="header-row">
        <h2>전체 목표 관리</h2>
        <button class="past-goal-button" @click="showSidebar = true">
          지난 목표 리스트
        </button>
      </div>

      <div class="average-progress">
        <span>목표 평균 달성률</span>
        <div class="progress-bar">
          <div class="progress" :style="{ width: '16%' }"></div>
        </div>
        <span class="percent">16%</span>
      </div>

      <div class="goal-grid">
        <GoalCard v-for="goal in goals" :key="goal.id" :goal="goal" />
        <GoalAddCard v-for="n in emptySlots" :key="'add-' + n" />
      </div>

      <div class="product-grid">
        <div
          v-for="goal in goals"
          :key="'product-' + goal.id"
          class="product-box"
        >
          {{ goal.product || '-' }}
        </div>
        <div
          v-for="n in emptySlots"
          :key="'product-empty-' + n"
          class="product-box"
        ></div>
      </div>
    </section>

    <!-- ✅ 트랜지션으로 감싸기 -->
    <transition name="sidebar-fade">
      <PastGoalSidebar v-if="showSidebar" @close="showSidebar = false" />
    </transition>
  </div>
</template>

<script>
import GoalCard from '@/components/GoalCard.vue';
import GoalAddCard from '@/components/GoalAddCard.vue';
import PastGoalSidebar from '@/components/PastGoalSidebar.vue';

export default {
  name: 'GoalPage',
  components: {
    GoalCard,
    GoalAddCard,
    PastGoalSidebar,
  },
  data() {
    return {
      goals: [
        {
          id: 1,
          title: '노후준비',
          period1: '2025.07.15 ~',
          period2: '2100.01.01 (약 75년)',
          amount: '100억 원',
          progress: 9,
          product: '(적금) 매달 5일 20만원',
        },
        {
          id: 2,
          title: '내 집 마련',
          period1: '2025.07.15 ~',
          period2: '2100.01.01 (약 75년)',
          amount: '100억 원',
          progress: 21,
          product: '(예금) 200만원',
        },
        {
          id: 3,
          title: '대출 갚기',
          period1: '2025.07.15 ~',
          period2: '2100.01.01 (약 75년)',
          amount: '10억 원',
          progress: 18,
          product: '(펀드) 뭐가 될지 몰라',
        },
      ],
      maxGoals: 5,
      showSidebar: false,
    };
  },
  computed: {
    emptySlots() {
      return this.maxGoals - this.goals.length;
    },
  },
};
</script>

<style scoped>
.goal-page {
  padding: 2rem;
  font-family: 'Noto Sans KR', sans-serif;
}
.goal-summary {
  background: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.past-goal-button {
  background: #666;
  color: #fff;
  border: none;
  border-radius: 16px;
  padding: 0.3rem 1rem;
  font-size: 0.85rem;
  cursor: pointer;
}
.average-progress {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
}
.progress-bar {
  flex: 1;
  height: 16px;
  background: #eee;
  border-radius: 8px;
  overflow: hidden;
}
.progress {
  height: 100%;
  background: linear-gradient(90deg, red, orange, green);
}
.percent {
  font-weight: bold;
}
.goal-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 1rem;
  margin-bottom: 1rem;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 1rem;
}
.product-box {
  border: 1px solid #a2c3ff;
  border-radius: 12px;
  background: #fff;
  padding: 0.8rem;
  text-align: center;
  font-size: 0.9rem;
  color: #111;
  box-shadow: 0 0 6px rgba(0, 120, 255, 0.15);
}

/* ✅ 애니메이션 정의 */
.sidebar-fade-enter-active,
.sidebar-fade-leave-active {
  transition: all 0.2s ease;
}
.sidebar-fade-enter-from,
.sidebar-fade-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
.sidebar-fade-enter-to,
.sidebar-fade-leave-from {
  transform: translateX(0);
  opacity: 1;
}
</style>
