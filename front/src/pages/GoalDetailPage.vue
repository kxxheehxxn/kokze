<template>
  <div class="goal-detail-page">
    <router-link to="/goal" class="back-link">← 목표로 돌아가기</router-link>

    <!-- 목표 타이틀 및 달성률 -->
    <section class="summary-card">
      <div class="title-row">
        <h3>목표: {{ goal.title }} ({{ goal.progress }}%)</h3>
        <div class="progress-area">
          <div class="progress-bar">
            <div class="progress" :style="{ width: goal.progress + '%' }"></div>
          </div>
          <span class="progress-percent">{{ goal.progress }}%</span>
        </div>
      </div>

      <div class="details">
        <p>
          <strong>목표 기간:</strong> {{ goal.period1 }} ~ {{ goal.period2 }}
        </p>
        <p>
          <strong>목표 금액:</strong> {{ goal.savedAmount.toLocaleString() }} 원
          / {{ goal.totalAmount.toLocaleString() }} 원
        </p>
        <p><strong>연결된 금융 상품:</strong> {{ goal.product || '-' }}</p>
      </div>

      <div class="button-row">
        <button
          class="btn btn-primary"
          @click="
            $router.push({ name: 'GoalEditPage', params: { goalId: goal.id } })
          "
        >
          목표 수정하기
        </button>

        <button class="btn btn-danger" @click="handleDeleteGoal">
          목표 삭제하기
        </button>
      </div>
    </section>

    <section class="recommendation-section" v-if="recommended.length">
      <h4>✨ {{ userName }}님에게 추천하는 맞춤 금융 상품 ✨</h4>
      <div class="product-grid">
        <div
          v-for="(product, idx) in recommended"
          :key="idx"
          class="product-card"
        >
          <p>금융사: {{ product.korCoNm }}</p>
          <p>상품명: {{ product.finPrdtNm }}</p>
          <p>
            ✨ 적립 유형: {{ product.rsrvTypeNm }} / {{ product.saveTrm }}개월
          </p>
          <p>기본 금리: {{ product.intrRate }}%</p>
          <p>우대 금리: {{ product.intrRate2 }}%</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import {
  getGoalById,
  getRecommendedProducts,
  deleteGoalById,
} from '@/api/goalApi';

import { userAuthStore } from '@/stores/auth';

export default {
  name: 'GoalDetailPage',
  data() {
    return {
      goal: {
        id: '',
        title: '',
        progress: 0,
        period1: '',
        period2: '',
        savedAmount: 0,
        totalAmount: 0,
        product: '',
      },
      recommended: [],
      userName: '김콕재',
    };
  },
  async created() {
    const goalId = this.$route.params.goalId;
    const auth = userAuthStore();
    const token = auth.getToken();

    try {
      const response = await getGoalById(goalId, token);
      const data = response.data;

      const progress = data.target_amount
        ? Math.floor((data.current_amount / data.target_amount) * 100)
        : 0;

      this.goal = {
        id: data.goal_id,
        title: data.goal_name,
        progress,
        period1: data.start_date,
        period2: data.end_date,
        savedAmount: data.current_amount,
        totalAmount: data.target_amount,
        product: data.linked_accounts?.[0]?.product_name || '-',
      };

      const recommendRes = await getRecommendedProducts(goalId, token);
      this.recommended = recommendRes.data;
    } catch (err) {
      console.error('❌ 상세 조회 실패:', err);
    }
  },
  methods: {
    async handleDeleteGoal() {
      const confirmDelete = confirm('정말로 이 목표를 삭제하시겠습니까?');
      if (!confirmDelete) return;

      const auth = userAuthStore();
      const token = auth.getToken();

      try {
        await deleteGoalById(this.goal.id, token);
        alert('목표가 삭제되었습니다.');
        this.$router.push('/goals');
      } catch (error) {
        console.error('❌ 목표 삭제 실패:', error);
        alert('삭제 중 오류가 발생했습니다.');
      }
    },
  },
};
</script>

<style scoped>
/* 그대로 유지 */
.goal-detail-page {
  padding: 2rem;
}
.back-link {
  color: #007bff;
  text-decoration: none;
  margin-bottom: 1rem;
  display: inline-block;
}
.summary-card {
  background: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
.title-row {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.progress-area {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.progress-bar {
  flex: 1;
  height: 20px;
  background: #eee;
  border-radius: 10px;
  overflow: hidden;
}
.progress {
  height: 100%;
  background: linear-gradient(90deg, red, orange, green);
}
.progress-percent {
  font-weight: bold;
}
.details {
  margin-top: 1rem;
  line-height: 1.6;
}
.button-row {
  margin-top: 1.5rem;
  display: flex;
  gap: 1rem;
}
.recommendation-section {
  margin-top: 2rem;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}
.product-card {
  background: #fff;
  border: 1px solid #a2c3ff;
  border-radius: 12px;
  padding: 1rem;
  text-align: center;
  box-shadow: 0 0 6px rgba(0, 120, 255, 0.1);
}
.reason {
  font-size: 0.9rem;
  color: #555;
  margin-top: 0.5rem;
}
</style>
