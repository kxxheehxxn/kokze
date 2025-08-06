<script setup>
import { ref } from 'vue';
import ProductCarousel from '@/components/product/ProductCarousel.vue';
import ProductFilterBar from '@/components/product/ProductFilterBar.vue';
import ProductList from '@/components/product/ProductList.vue';
import ScrollTopButton from '@/components/layouts/ScrollTopButton.vue';

// 상위에서 필터 상태 관리
const filters = ref({
  bankNames: [],
  joinMembers: [],
  productType: null,
  minSaveTrm: null,
  maxSaveTrm: null,
  minAmount: null,
  maxAmount: null,
  hasSpclCnd: false,
  __active: null,
});
</script>

<template>
  <div class="product-recommendation-page">
    <div class="product-wrapper">
      <!-- 추천 캐러셀 -->
      <ProductCarousel />

      <!-- 필터 영역 (v-model로 상태 공유) -->
      <ProductFilterBar v-model:filters="filters" />

      <!-- 필터된 리스트 출력 -->
      <ProductList :filters="filters" />
    </div>
    <ScrollTopButton />
  </div>
</template>

<style scoped>
.product-recommendation-page {
  width: 100%;
  padding: 60px 90px 40px;
  box-sizing: border-box;
  background-color: #fbfbfb;
}

.product-wrapper {
  background-color: transparent;
  margin: 0 auto;
  padding: 0 12px;
  max-width: none;
}

@media (max-width: 1024px) {
  .product-recommendation-page {
    padding: 60px 70px;
  }
}

@media (max-width: 768px) {
  .product-recommendation-page {
    padding: 60px 30px;
  }
}
</style>
