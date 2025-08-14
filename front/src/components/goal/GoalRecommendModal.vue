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
      toDate(val) {
      if (!val) return null;
      if (val instanceof Date) return val;
      if (Array.isArray(val) && val.length === 3) {
        const [y, m, d] = val;
        return new Date(Number(y), Number(m) - 1, Number(d));
      }
      if (typeof val === 'string') {
        const parts = val.split(/[-/.]/);
        if (parts.length >= 3) {
          return new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
        }
        const d = new Date(val);
        return isNaN(d) ? null : d;
      }
      return null;
    },

    formatDate(input) {
      const d = this.toDate(input);
      if (!d) return '';
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}년 ${m}월 ${day}일`;
    },

    getPeriodDiff(start, end) {
      const s = this.toDate(start);
      const e = this.toDate(end);
      if (!s || !e) return '';
      let diffMonths =
        (e.getFullYear() - s.getFullYear()) * 12 +
        (e.getMonth() - s.getMonth());
      if (e.getDate() > s.getDate()) diffMonths += 1;
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
