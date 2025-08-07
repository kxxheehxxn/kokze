<script setup>
import Header from './Header.vue';
import Footer from './Footer.vue';
</script>
<template>
  <div class="container">
    <Header />
    <main class="content mt-5">
      <slot />
    </main>
    <Footer />
  </div>
</template>
<style scoped>
.container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.content {
  flex: 1;
  width: 100%;
}
</style>
