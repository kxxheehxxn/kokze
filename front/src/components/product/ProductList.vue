<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { bankNameMap } from '@/utils/bankMap';
import ProductPagination from './ProductPagination.vue';
import { fetchProductList, filterProducts } from '@/api/productApi';

const router = useRouter();
const sortKey = ref('max');
const currentPage = ref(1);
const itemsPerPage = 8;

const props = defineProps({
  filters: Object,
});

const products = ref([]);
const totalPages = ref(0);

// ✅ 필터 적용 여부 판단
const isFiltering = computed(() => {
  const f = props.filters || {};
  return (
    f.bankNames?.length > 0 ||
    f.joinMembers?.length > 0 ||
    f.productType ||
    f.minSaveTrm ||
    f.maxSaveTrm ||
    f.minAmount ||
    f.maxAmount ||
    f.hasSpclCnd === true
  );
});

// 🔽 상품 리스트 가져오기
const loadProducts = async () => {
  try {
    if (isFiltering.value) {
      const result = await filterProducts(props.filters);
      products.value = Array.isArray(result) ? result : result.products;
      totalPages.value = 1; // 의미 없는 값
    } else {
      const result = await fetchProductList(currentPage.value, itemsPerPage);
      products.value = result.products;
      totalPages.value = result.totalPages;
    }
  } catch (err) {
    console.error('상품 목록 불러오기 실패:', err);
    products.value = [];
    totalPages.value = 0;
  }
};

watch(currentPage, () => {
  if (!isFiltering.value) loadProducts();
});
watch(
  () => props.filters,
  () => {
    currentPage.value = 1; // 필터 적용 시 첫 페이지로 초기화
    loadProducts();
  }
);
onMounted(loadProducts);

// 🔽 아이콘 처리
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

// 🔽 정렬
const sortedProducts = computed(() => {
  return [...products.value].sort((a, b) =>
    sortKey.value === 'max'
      ? b.intrRate2 - a.intrRate2
      : b.intrRate - a.intrRate
  );
});

// 🔽 상세 이동
function goToDetail(product) {
  router
    .push({
      path: `/product/${product.finPrdtCd}`,
      state: { product },
    })
    .then(() => window.scrollTo(0, 0));
}

// 🔽 페이지 변경
function handlePageChange(page) {
  currentPage.value = page;
}
</script>

<template>
  <div class="product-list-wrapper">
    <div class="header-row">
      <div class="count">{{ sortedProducts.length }}개</div>
      <div class="sort">
        <select v-model="sortKey">
          <option value="max">최고금리순</option>
          <option value="basic">기본금리순</option>
        </select>
      </div>
    </div>

    <hr />

    <div
      v-for="product in sortedProducts"
      :key="product.finPrdtCd"
      class="product"
      @click="goToDetail(product)"
      style="cursor: pointer"
    >
      <div class="left">
        <img :src="getBankIcon(product.bankName)" class="bank-icon" />
        <div class="info">
          <div class="name">{{ product.productName }}</div>
          <div class="bank">{{ product.bankName }}</div>
        </div>
      </div>
      <div class="right">
        <div class="max">
          최고 <span>{{ product.intrRate2 }}%</span>
        </div>
        <div class="basic">
          기본 <span>{{ product.intrRate }}%</span>
        </div>
      </div>
    </div>

    <!-- ✅ 필터 적용 중일 때는 페이지네이션 숨김 -->
    <ProductPagination
      v-if="!isFiltering"
      :totalPages="totalPages"
      :page="currentPage"
      @update:page="handlePageChange"
    />
  </div>
</template>

<style scoped>
.product-list-wrapper {
  background: white;
  padding: 2rem;
  border-radius: 1.5rem;
  box-shadow: inset 0 0 12px #ddd;
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.count {
  font-weight: bold;
  font-size: 1rem;
  color: #1d4ed8;
}

.sort select {
  padding: 0.3rem 0.6rem;
  border: 1px solid #ccc;
  border-radius: 0.3rem;
  font-size: 0.9rem;
}

.product {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding: 1rem 0;
}

.left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.bank-icon {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.info .name {
  font-weight: bold;
  font-size: 1.1rem;
}

.info .bank {
  font-size: 0.9rem;
  color: #666;
  margin-top: 0.2rem;
}

.tags {
  margin-top: 0.3rem;
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
}

.tags span {
  background: #eee;
  padding: 0.2rem 0.5rem;
  font-size: 0.8rem;
  border-radius: 0.5rem;
  color: #333;
}

.right {
  text-align: right;
  font-size: 0.9rem;
}

.right .max {
  color: #22c55e;
  font-weight: bold;
}

.right .basic {
  color: #666;
  margin-top: 0.2rem;
}
</style>
