<script setup>
import { ref, computed } from 'vue';
import { bankNameMap } from '@/utils/bankMap'; // 한글 은행명을 영문 파일명으로 매핑

const currentPage = ref(0); // 현재 페이지
const itemsPerPage = 2; //

// 이미지 파일을 glob으로 불러오기
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
    eager: true,
    import: 'default',
});

// 은행명으로 아이콘 경로 반환
const getBankIcon = (bankName) => {
    const englishName = bankNameMap[bankName];
    if (!englishName) return '';
    const match = Object.entries(iconModules).find(([path]) =>
        path.includes(`/${englishName}.png`)
    );
    return match ? match[1] : '';
};

// 추천 상품 더미 데이터
const products = [
    {
        id: 1,
        name: 'Sh첫만남우대예금',
        bank: 'SH수협',
        maxRate: 2.9,
        basicRate: 1.85,
        highlights: ['20대 여성들의 TOP 10', 'SH 수협 첫거래 고객 우대사항'],
    },
    {
        id: 2,
        name: 'KB청년우대적금',
        bank: 'KB국민',
        maxRate: 3.2,
        basicRate: 2.0,
        highlights: ['청년 전용 금리 우대', '국민은행 첫 거래 고객 특별 혜택'],
    },
    {
        id: 3,
        name: '내맘적금',
        bank: '카카오뱅크',
        maxRate: 3.2,
        basicRate: 2.0,
        highlights: ['카카오페이 연동 시 금리 우대', 'MZ세대 인기 상품'],
    },
    {
        id: 4,
        name: '청년도약적금',
        bank: '신한',
        maxRate: 4.0,
        basicRate: 2.5,
        highlights: ['청년 대상 국고 지원 혜택', '우대금리 조건 간편'],
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
                    :key="product.id"
                >
                    <div class="card-header">
                        <img
                            :src="getBankIcon(product.bank)"
                            alt="은행 로고"
                            class="bank-icon"
                        />
                        <div>
                            <div class="product-name">{{ product.name }}</div>
                            <div class="bank-name">{{ product.bank }}</div>
                        </div>
                    </div>
                    <div class="rate">
                        최고 {{ product.maxRate.toFixed(2) }}% / 기본
                        {{ product.basicRate.toFixed(2) }}% (12개월 세전)
                    </div>
                    <ul class="highlights">
                        <li
                            v-for="(highlight, idx) in product.highlights"
                            :key="idx"
                        >
                            ✨ {{ highlight }} ✨
                        </li>
                    </ul>
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

.highlights {
    list-style: none;
    padding-left: 0.5rem;
    margin: 0;
}

.highlights li {
    font-size: 0.95rem;
    color: #111;
    font-weight: bold;
    margin-bottom: 0.3rem;
}
</style>
