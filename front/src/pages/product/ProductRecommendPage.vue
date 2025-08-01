<script setup>
import { ref } from 'vue';
import ProductCarousel from '@/components/product/ProductCarousel.vue';
import ProductFilterBar from '@/components/product/ProductFilterBar.vue';
import ProductList from '@/components/product/ProductList.vue';

// 상위에서 필터 상태 관리
const filters = ref({
    banks: [],
    period: 0,
    amount: '',
    type: [],
    conditions: [],
});
</script>

<template>
    <div class="product-recommendation-page">
        <!-- 추천 캐러셀 -->
        <ProductCarousel />

        <!-- 필터 영역 (v-model로 상태 공유) -->
        <ProductFilterBar v-model:filters="filters" />

        <!-- 필터된 리스트 출력 -->
        <ProductList :filters="filters" />
    </div>
</template>

<style scoped>
.product-recommendation-page {
    max-width: 1024px;
    margin: 0 auto;
    padding: 2rem 1rem 3rem;
    box-sizing: border-box;
}
</style>
