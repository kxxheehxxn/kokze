<template>
  <div class="goal-detail-page">
    <router-link to="/goals" class="back-link">← 목표로 돌아가기</router-link>
    <section class="summary-card">
      <div class="header-row">
        <h2 class="goal-title">
          목표: {{ goal.title }} ({{ goal.progress }}%)
        </h2>
      </div>
      <hr />
      <div class="progress-area">
        <div class="progress-bar">
          <div class="progress" :style="{ width: goal.progress + '%' }"></div>
          <div class="progress-markers">
            <span
              v-for="mark in [0, 25, 50, 75, 100]"
              :key="mark"
              class="marker"
              :style="{ left: mark + '%' }"
            >
              <div v-if="mark !== 0 && mark !== 100" class="marker-line"></div>
              <div class="marker-label">{{ mark }}%</div>
            </span>
          </div>
        </div>
        <img src="/src/assets/images/gift.png" alt="목표 달성 선물 이미지" />
      </div>
      <div class="details">
        <div class="detail-row">
          <span class="label">목표 기간</span>
          <span class="value">
            {{ formatDate(goal.period1) }} ~ {{ formatDate(goal.period2) }} ( 약
            {{ getPeriodDiff(goal.period1, goal.period2) }} )
          </span>
        </div>
        <div class="detail-row">
          <span class="label">목표 금액</span>
          <span class="value">
            {{ goal.savedAmount.toLocaleString() }} 원 /
            {{ goal.totalAmount.toLocaleString() }} 원
          </span>
        </div>
        <div class="detail-row">
          <span class="label">연결된 금융 상품</span>
          <span class="value">
            <template v-if="goal.linked_accounts.length > 0">
              {{ goal.linked_accounts[0].bank_name }} -
              {{ goal.linked_accounts[0].account_num }}
            </template>
            <template v-else>-</template>
          </span>
        </div>
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
        <RecommendedProductCard
          v-for="(product, idx) in recommended"
          :key="idx"
          :product="product"
          @click="$router.push(`/product/${product.finPrdtCd}`)"
        />
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
import RecommendedProductCard from '@/components/goal/RecommendedProductCard.vue';
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
        depositDate: '',
        product: '',
        linked_accounts: [],
      },
      recommended: [],
      userName: '김콕재',
    };
  },
  components: {
    RecommendedProductCard,
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
        depositDate: data.deposit_date,
        linked_accounts: data.linked_accounts || [],
        product: data.linked_accounts?.[0]?.product_name || '-',
      };
      const recommendRes = await getRecommendedProducts(goalId, token);
      this.recommended = recommendRes.data;
    } catch (err) {
      console.error('Failed to load details:', err);
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
        console.error('Failed to delete goal:', error);
        alert('삭제 중 오류가 발생했습니다.');
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return `${date.getFullYear()}년 ${
        date.getMonth() + 1
      }월 ${date.getDate()}일`;
    },
    getPeriodDiff(start, end) {
      if (!start || !end) return '';
      const startDate = new Date(start);
      const endDate = new Date(end);
      let diffMonths =
        (endDate.getFullYear() - startDate.getFullYear()) * 12 +
        (endDate.getMonth() - startDate.getMonth());
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
.goal-detail-page {
  padding: 2rem;
}
.back-link {
  color: #007bff;
  text-decoration: none;
  margin: 1rem;
  display: inline-block;
}
/* 박스 */
.summary-card {
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
/* 타이틀 */
.header-row {
  margin-bottom: 0.5rem;
}
/* 진행률 영역 */
.progress-area,
.details,
.button-row {
  padding: 1rem 10rem;
}
.progress-area {
  position: relative;
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
}
.progress-bar {
  position: relative;
  flex: 1;
  height: 20px;
  background: #eee;
  border-radius: 10px;
  overflow: visible;
}
.progress {
  height: 100%;
  border-radius: 10px;
  background: linear-gradient(90deg, #f44336, #ff9800, #4caf50);
}
.progress-markers {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  pointer-events: none;
}
.marker {
  position: absolute;
  height: 100%;
  transform: translateX(-50%);
  text-align: center;
}
.marker-line {
  height: 100%;
  border-left: 1px dashed #888;
}
.marker-label {
  position: absolute;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 0.8rem;
  color: #555;
}
.progress-area img {
  margin-left: 8px;
  height: 40px;
  width: auto;
}
/* 상세 정보 */
.details {
  margin-top: 1rem;
}
.detail-row {
  display: flex;
  margin-bottom: 0.5rem;
  gap: 0.5rem;
}
.label {
  font-weight: bold;
  min-width: 120px;
  color: #444;
}
.value {
  flex: 1;
  color: #111;
}
/* 버튼 */
.button-row {
  margin-top: 1.5rem;
  display: flex;
  gap: 1rem;
}
.btn {
  border-radius: 16px;
  padding: 0.2rem 2rem;
}
/* 추천 상품 */
.recommendation-section {
  margin-top: 2rem;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  margin-top: 1rem;
  gap: 1.5rem;
}
.product-grid > * {
  width: 100%;
}
@media (max-width: 1024px) {
  .progress-area,
  .details,
  .button-row {
    padding: 1rem;
  }
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
