<template>
  <div class="container-fluid mt-4">
    <div class="row">
      <!-- 항목 버튼 리스트 -->
      <div class="col-md-4">
        <div class="row row-cols-2 g-2">
          <TaxItemBox
            v-for="item in items"
            :key="item.label"
            :label="item.label"
            :amount="item.amount"
            @click="selectItem(item.label)"
            :active="activeItem === item.label"
          />
        </div>
      </div>
      <!-- 상세 항목 표시 영역 -->
      <div class="col-md-8">
        <TaxDetail :type="activeItem" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import TaxItemBox from './TaxItemBox.vue'
import TaxDetail from './TaxDetail.vue'
const activeItem = ref('의료비')
const items = [
  { label: '소득기준 초과 부양가족', amount: '0원' },
  { label: '건강/고용보험', amount: '300,110원' },
  { label: '국민연금', amount: '0원' },
  { label: '보험료', amount: '0원' },
  { label: '의료비', amount: '314,500원' },
  { label: '교육비', amount: '0원' },
  { label: '신용카드', amount: '2,789,200원' },
  { label: '직불카드 등', amount: '7,230,000원' },
  { label: '현금영수증', amount: '0원' },
  { label: '개인연금저축/연금계좌', amount: '3,210,000원' },
  { label: '주택자금/월세액', amount: '0원' },
  { label: '주택마련저축', amount: '0원' },
  { label: '장기집합투자증권저축/벤처기업투자신탁', amount: '0원' },
  { label: '기부금', amount: '0원' },
]
function selectItem(label) {
  activeItem.value = label
}
</script>