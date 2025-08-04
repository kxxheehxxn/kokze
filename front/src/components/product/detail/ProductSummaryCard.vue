<script setup>
import { bankNameMap } from '@/utils/bankMap';

const props = defineProps({
    product: Object,
});

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
</script>

<template>
    <div class="summary-card">
        <div class="header">
            <div>
                <h1 class="title">{{ product.productName }}</h1>
                <p class="bank">{{ product.bankName }}</p>
            </div>
            <img
                :src="getBankIcon(product.bankName)"
                alt="은행 로고"
                class="logo"
            />
        </div>

        <div class="rates">
            <div class="rate-item">
                <div class="label">최고</div>
                <div class="value">연 {{ product.intrRate2 }}%</div>
            </div>
            <div class="divider" />
            <div class="rate-item">
                <div class="label">기본</div>
                <div class="value">연 {{ product.intrRate }}%</div>
            </div>
            <span class="term">(12개월, 세전)</span>
        </div>

        <button class="cta-button">공식 홈페이지 더 알아보기</button>

        <div class="footer-note">
            금융 상품 가입 후 ‘홈에서 자산 조회’를 클릭하여 금융 상품을
            연결하세요!
        </div>
    </div>
</template>

<style scoped>
.summary-card {
    background: white;
    padding: 3rem 4rem 2rem 4rem;
    border-radius: 1.5rem;
    box-shadow: inset 0 0 12px #3573ee;
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

/* 타이틀 + 은행 + 로고 */
.header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.title {
    font-size: 1.6rem;
    font-weight: 800;
}

.bank {
    font-size: 1.2rem;
    color: #999;
    margin-top: 0.2rem;
}

.logo {
    width: 70px;
    height: 70px;
    object-fit: contain;
}

/* 금리 */
.rates {
    display: flex;
    align-items: center;
    gap: 1.2rem;
    font-size: 1rem;
    align-items: flex-end;
}

.rate-item {
    display: flex;
    flex-direction: column;
    align-items: left;
}

.label {
    color: #666;
    font-weight: 600;
}

.value {
    font-weight: bold;
    font-size: 1.5rem;
    color: #22c55e;
}

.divider {
    width: 1px;
    height: 2rem;
    background: #999;
}

.term {
    font-size: 0.95rem;
    color: #666;
}

/* 버튼 */
.cta-button {
    background: #2563eb;
    color: white;
    border: none;
    padding: 0.8rem 1.5rem;
    font-size: 1.1rem;
    font-weight: bold;
    border-radius: 0.5rem;
    cursor: pointer;
    margin-top: 1rem;
}

.footer-note {
    font-size: 0.9rem;
    color: #555;
    text-align: center;
}
</style>
