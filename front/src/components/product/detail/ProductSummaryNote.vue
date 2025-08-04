<script setup>
const props = defineProps({
    product: Object,
});

// 더미 요약 텍스트
const summaryText = `
KB Star 정기예금은 고정금리·단리형 비대면 전용 상품으로, 최대 연 2.45% 금리를 제공합니다.
최소 100만 원부터 가입 가능하며, 1천만 원 예치 시 1년 세후 이자는 약 20만 원입니다.
복리 아님에 유의해야 하며, 중도해지 시 금리 손해가 크고 우대금리는 신규 시에만 적용됩니다.
`.trim();
</script>

<template>
    <div class="summary-note-box">
        <div class="header">
            <div class="title-row">
                <span>✨</span>
                <span class="title">상품 3줄 요약</span>
                <span>✨</span>
            </div>
            <div class="desc">
                해당 요약은 ai가 요약한 설명입니다. 해당 약관과는 다를 수
                있습니다.
            </div>
        </div>
        <hr />
        <div class="content">
            <p>{{ summaryText }}</p>
        </div>
    </div>
</template>

<style scoped>
.summary-note-box {
    background: white;
    border-radius: 1.5rem;
    padding: 2rem;
    box-shadow: inset 0 0 12px #a1c4fd;
    margin-top: 1.5rem;
    font-size: 1rem;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    margin-bottom: 1rem;
}

.title-row {
    display: flex;
    align-items: flex-end;
    gap: 0.5rem;
    font-weight: bold;
    font-size: 1.5rem;
}

.desc {
    font-size: 0.9rem;
    color: #666;
    white-space: nowrap;
}

.content p {
    white-space: pre-wrap;
    line-height: 1.6;
    font-size: 1rem;
    color: #222;
}
</style>
