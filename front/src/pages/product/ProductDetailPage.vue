<script setup>
import { useRoute, useRouter } from 'vue-router';
import ProductSummaryCard from '@/components/product/detail/ProductSummaryCard.vue';
import ProductSummaryNote from '@/components/product/detail/ProductSummaryNote.vue';
import ProductInfoBox from '@/components/product/detail/ProductInfo.vue';
import ProductRateBox from '@/components/product/detail/ProductRate.vue';

// 더미 데이터
const dummyProduct = {
    finPrdtCd: 'WR0001F',
    productName: '우리SUPER주거래적금',
    bankName: '우리은행',
    intrRate: 2.15,
    intrRate2: 3.55,
    joinWay: '인터넷, 스마트폰',
    joinMember: '개인(실명의 개인)',
    etcNote: '추가 우대조건 없음',
    spclCnd: '급여이체 실적 등 우대조건 있음',
    mtrtInt: '만기 이자 지급',
};

const route = useRoute();
const router = useRouter();
const product = route.state?.product || dummyProduct;

// 돌아가기
function goBackToList() {
    router.push('/product');
}
</script>

<template>
    <div class="product-detail-page">
        <!-- 🔙 목록으로 돌아가기 -->
        <div class="back-button" @click="goBackToList">
            ＜ 목록으로 돌아가기
        </div>

        <template v-if="product">
            <ProductSummaryCard :product="product" />
            <ProductSummaryNote :product="product" />
            <ProductInfoBox :product="product" />
            <ProductRateBox :product="product" />
        </template>

        <div v-else class="not-found">해당 상품을 찾을 수 없습니다.</div>
    </div>
</template>

<style scoped>
.product-detail-page {
    max-width: 1024px;
    margin: 0 auto;
    padding: 2rem 1rem 3rem;
    box-sizing: border-box;
}

.back-button {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1.25rem;
    background-color: #e5e5e5;
    border: none;
    border-radius: 1.5rem;
    font-size: 1.1rem;
    font-weight: 600;
    color: #111;
    cursor: pointer;
    transition: background-color 0.2s;
    margin: 1rem 0;
}

.back-button:hover {
    background-color: #d4d4d4;
}
</style>
