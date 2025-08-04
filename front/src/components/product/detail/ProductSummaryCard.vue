<script setup>
import { bankNameMap } from '@/utils/bankMap';
import { bankUriMap } from '@/utils/bankUriMap'; // 🔽 새로 추가

const props = defineProps({
  product: Object,
});

// 은행 아이콘 처리
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
  eager: true,
  import: 'default',
});
const defaultIcon = new URL(
  '@/assets/images/bankIcon/default.png',
  import.meta.url
).href;

const getBankIcon = (bankName) => {
  const english = bankNameMap[bankName];
  if (!english) return defaultIcon;
  const match = Object.entries(iconModules).find(([path]) =>
    path.includes(`/${english}.png`)
  );
  return match ? match[1] : defaultIcon;
};

// 🔽 은행 URI 찾기
const getBankUri = (bankName) => bankUriMap[bankName] || null;

// 🔽 버튼 클릭 시 이동
const openBankHomepage = () => {
  const url = getBankUri(props.product.bankName);
  if (url) {
    window.open(url, '_blank');
  } else {
    alert('해당 은행의 공식 홈페이지 주소가 등록되지 않았습니다.');
  }
};
</script>

<template>
  <div class="summary-card">
    <div class="header">
      <div>
        <h1 class="title">{{ product.productName }}</h1>
        <p class="bank">{{ product.bankName }}</p>
      </div>
      <img :src="getBankIcon(product.bankName)" alt="은행 로고" class="logo" />
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

    <button class="cta-button" @click="openBankHomepage">
      공식 홈페이지 더 알아보기
    </button>

    <div class="footer-note">
      금융 상품 가입 후 ‘홈에서 자산 조회’를 클릭하여 금융 상품을 연결하세요!
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
