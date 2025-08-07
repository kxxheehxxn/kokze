<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { bankNameMap } from '@/utils/bankMap';
import { fetchRecommendedProducts } from '@/api/productApi';
import { userAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
const router = useRouter();
function goToDetail(finPrdtCd) {
  router.push(`/product/${finPrdtCd}`);
}
const currentPage = ref(0);
const itemsPerPage = 2;
const products = ref([]);
const windowWidth = ref(window.innerWidth); // 반응형을 위한 상태

const auth = userAuthStore();
const userId = auth.state.user.userId;
const userName = auth.state.user.userName || '김콕재';
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
  eager: true,
  import: 'default',
});
const defaultIcon = new URL('@/assets/images/bankIcon/default.png', import.meta.url).href;
const getBankIcon = (bankName) => {
  const english = bankNameMap[bankName];
  if (!english) return defaultIcon;
  const match = Object.entries(iconModules).find(([path]) => path.includes(`/${english}.png`));
  return match ? match[1] : defaultIcon;
};

// 화면 크기 변경 감지
const handleResize = () => {
  windowWidth.value = window.innerWidth;
  // 화면 크기가 변경되면 현재 페이지 초기화
  currentPage.value = 0;
};

onMounted(async () => {
  try {
    const result = await fetchRecommendedProducts(userId);
    // 최대 4개 표시
    products.value = result.slice(0, 4);
  } catch (error) {
    console.error('추천 상품 불러오기 실패:', error);
  }
  // 리사이즈 이벤트 리스너 추가
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  // 리사이즈 이벤트 리스너 제거
  window.removeEventListener('resize', handleResize);
});

// 데스크탑에서는 모든 카드 표시, 모바일에서는 페이징
const visibleProducts = computed(() => {
  // 데스크탑 (1025px 이상)에서는 모든 상품 표시
  if (windowWidth.value > 1024) {
    return products.value;
  }

  // 모바일에서는 페이징
  const start = currentPage.value * itemsPerPage;
  const end = start + itemsPerPage;
  return products.value.slice(start, end);
});

// 네비게이션 버튼 표시 여부
const showNavButtons = computed(() => {
  return windowWidth.value <= 1024 && products.value.length > itemsPerPage;
});

const endOfSlide = computed(() => {
  return (currentPage.value + 1) * itemsPerPage >= products.value.length;
});
function prevSlide() {
  if (currentPage.value > 0) currentPage.value--;
}
function nextSlide() {
  if (!endOfSlide.value) currentPage.value++;
}
</script>
<template>
  <div class="carousel-wrapper">
    <div class="title">✨ {{ userName }}님의 맞춤 추천 상품 ✨</div>
    <div class="carousel-container">
      <!-- 태블릿에서만 네비게이션 버튼 표시 -->
      <button v-if="showNavButtons" class="nav-button" @click="prevSlide" :disabled="currentPage === 0">
        <i class="fa-solid fa-angle-left"></i>
      </button>
      <div class="carousel-cards">
        <div
          class="card"
          v-for="product in visibleProducts"
          :key="product.finPrdtCd"
          @click="goToDetail(product.finPrdtCd)"
        >
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

      <!-- 태블릿에서만 네비게이션 버튼 표시 -->
      <button v-if="showNavButtons" class="nav-button" @click="nextSlide" :disabled="endOfSlide">
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
  font-size: 1.2rem;
  margin-bottom: 50px;
}
.carousel-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}
/* 좌우버튼 */
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
/* 금융 상품 */
.carousel-cards {
  display: flex;
  gap: 8px;
}
.card {
  width: clamp(180px, calc(180px + (320 - 180) * ((100vw - 1024px) / (1920 - 1024))), 320px);
  background: white;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 1.5rem 1.5rem 1.8rem;
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
  font-size: clamp(10px, calc(10px + (18 - 10) * ((100vw - 1024px) / (1920 - 1024))), 18px);
}
.bank-name {
  font-size: clamp(7.3px, calc(7.3px + (13 - 7.3) * ((100vw - 1024px) / (1920 - 1024))), 13px);
  color: #666;
  margin-top: 0.2rem;
}
.rate {
  margin: 0.5rem 0 1rem;
  font-weight: normal;
  color: #222;
  font-size: clamp(8.4px, calc(8.4px + (15 - 8.4) * ((100vw - 1024px) / (1920 - 1024))), 15px);
  text-align: center;
}
.highlight {
  list-style: none;
  margin: 0;
  font-weight: bold;
  font-size: clamp(8.4px, calc(8.4px + (15 - 8.4) * ((100vw - 1024px) / (1920 - 1024))), 15px);
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
</style>
