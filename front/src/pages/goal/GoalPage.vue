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
          <div class="progress" :style="{ width: averageProgress + '%' }"></div>
        </div>
        <span class="percent">{{ averageProgress }}%</span>
      </div>
      <div class="goal-grid">
        <div v-for="goal in goals" :key="goal.id" class="goal-wrapper">
          <GoalCard :goal="goal" />
          <div class="product-box">{{ goal.product || '-' }}</div>
        </div>

        <div v-for="n in emptySlots" :key="'add-' + n" class="goal-wrapper">
          <GoalAddCard />
          <div class="product-box">-</div>
        </div>
      </div>
    </section>

    <transition name="sidebar-fade">
      <PastGoalSidebar v-if="showSidebar" @close="showSidebar = false" />
    </transition>
  </div>
</template>

<script>
import { fetchGoals } from '@/api/goalApi';
import { userAuthStore } from '@/stores/auth';
import GoalCard from '@/components/goal/GoalCard.vue';
import GoalAddCard from '@/components/goal/GoalAddCard.vue';
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
      goals: [],
      maxGoals: 5,
      showSidebar: false,
    };
  },
  computed: {
    emptySlots() {
      return this.maxGoals - this.goals.length;
    },
    averageProgress() {
      if (!this.goals.length) return 0;
      const total = this.goals.reduce((sum, g) => sum + g.progress, 0);
      return Math.floor(total / this.goals.length);
    },
  },
  created() {
    this.fetchGoals();
  },
  methods: {
    async fetchGoals() {
      const auth = userAuthStore();
      const userId = auth.state.user.userId;
      const token = auth.getToken();

      try {
        const response = await fetchGoals(userId, token);

        if (!Array.isArray(response.data)) {
          console.error('🚨 응답이 배열이 아닙니다:', response.data);
          return;
        }

        this.goals = response.data.map((goal) => ({
          id: goal.goal_id,
          title: goal.goal_name,
          amount: `${goal.target_amount.toLocaleString()} 원`,
          progress: this.calculateProgress(
            goal.save_amount,
            goal.target_amount
          ),
          product: '-',
          period1: goal.start_date ?? '',
          period2: goal.end_date ?? '',
        }));
      } catch (error) {
        console.error('❌ 목표 불러오기 실패:', error);
      }
    },
    calculateProgress(saved, target) {
      if (!saved || !target) return 0;
      return Math.floor((saved / target) * 100);
    },
  },
};
</script>

<style scoped>
/* 동일한 스타일 유지 */
.goal-page {
  padding: 2rem;
}
.goal-summary {
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1rem;
}

/* 타이틀 */
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

/* 목표 평균 달성률 */
.average-progress {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 0 auto 2rem;
  font-size: 1.2rem;

  max-width: 1300px;
  padding: 0.5rem 0;
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

/* 5가지 목표 리스트 */
.goal-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 1rem;
}

.goal-wrapper {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
}

.product-box {
  border: 1px solid #a2c3ff;
  border-radius: 12px;
  background: #fff;
  margin-top: 0.5rem;
  padding: 0.8rem;
  text-align: center;
  font-size: 0.9rem;
  color: #111;
  box-shadow: 0 0 6px rgba(0, 120, 255, 0.15);
  height: 50px;
  box-sizing: border-box;
}

/* 지난 목표 리스트 이동 */
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

@media (max-width: 1024px) {
  .goal-summary {
    margin-top: 0rem;
  }
  /* 목표 카드 & 연결된 계좌 카드 → 2열로 */
  .goal-grid,
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 0.8rem;
  }

  /* 카드 내부 여백 및 글씨 크기 조정 */
  .goal-card,
  .goal-add-card,
  .product-box {
    padding: 1.5rem;
    font-size: 0.95rem;
  }
}
</style>
