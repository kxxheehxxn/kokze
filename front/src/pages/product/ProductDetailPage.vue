<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import ProductSummaryCard from '@/components/product/detail/ProductSummaryCard.vue'
import ProductSummaryNote from '@/components/product/detail/ProductSummaryNote.vue'
import ProductInfoBox from '@/components/product/detail/ProductInfo.vue'
import ProductRateBox from '@/components/product/detail/ProductRate.vue'
import ScrollTopButton from '@/components/layouts/ScrollTopButton.vue'

import { fetchProductDetail } from '@/api/productApi'

const props = defineProps({
  fin_prdt_cd: String,
})

const route = useRoute()
const router = useRouter()

// prop이 없으면 /product/:fin_prdt_cd 에서 폴백
const finPrdtCd = computed(() => props.fin_prdt_cd || route.params.fin_prdt_cd)

const product = ref(null)
const isLoading = ref(true)
const loadError = ref(null)

async function loadProduct() {
  if (!finPrdtCd.value) {
    loadError.value = '잘못된 접근입니다. 상품 코드가 없습니다.'
    product.value = null
    isLoading.value = false
    return
  }

  isLoading.value = true
  loadError.value = null

  try {
    const result = await fetchProductDetail(finPrdtCd.value)
    product.value = result
  } catch (err) {
    console.error('상세 정보 불러오기 실패:', err)
    loadError.value = '해당 상품 정보를 불러오지 못했어요.'
    product.value = null
  } finally {
    isLoading.value = false
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

onMounted(loadProduct)

// 코드가 바뀌면 상세 → 상세에서도 재조회
watch(
  () => finPrdtCd.value,
  () => {
    if (!isLoading.value) loadProduct()
  },
)

function goBackToList() {
  router.back()
}
</script>

<template>
  <div class="product-detail-page">
    <div class="product-detail-wrapper">
      <div class="back-button" @click="goBackToList">
        <i class="fa-solid fa-arrow-left" /> 목록으로 돌아가기
      </div>

      <template v-if="isLoading">
        <p>로딩 중...</p>
      </template>

      <template v-else>
        <template v-if="product">
          <ProductSummaryCard :product="product" />
          <ProductSummaryNote :product="product" />
          <ProductInfoBox :product="product" />
          <ProductRateBox :product="product" />
        </template>

        <div v-else class="not-found">
          {{ loadError || '해당 상품을 찾을 수 없습니다.' }}
        </div>
      </template>
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
.not-found {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  color: #555;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
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
