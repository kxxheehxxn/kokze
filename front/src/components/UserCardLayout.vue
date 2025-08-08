<template>
  <div class="container">
    <div class="user-card">
      <slot />
    </div>
  </div>
</template>
<style scoped>
.container {
  background-color: #fbfbfb;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding-top: 60px;
  position: relative;
}
.user-card {
  background-color: #fff;
  width: 100%;
  max-width: 920px;
  min-height: 80vh;
  padding: 80px 140px;
  border-radius: 28px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 20px;
}
@media (max-width: 1024px) and (orientation: portrait) {
  .user-card {
    min-height: 80vh;
    width: 90vw;
    padding: 80px 100px;
  }
}
</style>
