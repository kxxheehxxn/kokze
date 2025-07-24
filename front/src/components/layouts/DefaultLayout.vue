<script setup>
import Header from './Header.vue';
import Footer from './Footer.vue';
</script>

<template>
  <div class="container">
    <Header></Header>
    <div class="content my-5">
      <slot></slot>
    </div>
    <Footer></Footer>
  </div>
</template>
