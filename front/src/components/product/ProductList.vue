<script setup>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { bankNameMap } from '@/utils/bankMap';
import ProductPagination from './ProductPagination.vue';

const router = useRouter();
const sortKey = ref('max'); // 'max' 또는 'basic'
const currentPage = ref(1);
const itemsPerPage = 10;

const props = defineProps({
    filters: Object,
});

// DB 맞춤 더미 데이터
const products = [
    {
        finPrdtCd: 'WR0001F',
        productName: '우리SUPER주거래적금',
        bankName: '우리은행',
        intrRate: 2.15,
        intrRate2: 3.55,
    },
    {
        finPrdtCd: 'WR0001L',
        productName: 'WON적금',
        bankName: '우리은행',
        intrRate: 2.95,
        intrRate2: 3.15,
    },
    {
        finPrdtCd: '00266451',
        productName: '퍼스트가계적금',
        bankName: '한국스탠다드차타드은행',
        intrRate: 2.55,
        intrRate2: 2.55,
    },
    {
        finPrdtCd: '10527001000925000',
        productName: '영플러스적금',
        bankName: '아이엠뱅크',
        intrRate: 2.71,
        intrRate2: 3.26,
    },
    {
        finPrdtCd: '01020400490002',
        productName: '펫 적금',
        bankName: '부산은행',
        intrRate: 2.0,
        intrRate2: 2.9,
    },
];

// 아이콘 이미지 파일들을 모두 가져오기
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

// 필터링
const filteredProducts = computed(() => {
    return products.filter((p) => {
        const f = props.filters;

        if (f.bankNames?.length && !f.bankNames.includes(p.bankName))
            return false;
        if (f.productType && !p.productName.includes(f.productType))
            return false;
        return true;
    });
});

// filters가 바뀌면 페이지 초기화
watch(
    () => props.filters,
    () => {
        currentPage.value = 1;
    },
    { deep: true }
);

// 정렬
const sortedProducts = computed(() => {
    return [...filteredProducts.value].sort((a, b) => {
        return sortKey.value === 'max'
            ? b.intrRate2 - a.intrRate2
            : b.intrRate - a.intrRate;
    });
});

// 상세보기 이동
function goToDetail(product) {
    router
        .push({
            path: `/product/${product.finPrdtCd}`,
            state: { product },
        })
        .then(() => window.scrollTo(0, 0));
}

// 페이지 이동
const paginatedProducts = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    return sortedProducts.value.slice(start, start + itemsPerPage);
});
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
            v-for="product in paginatedProducts"
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

        <ProductPagination
            :total="sortedProducts.length"
            v-model:page="currentPage"
            :perPage="itemsPerPage"
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
