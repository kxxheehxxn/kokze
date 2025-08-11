<script setup>
import { ref, onMounted, onBeforeUnmount, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

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

// prop이 없으면 /product/:fin_prdt_cd 사용
const finPrdtCd = computed(() => props.fin_prdt_cd || route.params.fin_prdt_cd)

const product = ref(null)
const isLoading = ref(true)
const loadError = ref(null)

// 요약 폴링 관련
const loadingSummary = ref(false)
let summaryTimer = null
let pollTries = 0
const MAX_TRIES = 5
const DELAY_MS = 1200

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

    // 요약이 없으면: (A) GET 시 자동 생성된다면 폴링만
    //                (B) 자동 생성이 아니라면 한 번 트리거 호출 후 폴링
    if (!result?.summary) {
      // ↓ 백엔드가 수동 트리거를 제공하는 경우만 사용하세요.
      // 실패해도 폴링은 계속 진행하므로 catch 무시
      await axios
        .post(`/api/products/${finPrdtCd.value}/summary/refresh`)
        .catch(() => {})
      startSummaryPolling()
    } else {
      stopSummaryPolling()
    }
  } catch (err) {
    console.error('상세 정보 불러오기 실패:', err)
    loadError.value = '해당 상품 정보를 불러오지 못했어요.'
    product.value = null
  } finally {
    isLoading.value = false
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

function startSummaryPolling() {
  loadingSummary.value = true
  pollTries = 0
  scheduleNextPoll()
}

function scheduleNextPoll() {
  clearTimeout(summaryTimer)
  summaryTimer = setTimeout(async () => {
    try {
      const refreshed = await fetchProductDetail(finPrdtCd.value)
      if (refreshed?.summary && refreshed.summary.trim()) {
        product.value = { ...refreshed } // 최신 전체 데이터로 교체
        stopSummaryPolling()
        return
      }
      bumpAndMaybeContinue()
    } catch (e) {
      console.warn('요약 재조회 실패:', e)
      bumpAndMaybeContinue()
    }
  }, DELAY_MS)
}

function bumpAndMaybeContinue() {
  pollTries += 1
  if (pollTries < MAX_TRIES) scheduleNextPoll()
  else stopSummaryPolling()
}

function stopSummaryPolling() {
  loadingSummary.value = false
  if (summaryTimer) {
    clearTimeout(summaryTimer)
    summaryTimer = null
  }
}

onBeforeUnmount(stopSummaryPolling)
onMounted(loadProduct)

watch(
  () => finPrdtCd.value,
  () => {
    stopSummaryPolling()
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

          <div v-if="loadingSummary" class="summary-loading-banner">
            🔄 상품 요약을 생성 중입니다… 잠시만 기다려 주세요.
          </div>

          <ProductSummaryNote
            :product="product"
            :loadingSummary="loadingSummary"
          />

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
.summary-loading-banner {
  background: #fff7e6;
  border: 1px solid #ffd591;
  color: #ad6800;
  padding: 12px 16px;
  border-radius: 10px;
  margin-top: 12px;
  font-size: 0.95rem;
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
