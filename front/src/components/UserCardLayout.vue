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
  justify-content: center;
  position: relative;
}
.user-card {
  background-color: #fff;
  width: 100%;
  max-width: 900px;
  padding: 90px 140px;
  border-radius: 28px;
  box-shadow: 0 0 20px #85858540;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
