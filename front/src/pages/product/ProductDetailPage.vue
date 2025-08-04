<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProductSummaryCard from '@/components/product/detail/ProductSummaryCard.vue';
import ProductSummaryNote from '@/components/product/detail/ProductSummaryNote.vue';
import ProductInfoBox from '@/components/product/detail/ProductInfo.vue';
import ProductRateBox from '@/components/product/detail/ProductRate.vue';
import { fetchProductDetail } from '@/api/productApi';

// ✅ 여기에서 props로 받아야 함 (라우터에서 props: true 사용 중이므로)
const props = defineProps({
  fin_prdt_cd: String,
});
const router = useRouter();
const product = ref(null);
const isLoading = ref(true);

// 상품 정보 불러오기
const loadProduct = async () => {
  console.log('받은 fin_prdt_cd:', props.fin_prdt_cd); // ✅ 확인용
  try {
    const result = await fetchProductDetail(props.fin_prdt_cd); // ✅ 여기!
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
  router.push('/product');
}
</script>
<template>
  <div class="product-detail-page">
    <!-- 🔙 목록으로 돌아가기 -->
    <div class="back-button" @click="goBackToList">＜ 목록으로 돌아가기</div>

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
