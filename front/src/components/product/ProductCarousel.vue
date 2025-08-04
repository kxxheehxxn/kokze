<script setup>
import { ref, computed } from 'vue';
import { bankNameMap } from '@/utils/bankMap'; // 한글 은행명을 영문 파일명으로 매핑

const currentPage = ref(0); // 현재 페이지
const itemsPerPage = 2; // 보여줄 갯수

// 이미지 파일을 glob으로 불러오기
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
    eager: true,
    import: 'default',
});

// 기본 아이콘 경로
const defaultIcon = new URL(
    '@/assets/images/bankIcon/default.png',
    import.meta.url
).href;

// 은행 이름(영문)을 받아 아이콘 경로를 반환
const getBankIcon = (bankName) => {
    const english = bankNameMap[bankName];
    if (!english) return defaultIcon;
    const match = Object.entries(iconModules).find(([path]) =>
        path.includes(`/${english}.png`)
    );
    return match ? match[1] : defaultIcon;
};

// DB 맞춤 더미 데이터
const products = [
    {
        finPrdtCd: 'WR0001F',
        productName: '우리SUPER주거래적금',
        bankName: '우리은행',
        intrRate: 2.15,
        intrRate2: 3.55,
        reason: '안정적이고 장기적인 상품',
    },
    {
        finPrdtCd: 'WR0001L',
        productName: 'WON적금',
        bankName: '우리은행',
        intrRate: 2.95,
        intrRate2: 3.15,
        reason: '안정적이고 장기적인 상품',
    },
    {
        finPrdtCd: '00266451',
        productName: '퍼스트가계적금',
        bankName: '한국스탠다드차타드은행',
        intrRate: 2.55,
        intrRate2: 2.55,
        reason: '안정적이고 장기적인 상품',
    },
    {
        finPrdtCd: '10527001000925000',
        productName: '영플러스적금',
        bankName: '아이엠뱅크',
        intrRate: 2.71,
        intrRate2: 3.26,
        reason: '안정적이고 장기적인 상품',
    },
];

const visibleProducts = computed(() => {
    const start = currentPage.value * itemsPerPage;
    const end = start + itemsPerPage;
    return products.slice(start, end);
});

const endOfSlide = computed(() => {
    return (currentPage.value + 1) * itemsPerPage >= products.length;
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
        <div class="title">✨ 김국제님의 맞춤 추천 상품 ✨</div>

        <div class="carousel-container">
            <button
                class="nav-button"
                @click="prevSlide"
                :disabled="currentPage === 0"
            >
                &lt;
            </button>

            <div class="carousel-cards">
                <div
                    class="card"
                    v-for="product in visibleProducts"
                    :key="product.finPrdtCd"
                >
                    <div class="card-header">
                        <img
                            :src="getBankIcon(product.bankName)"
                            alt="은행 로고"
                            class="bank-icon"
                        />
                        <div>
                            <div class="product-name">
                                {{ product.productName }}
                            </div>
                            <div class="bank-name">{{ product.bankName }}</div>
                        </div>
                    </div>
                    <div class="rate">
                        최고 {{ product.intrRate2 }}% / 기본
                        {{ product.intrRate }}% (12개월 세전)
                    </div>
                    <div class="highlight">✨ {{ product.reason }} ✨</div>
                </div>
            </div>

            <button
                class="nav-button"
                @click="nextSlide"
                :disabled="endOfSlide"
            >
                &gt;
            </button>
        </div>
    </div>
</template>

<style scoped>
.carousel-wrapper {
    background: white;
    padding: 2rem;
    border-radius: 1.5rem;
    box-shadow: inset 0 0 12px #3573ee;
    text-align: center;
    margin-bottom: 2rem;
}

.title {
    font-weight: bold;
    font-size: 1.2rem;
    margin-bottom: 1.5rem;
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
    font-size: 1.2rem;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}
.nav-button:disabled {
    opacity: 0.3;
    cursor: not-allowed;
}

/* 금융 상품 */
.carousel-cards {
    display: flex;
    gap: 1.5rem;
}

.card {
    width: 350px;
    background: white;
    border: 1px solid #ddd;
    border-radius: 1rem;
    padding: 1.5rem 1.5rem 1.8rem;
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);
    text-align: left;
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
    width: 45px;
    height: 45px;
    object-fit: contain;
}

.product-name {
    font-weight: bold;
    font-size: 1.2rem;
}

.bank-name {
    font-size: 0.85rem;
    color: #666;
    margin-top: 0.2rem;
}

.rate {
    margin: 0.5rem 0 1rem;
    font-weight: normal;
    color: #222;
    font-size: 0.95rem;
}

.highlight {
    list-style: none;
    padding-left: 0.5rem;
    margin: 0;
    font-weight: bold;
}
</style>
