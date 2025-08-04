<script setup>
import { ref } from 'vue';

const isExpanded = ref(false);

const infoData = [
    {
        label: '기간',
        value: '1개월 이상 36개월 이하(월 단위)',
    },
    {
        label: '금액',
        value: '1백만원 이상 (추가입금 불가)',
    },
    {
        label: '가입방법',
        value: 'KB스타뱅킹, 인터넷뱅킹, 고객센터 (영업점 가입 불가)',
    },
    {
        label: '대상',
        value: '개인 및 개인사업자',
    },
    {
        label: '이자지급',
        value: '만기일시지급식 : 만기(후) 또는 중도해지 요청시 이자를 지급',
    },
    {
        label: '유의',
        value: `• 본 설명서는 법령 및 내부통제기준에 따른 절차를 거쳐 제공됩니다.\n• 본 상품은 예금자보호를 받는 상품입니다.\n• 기타 약관은 은행 홈페이지 참고`,
    },
    {
        label: '예금자 보호',
        value: '예금자보호법에 따라 예금 보호. 1인당 최고 5천만원까지 보호됨',
    },
];
</script>

<template>
    <div class="info-box">
        <h2 class="title">상품 안내</h2>
        <hr />

        <div class="info-rows">
            <template v-if="isExpanded">
                <div
                    v-for="item in infoData"
                    :key="item.label"
                    class="info-row"
                >
                    <div class="label">{{ item.label }}</div>
                    <div
                        class="value"
                        v-html="item.value.replaceAll('\n', '<br />')"
                    />
                </div>
            </template>
            <template v-else>
                <div
                    v-for="item in infoData.slice(0, 4)"
                    :key="item.label"
                    class="info-row"
                >
                    <div class="label">{{ item.label }}</div>
                    <div
                        class="value"
                        v-html="item.value.replaceAll('\n', '<br />')"
                    />
                </div>
            </template>
        </div>

        <div class="more-btn-wrapper" v-if="!isExpanded">
            <button class="more-btn" @click="isExpanded = true">
                더보기 <span class="icon">⌄</span>
            </button>
        </div>
    </div>
</template>

<style scoped>
.info-box {
    background: white;
    border-radius: 1.5rem;
    padding: 2rem;
    box-shadow: inset 0 0 12px #a1c4fd;
    margin-top: 1.5rem;
    font-size: 1rem;
}

.title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 1rem;
}

.info-rows {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.info-row {
    display: grid;
    grid-template-columns: 120px 1fr;
    gap: 1rem;
    align-items: start;
}

.label {
    font-weight: bold;
    color: #111;
    white-space: nowrap;
}

.value {
    color: #333;
    line-height: 1.6;
    word-break: keep-all;
}

.more-btn-wrapper {
    text-align: center;
    margin-top: 2rem;
}

.more-btn {
    border: 1px solid #ccc;
    padding: 0.6rem 1.2rem;
    border-radius: 0.5rem;
    background: white;
    cursor: pointer;
    font-weight: bold;
}
</style>
