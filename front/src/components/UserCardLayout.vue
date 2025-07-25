<template>
  <div class="user-card">
    <slot />
  </div>
</template>

<style scoped>
.user-card {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  margin: 100px auto 0 auto;
  padding: 64px 80px;
  width: 70vw;
  max-width: 1100px;
  min-width: 400px;
  border: 4px solid #189eff;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  box-sizing: border-box;
}
</style> 