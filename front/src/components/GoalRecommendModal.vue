<template>
  <!-- ⛳ 배경 클릭 시 닫히고 -->
  <div class="modal-overlay" @click.self="$emit('close')">
    <!-- ⛳ 내부 클릭 시 부모 이벤트 막기 -->
    <div class="modal-content" @click.stop>
      <h3>🎯 추천 목표가 있어요!</h3>
      <p class="reason">{{ recommendData.reason }}</p>
      <div class="summary">
        <p><strong>💰 금액:</strong> {{ formattedAmount }} 원</p>
        <p>
          <strong>📅 기간:</strong>
          {{ recommendData.recommendedStartDate }} ~
          {{ recommendData.recommendedEndDate }}
        </p>
      </div>
      <div class="btn-group">
        <button class="accept" @click="$emit('accept')">추천 목표로 할래요</button>
        <button class="reject" @click="$emit('reject')">직접 설정할게요</button>
        <button class="close" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    recommendData: Object,
  },
  computed: {
    formattedAmount() {
      return Number(this.recommendData.recommendedAmount).toLocaleString();
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
