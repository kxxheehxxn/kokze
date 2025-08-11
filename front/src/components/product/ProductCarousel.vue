<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { bankNameMap } from '@/utils/bankMap';
import { fetchRecommendedProducts } from '@/api/productApi'
import { userAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
const router = useRouter()
function goToDetail(finPrdtCd) {
  router.push(`/product/${finPrdtCd}`)
}
const currentPage = ref(0)
const itemsPerPage = 2
const products = ref([])
const windowWidth = ref(window.innerWidth)

const auth = userAuthStore()
const userId = auth.state.user.userId
const userName = auth.state.user.userName || '김콕재'
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
  eager: true,
  import: 'default',
})
const defaultIcon = new URL('@/assets/images/bankIcon/default.png', import.meta.url).href
const getBankIcon = (bankName) => {
  const english = bankNameMap[bankName]
  if (!english) return defaultIcon
  const match = Object.entries(iconModules).find(([path]) => path.includes(`/${english}.png`))
  return match ? match[1] : defaultIcon
}
const handleResize = () => {
  windowWidth.value = window.innerWidth
  currentPage.value = 0
}
onMounted(async () => {
  try {
    const result = await fetchRecommendedProducts(userId)
    products.value = result;
  } catch (error) {
    console.error('추천 상품 불러오기 실패:', error)
  }
  window.addEventListener('resize', handleResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
const visibleProducts = computed(() => {
  
  if (windowWidth.value > 1024) {
    const itemsPerPageDesktop = 3
    const start = currentPage.value * itemsPerPageDesktop
    const end = start + itemsPerPageDesktop
    return products.value.slice(start, end)
  }
  const start = currentPage.value * itemsPerPage
  const end = start + itemsPerPage
  return products.value.slice(start, end)
})

const endOfSlide = computed(() => {
  const currentItemsPerPage = windowWidth.value > 1024 ? 3 : itemsPerPage;
  return (currentPage.value + 1) * currentItemsPerPage >= products.value.length;
})
function prevSlide() {
  if (currentPage.value > 0) currentPage.value--
}
function nextSlide() {
  if (!endOfSlide.value) currentPage.value++
}
</script>
<template>
  <div class="carousel-wrapper">
    <div class="title">✨ {{ userName }}님의 맞춤 추천 상품 ✨</div>
    <div class="carousel-container">
      <button class="nav-button" @click="prevSlide" :disabled="currentPage === 0">
        <i class="fa-solid fa-angle-left"></i>
      </button>
      <div class="carousel-cards">
        <div class="card" v-for="product in visibleProducts" :key="product.finPrdtCd" @click="goToDetail(product.finPrdtCd)">
          <div class="card-header">
            <img :src="getBankIcon(product.bankName)" alt="은행 로고" class="bank-icon" />
            <div>
              <div class="product-name">
                {{ product.productName }}
              </div>
              <div class="bank-name">{{ product.bankName }}</div>
            </div>
          </div>
          <div class="rate">최고 {{ product.intrRate2 }}% / 기본 {{ product.intrRate }}% (12개월 세전)</div>
          <div class="highlight">✨ {{ product.reason }} ✨</div>
        </div>
      </div>
      <button class="nav-button" @click="nextSlide" :disabled="endOfSlide">
        <i class="fa-solid fa-angle-right"></i>
      </button>
    </div>
  </div>
</template>
<style scoped>
.carousel-wrapper {
  height: 360px;
  background: #ffffff;
  padding: 48px 0 36px;
  border-radius: 20px;
  box-shadow: inset 0 0 12px #3573ee;
  text-align: center;
  margin: 0 0 36px;
}
.title {
  font-weight: bold;
  font-size: 1.5rem;
  margin-bottom: 50px;
}
.carousel-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}
.nav-button {
  background: white;
  border: 1px solid #ccc;
  width: 40px;
  height: 40px;
  font-weight: bold;
  font-size: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.2s;
}
.nav-button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
.nav-button:hover {
  cursor: pointer;
  transform: translateY(-2px);
  transition: all 0.2s ease;
}
.carousel-cards {
  display: flex;
  gap: 12px;
}
.card {
  width: clamp(240px, calc(240px + (360 - 240) * ((100vw - 1024px) / (1920 - 1024))), 360px);
  background: white;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 1.2rem 1.2rem 1.4rem;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);
  text-align: left;
}
.card:hover {
  cursor: pointer;
  transform: translateY(-2px);
  transition: all 0.2s ease;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
  background: white;
  padding: 0;
  border: none;
}
.bank-icon {
  width: clamp(25.3px, calc(25.3px + (45 - 25.3) * ((100vw - 1024px) / (1920 - 1024))), 45px);
  height: clamp(25.3px, calc(25.3px + (45 - 25.3) * ((100vw - 1024px) / (1920 - 1024))), 45px);
  object-fit: contain;
}
.product-name {
  font-weight: bold;
  font-size: clamp(14px, calc(14px + (22 - 14) * ((100vw - 1024px) / (1920 - 1024))), 22px);
}
.bank-name {
  font-size: clamp(10px, calc(10px + (15 - 10) * ((100vw - 1024px) / (1920 - 1024))), 15px);
  color: #666;
  margin-top: 0.2rem;
}
.rate {
  margin: 0.5rem 0 1rem;
  font-weight: normal;
  color: #222;
  font-size: clamp(13px, calc(13px + (18 - 13) * ((100vw - 1024px) / (1920 - 1024))), 18px);
  text-align: center;
}
.highlight {
  list-style: none;
  margin: 0;
  font-weight: bold;
  font-size: clamp(13px, calc(13px + (18 - 13) * ((100vw - 1024px) / (1920 - 1024))), 18px);
  text-align: center;
}
@media (max-width: 1024px) {
  .carousel-wrapper {
    padding: 36px 36px 36px;
    margin-bottom: 24px;
    height: auto;
  }
  .title {
    margin-bottom: 36px;
  }
  .card {
    width: 230px;
  }
  .product-name {
    font-size: 12px;
  }
  .bank-name {
    font-size: 10px;
  }
  .rate {
    font-size: 10px;
  }
  .highlight {
    font-size: 10px;
  }
  .nav-button {
    width: 30px;
    height: 30px;
    font-size: 16px;
  }
}
@media (max-width: 768px) {

}
</style>
