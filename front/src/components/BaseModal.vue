<template>
  <div v-if="visible" class="modal-backdrop">
    <div class="modal-container">
      <div class="modal-message text-center">
        {{ message }}
      </div>
      <div class="modal-actions">
        <button
          v-for="(btn, idx) in buttons"
          :key="idx"
          @click="btn.onClick"
          :class="buttonClass(idx)"
        >
          {{ btn.text }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  visible: Boolean,
  message: String,
  buttons: Array, // [{ text: '확인', onClick: () => {} }]
});

// 버튼 색상 결정 함수
function buttonClass(idx) {
  if (props.buttons.length === 1) {
    // 버튼이 하나면 파랑색
    return 'primary-btn';
  } else if (props.buttons.length >= 2) {
    // 버튼이 두개 이상이면 마지막 버튼만 파랑, 나머지는 회색
    return idx === props.buttons.length - 1 ? 'primary-btn' : 'secondary-btn';
  }
  // 기본 회색
  return 'secondary-btn';
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}
.modal-container {
  display: flex;
  background: white;
  padding: 20px;
  border-radius: 10px;
  min-width: 450px;
  min-height: 200px;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.modal-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.modal-message {
  margin-top: 10px;
  font-size: 20px;
}
button {
  width: 60px;
  height: 40px;
  border-radius: 10px;
  margin: 10px 5px 0 5px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border: none;
  cursor: pointer;
}
.primary-btn {
  background-color: #3573ee;
  color: white;
  border: none;
}
.secondary-btn {
  color: #666666;
  background-color: #fff;
  border: solid 1px #e1e1e1;
}
</style>
