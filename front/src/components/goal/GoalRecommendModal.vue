<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content" @click.stop>
      <h3>🎯 추천 목표가 있어요!</h3>
      <p class="reason">{{ reason }}</p>

      <div class="summary">
        <p><strong>💰 금액: </strong> {{ formattedAmount }} 원</p>
        <p>
          <strong>📅 기간: </strong>
          {{ formattedStartDate }} ~ {{ formattedEndDate }} ( 약
          {{ periodDiff }} )
        </p>
      </div>

      <div class="btn-group">
        <button class="accept" @click="$emit('accept')">
          추천 목표로 할래요
        </button>
        <button class="reject" @click="$emit('reject')">직접 설정할게요</button>
        <button class="close" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    recommendData: {
      type: Object,
      required: true,
    },
  },
  computed: {
    reason() {
      return this.recommendData.reason;
    },
    formattedAmount() {
      return Number(this.recommendData.recommendedAmount).toLocaleString();
    },
    formattedStartDate() {
      return this.formatDate(this.recommendData.recommendedStartDate);
    },
    formattedEndDate() {
      return this.formatDate(this.recommendData.recommendedEndDate);
    },
    periodDiff() {
      return this.getPeriodDiff(
        this.recommendData.recommendedStartDate,
        this.recommendData.recommendedEndDate
      );
    },
  },
  methods: {
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

      if (endDate.getDate() > startDate.getDate()) {
        diffMonths += 1;
      }

      return diffMonths < 24
        ? `${diffMonths}개월`
        : `${Math.round(diffMonths / 12)}년`;
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  color: black;
}
.modal-content {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  width: 420px;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}
.reason {
  margin-bottom: 1rem;
  font-size: 1.1rem;
}
.summary {
  text-align: left;
  margin-bottom: 1.5rem;
}
.btn-group button {
  margin: 0.3rem;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.accept {
  background-color: #4caf50;
  color: white;
}
.reject {
  background-color: #f0ad4e;
  color: white;
}
.close {
  background-color: #aaa;
  color: white;
}
</style>
