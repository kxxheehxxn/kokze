<template>
  <!-- ScrollToTop 버튼 -->
  <div class="scroll-top-btn" @click="scrollToTop">
    <div class="scroll-top-icon">
      <i class="fa-solid fa-chevron-up" style="color: #3573ee"></i>
    </div>
  </div>
</template>
<script setup>
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
};
</script>
<style scoped>
/* ===== ScrollToTop 버튼 ===== */
.scroll-top-btn {
  cursor: pointer;
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 56px;
  height: 56px;
  background-color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px solid #e5e7eb;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}
.scroll-top-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}
.scroll-top-icon {
  font-size: 20px;
  transition: transform 0.3s ease;
}
.scroll-top-btn:hover .scroll-top-icon {
  transform: translateY(-2px);
}
@media (max-width: 1024px) {
  .scroll-top-btn {
    width: 52px;
    height: 52px;
    bottom: 25px;
    right: 25px;
  }
}
@media (max-width: 768px) {
  /* 스크롤 버튼 */
  .scroll-top-btn {
    width: 48px;
    height: 48px;
    bottom: 20px;
    right: 20px;
  }
  .scroll-top-icon {
    font-size: 18px;
  }
}
</style>
