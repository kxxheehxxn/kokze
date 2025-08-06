<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProductSummaryCard from '@/components/product/detail/ProductSummaryCard.vue';
import ProductSummaryNote from '@/components/product/detail/ProductSummaryNote.vue';
import ProductInfoBox from '@/components/product/detail/ProductInfo.vue';
import ProductRateBox from '@/components/product/detail/ProductRate.vue';
import ScrollTopButton from '@/components/layouts/ScrollTopButton.vue';
import { fetchProductDetail } from '@/api/productApi';

const props = defineProps({
  fin_prdt_cd: String,
});
const router = useRouter();
const product = ref(null);
const isLoading = ref(true);

// 상품 정보 불러오기
const loadProduct = async () => {
  console.log('받은 fin_prdt_cd:', props.fin_prdt_cd);
  try {
    const result = await fetchProductDetail(props.fin_prdt_cd);
    product.value = result;
  } catch (error) {
    console.error('상세 정보 불러오기 실패:', error);
    product.value = null;
  } finally {
    isLoading.value = false;
  }
};

onMounted(loadProduct);

// 🔙 목록으로 돌아가기
function goBackToList() {
  router.back();
}
</script>

<template>
  <div class="product-detail-page">
    <div class="product-detail-wrapper">
      <!-- 🔙 목록으로 돌아가기 -->
      <div class="back-button" @click="goBackToList"><i class="fa-solid fa-arrow-left"></i> 목록으로 돌아가기</div>

      <template v-if="isLoading">
        <p>로딩 중...</p>
      </template>

      <template v-else-if="product">
        <ProductSummaryCard :product="product" />
        <ProductSummaryNote :product="product" />
        <ProductInfoBox :product="product" />
        <ProductRateBox :product="product" />
      </template>

      <div v-else class="not-found">해당 상품을 찾을 수 없습니다.</div>
    </div>
    <ScrollTopButton />
  </div>
</template>

<style scoped>
.product-detail-page {
  margin: 0 auto;
  padding: 60px 100px 40px;
  box-sizing: border-box;
  width: 100%;
  background-color: #fbfbfb;
}

.product-detail-wrapper {
  padding: 0 12px;
  max-width: 1280px;
  margin: 0 auto;
}

.back-button {
  color: #007bff;
  text-decoration: none;
  margin: 1rem;
  display: inline-block;
  cursor: pointer;
}

@media (max-width: 1024px) {
  .product-detail-page {
    padding: 60px 70px 40px;
  }
}

@media (max-width: 768px) {
  .product-detail-page {
    padding: 60px 30px 40px;
  }
}
</style>
