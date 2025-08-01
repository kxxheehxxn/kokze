<template>
    <div class="product-recommendation-page">
        <!-- 추천 캐러셀 -->
        <ProductCarousel />

        <!-- 필터 영역 -->
        <ProductFilterBar />

        <!-- 상품 리스트 -->
        <ProductList />

        <!-- 페이지네이션 -->
        <ProductPagination />
    </div>
</template>

<script setup>
import ProductCarousel from '@/components/product/ProductCarousel.vue';
import ProductFilterBar from '@/components/product/ProductFilterBar.vue';
import ProductList from '@/components/product/ProductList.vue';
import ProductPagination from '@/components/product/ProductPagination.vue';
</script>

<style scoped>
.product-recommendation-page {
    max-width: 1024px;
    margin: 0 auto;
    padding: 2rem 1rem 3rem;
    box-sizing: border-box;
}
</style>
