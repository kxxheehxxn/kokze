<template>
  <div class="goal-add-card" @click="loadRecommendedGoal">
    <div class="plus-icon">+</div>
    <p>목표 추가하기</p>
    <GoalRecommendModal
      v-if="showModal"
      :recommendData="recommendData"
      @accept="goToRecommended"
      @reject="goToManual"
      @close="showModal = false"
    />
  </div>
</template>
<script>
import GoalRecommendModal from '@/components/GoalRecommendModal.vue';
import { fetchRecommendedGoal } from '@/api/goalApi';
import { userAuthStore } from '@/stores/auth';
export default {
  components: { GoalRecommendModal },
  data() {
    return {
      showModal: false,
      recommendData: null,
    };
  },
  methods: {
    async loadRecommendedGoal() {
      try {
        const userId = userAuthStore().state.user.userId;
        const data = await fetchRecommendedGoal(userId);
        this.recommendData = data;
        this.showModal = true;
      } catch (err) {
        console.error('추천 목표 불러오기 실패:', err);
        this.$router.push({ name: 'GoalCreatePage' });
      }
    },
    goToRecommended() {
      const r = this.recommendData;
      this.$router.push({
        name: 'GoalCreatePage',
        query: {
          amount: r.recommendedAmount,
          start: r.recommendedStartDate,
          end: r.recommendedEndDate,
        },
      });
    },
    goToManual() {
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
