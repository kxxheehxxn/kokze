<script setup>
import { ref, computed } from 'vue';
import { bankNameMap } from '@/utils/bankMap';
import ProductPagination from './ProductPagination.vue';

const sortKey = ref('max'); // 'max' 또는 'basic'
const currentPage = ref(1);
const itemsPerPage = 10;

const props = defineProps({
    filters: Object,
});

// 더미 데이터
const products = ref([
    {
        id: 1,
        name: '우리 첫거래우대 정기예금',
        bank: '우리',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 1.8,
    },
    {
        id: 2,
        name: 'NH고향사랑기부예금',
        bank: 'NH농협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 2.15,
    },
    {
        id: 3,
        name: '황금빛예금(일반형)',
        bank: 'iM뱅크',
        tags: [],
        maxRate: 2.79,
        basicRate: 2.19,
    },
    {
        id: 4,
        name: 'NH대한민국 히어로예금',
        bank: 'NH농협',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 5,
        name: 'e-그린세이브예금',
        bank: 'SC제일',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 6,
        name: 'Sh해양플라스틱Zero!예금',
        bank: 'SH수협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.4,
    },
    {
        id: 7,
        name: '우리 첫거래우대 정기예금',
        bank: '우리',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 1.8,
    },
    {
        id: 8,
        name: 'NH고향사랑기부예금',
        bank: 'NH농협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 2.15,
    },
    {
        id: 9,
        name: '황금빛예금(일반형)',
        bank: 'iM뱅크',
        tags: [],
        maxRate: 2.79,
        basicRate: 2.19,
    },
    {
        id: 10,
        name: 'NH대한민국 히어로예금',
        bank: 'NH농협',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 11,
        name: 'e-그린세이브예금',
        bank: 'SC제일',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 12,
        name: 'Sh해양플라스틱Zero!예금',
        bank: 'SH수협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.4,
    },
    {
        id: 13,
        name: '황금빛예금(일반형)',
        bank: 'iM뱅크',
        tags: [],
        maxRate: 2.79,
        basicRate: 2.19,
    },
    {
        id: 14,
        name: 'NH대한민국 히어로예금',
        bank: 'NH농협',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 15,
        name: 'e-그린세이브예금',
        bank: 'SC제일',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.45,
    },
    {
        id: 16,
        name: 'Sh해양플라스틱Zero!예금',
        bank: 'SH수협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.75,
        basicRate: 2.4,
    },
    {
        id: 17,
        name: '우리 첫거래우대 정기예금',
        bank: '우리',
        tags: ['특판', '방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 1.8,
    },
    {
        id: 18,
        name: 'NH고향사랑기부예금',
        bank: 'NH농협',
        tags: ['방문없이가입', '누구나가입'],
        maxRate: 2.8,
        basicRate: 2.15,
    },
    {
        id: 19,
        name: '황금빛예금(일반형)',
        bank: 'iM뱅크',
        tags: [],
        maxRate: 2.79,
        basicRate: 2.19,
    },
    {
        id: 20,
        name: '황금빛예금(일반형)',
        bank: 'iM뱅크',
        tags: [],
        maxRate: 2.79,
        basicRate: 2.19,
    },
]);

// 아이콘 이미지 파일들을 모두 가져오기
const iconModules = import.meta.glob('@/assets/images/bankIcon/*.png', {
    eager: true,
    import: 'default',
});

// 은행 이름(영문)을 받아 아이콘 경로를 반환
const getBankIcon = (bankName) => {
    const english = bankNameMap[bankName];
    if (!english) return '';
    const match = Object.entries(iconModules).find(([path]) =>
        path.includes(`/${english}.png`)
    );
    return match ? match[1] : '';
};

// 필터링
const filteredProducts = computed(() => {
    if (!props.filters) return products.value;

    return products.value.filter((product) => {
        const {
            banks = [],
            period = 0,
            amount = '',
            type = [],
            conditions = [],
        } = props.filters;

        // 은행 필터
        if (banks.length > 0 && !banks.includes(product.bank)) return false;

        // 기간 필터
        if (period > 0) {
            if (!product.period) return false;
            if (
                product.period !== period &&
                !(period === 25 && product.period > 24)
            ) {
                return false;
            }
        }

        // 금액 필터
        if (amount) {
            const amt = parseInt(amount);
            if (isNaN(amt) || amt < 100000) return false;
            if (product.minAmount && amt < product.minAmount) return false;
        }

        // 유형 필터 (모두 포함)
        if (type.length > 0 && !type.every((t) => product.tags.includes(t))) {
            return false;
        }

        // 조건 필터 (하나만 포함해도 통과)
        if (
            conditions.length > 0 &&
            !conditions.some((c) => product.tags.includes(c))
        ) {
            return false;
        }

        return true;
    });
});

// 필터링 결과에 정렬
const sortedProducts = computed(() => {
    return [...filteredProducts.value].sort((a, b) => {
        return sortKey.value === 'max'
            ? b.maxRate - a.maxRate
            : b.basicRate - a.basicRate;
    });
});

// 페이지 이동
const paginatedProducts = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    return sortedProducts.value.slice(start, start + itemsPerPage);
});
</script>

<template>
    <div class="product-list-wrapper">
        <div class="header-row">
            <div class="count">{{ filteredProducts.length }}개</div>
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
            :key="product.id"
            class="product"
        >
            <div class="left">
                <img :src="getBankIcon(product.bank)" class="bank-icon" />
                <div class="info">
                    <div class="name">{{ product.name }}</div>
                    <div class="bank">{{ product.bank }}</div>
                    <div class="tags">
                        <span v-for="tag in product.tags" :key="tag">{{
                            tag
                        }}</span>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="max">
                    최고 <span>{{ product.maxRate.toFixed(2) }}%</span>
                </div>
                <div class="basic">
                    기본 {{ product.basicRate.toFixed(2) }}%
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
