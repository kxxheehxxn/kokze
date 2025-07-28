<template>
  <div class="goal-detail-page">
    <!-- 돌아가기 -->
    <router-link to="/goals" class="back-link">← 목표로 돌아가기</router-link>

    <!-- 목표 타이틀 및 달성률 -->
    <section class="summary-card">
      <div class="title-row">
        <h3>목표 1: {{ goal.title }} ({{ goal.progress }}%)</h3>
        <div class="progress-area">
          <div class="progress-bar">
            <div class="progress" :style="{ width: goal.progress + '%' }"></div>
          </div>
          <span class="progress-percent">{{ goal.progress }}%</span>
        </div>
      </div>

      <!-- 목표 상세 정보 -->
      <div class="details">
        <p>
          <strong>목표 기간:</strong> {{ goal.period1 }} ~ {{ goal.period2 }}
        </p>
        <p>
          <strong>목표 금액:</strong> {{ goal.savedAmount }} 원 /
          {{ goal.totalAmount }} 원
        </p>
        <p><strong>연결된 금융 상품:</strong> {{ goal.product }}</p>
      </div>

      <div class="button-row">
        <!-- 예시 버튼 -->
        <button
          class="btn btn-primary"
          @click="
            $router.push({ name: 'GoalEditPage', params: { goalId: goal.id } })
          "
        >
          목표 수정하기
        </button>

        <button class="btn btn-danger">목표 삭제하기</button>
      </div>
    </section>

    <!-- 맞춤 금융 상품 추천 -->
    <section class="recommendation-section">
      <h4>✨ 김콕재님에게 추천하는 맞춤 금융 상품 ✨</h4>
      <div class="product-grid">
        <div
          v-for="(product, idx) in recommended"
          :key="idx"
          class="product-card"
        >
          <p>
            <strong>{{ product.name }}</strong>
          </p>
          <p>✨ 추천하는 이유: {{ product.reason }} ✨</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'GoalDetailPage',
  data() {
    return {
      goal: {
        title: '노후 준비',
        progress: 9,
        period1: '2025.07.15',
        period2: '2100.01.01 (약 75년)',
        savedAmount: 900000000,
        totalAmount: 1000000000,
        product: 'J 정기예금 매달 5일 20만원',
      },
      recommended: [
        { name: 'S 정기예금', reason: '안정적인 수익' },
        { name: 'KB이상사랑적금', reason: '고금리 상품' },
        { name: '한화2.4레버리지...', reason: '높은 수익 가능성' },
        { name: '(적금) J 정기예금', reason: '목표 연동' },
        { name: '(적금) S 정기예금', reason: '안정성 우수' },
      ],
    };
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
</style>
