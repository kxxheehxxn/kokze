<template>
  <div class="card" @click="$emit('click')">
    <div class="card-header">
      <img
        :src="getBankIcon(product.korCoNm)"
        alt="은행 로고"
        class="bank-icon"
      />
      <div>
        <div class="product-name">{{ product.finPrdtNm }}</div>
        <div class="bank-name">{{ product.korCoNm }}</div>
      </div>
    </div>
    <hr />
    <div class="highlight">
      ✨ 적립 유형: {{ product.rsrvTypeNm }} / {{ product.saveTrm }}개월 ✨
    </div>
    <div class="rate">
      최고 {{ product.intrRate2 }}% / 기본 {{ product.intrRate }}%
    </div>
  </div>
</template>
<script>
import { bankNameMap } from '@/utils/bankMap';
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
  eager: true,
  import: 'default',
});
const defaultIcon = new URL(
  '@/assets/images/bankIcon/default.png',
  import.meta.url
).href;
export default {
  name: 'RecommendedProductCard',
  props: {
    product: {
      type: Object,
      required: true,
    },
  },
  methods: {
    getBankIcon(bankName) {
      const english = bankNameMap[bankName];
      if (!english) return defaultIcon;
      const match = Object.entries(iconModules).find(([path]) =>
        path.includes(`/${english}.png`)
      );
      return match ? match[1] : defaultIcon;
    },
  },
};
</script>
<style scoped>
.card {
  width: 350px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 1rem;
  padding: 1.2rem;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);
  text-align: left;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 0 15px #3573ee;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: white;
  border-bottom: none;
}
.card-header > div {
  flex: 1;
  min-width: 0;
}
.bank-icon {
  width: 45px;
  height: 45px;
  object-fit: contain;
}
.product-name {
  font-weight: bold;
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.bank-name {
  font-size: 0.85rem;
  color: #666;
  margin-top: 0.2rem;
}
.highlight {
  font-weight: bold;
}
.rate {
  margin: 0.5rem;
  font-weight: normal;
  color: #222;
  font-size: 0.95rem;
}
</style>
