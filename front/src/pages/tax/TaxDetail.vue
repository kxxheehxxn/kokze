<template>
  <div class="tax-page">
    <div class="tax-wrapper">
      <div class="tax-content">
        <div class="tax-grid">
          <div
            v-for="(label, idx) in taxItems"
            :key="idx"
            class="tax-card"
            :class="{ active: selected === label }"
            @click="handleCardClick(label)"
          >
            <p class="tax-label">{{ label }}</p>
            <p class="tax-amount">{{ taxAmounts[label] || '0원' }}</p>
          </div>
        </div>
        <div class="tax-info">
          <DefaultInfo v-if="!selected" />
          <component
            v-else
            :is="detailComponentName"
            :label="selected"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import DefaultInfo from './details/DefaultInfo.vue'
import MedicalDetail from './details/MEdicalDetail.vue'
import InsuranceDetail from './details/InsuranceDetail.vue'
import PensionDetail from './details/PensionDetail.vue'
import DebitCardDetail from './details/DebitCardDetail.vue'
import SavingDetail from './details/SavingDetail.vue'
import RentDetail from './details/RentDetail.vue'
import HouseSavingDetail from './details/HouseSavingDetail.vue'
import FunVentureDetail from './details/FunVentureDetail.vue'
import DonationDetail from './details/DonationDetail.vue'
import OverIncomeDetail from './details/OverIncomeDetail.vue'
import HealthInsuranceDetail from './details/HealthInsuranceDetail.vue'
import NationalPensionDetail from './details/NationalPensionDetail.vue'
import CashReceiptDetail from './details/CashReceiptDetail.vue'
export default {
  name: 'TaxPage',
  components: {
    DefaultInfo,
    MedicalDetail,
    InsuranceDetail,
    PensionDetail,
    CashDetail,
    DebitCardDetail,
    SavingDetail,
    RentDetail,
    HouseSavingDetail,
    FunVentureDetail,
    DonationDetail,
    OverIncomeDetail,
    HealthInsuranceDetail,
    NationalPensionDetail,
    CashReceiptDetail
  },
  data() {
    return {
      selected: '',
      taxAmounts: {
        '소득기준 초과 부양가족': '0원',
        '건강/고용보험': '300,110원',
        '국민연금': '0원',
        '보험료': '0원',
        '의료비': '314,500원',
        '교육비': '0원',
        '신용카드': '2,789,200원',
        '직불카드 등': '7,230,000원',
        '현금영수증': '0원',
        '개인연금저축/연금계좌': '0원',
        '주택자금/월세액': '0원',
        '주택마련저축': '0원',
        '장기집합투자증권저축/벤처기업투자신탁': '0원',
        '기부금': '0원'
      },
      taxItems: [
        '소득기준 초과 부양가족',
        '건강/고용보험',
        '국민연금',
        '보험료',
        '의료비',
        '교육비',
        '신용카드',
        '직불카드 등',
        '현금영수증',
        '개인연금저축/연금계좌',
        '주택자금/월세액',
        '주택마련저축',
        '장기집합투자증권저축/벤처기업투자신탁',
        '기부금'
      ],
      componentMap: {
        '소득기준 초과 부양가족': 'OverIncomeDetail',
        '건강/고용보험': 'HealthInsuranceDetail',
        '국민연금': 'NationalPensionDetail',
        '보험료': 'InsuranceDetail',
        '의료비': 'MedicalDetail',
        '교육비': 'HousingDetail',
        '신용카드': 'DebitCardDetail',
        '직불카드 등': 'DebitCardDetail',
        '현금영수증': 'CashReceiptDetail',
        '개인연금저축/연금계좌': 'PensionDetail',
        '주택자금/월세액': 'HousingSavingDetail',
        '주택마련저축': 'HousingSavingDetail',
        '장기집합투자증권저축/벤처기업투자신탁': 'FunVentureDetail',
        '기부금': 'DonationDetail'
      }
    }
  },
  computed: {
    detailComponentName() {
      const componentName = this.componentMap[this.selected];
      return componentName ? this.$options.components[componentName] : 'DefaultInfo';
    }
  },
  methods: {
    handleCardClick(label) {
      this.selected = this.selected === label ? '' : label
    }
  }
}
</script>
<style scoped>
/* 기존 스타일 유지 */
.tax-page {
  background-color: #ffffff;
  padding: 40px;
  min-height: 100vh;
  box-sizing: border-box;
}
.tax-wrapper {
  background-color: #ffffff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  max-width: 1280px;
  margin: 0 auto;
}
.tax-content {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}
.tax-grid {
  background-color: #ffffff;
  flex: 1.618;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.tax-card {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 16px;
  text-align: center;
  transition: all 0.2s ease;
  color: #111827;
}
.tax-label,
.tax-amount {
  font-size: 16px;
  font-weight: bold;
  color: #111827;
}
.tax-card:hover {
  cursor: pointer;
  transform: translateY(-2px);
  transition: all 0.2s ease;
}
.tax-info {
  flex: 1;
  min-width: 280px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 20px;
  box-sizing: border-box;
}
.info-title,
.info-subtitle {
  margin: 0;
  background-color: transparent;
}
.info-title {
  background-color: #ffffff;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
  color: #111827;
}
.info-subtitle {
  background-color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #1f2937;
}
.info-list {
  background-color: #ffffff;
  list-style: disc;
  padding-left: 16px;
  margin-bottom: 16px;
  font-size: 13px;
  color: #4b5563;
  line-height: 1.5;
}
.info-list li {
  margin-bottom: 8px;
  background-color: transparent;
}
.info-button {
  width: 100%;
  padding: 12px;
  background-color: #3b82f6;
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.info-button:hover {
  background-color: #2563eb;
}
.tax-card:hover {
  cursor: pointer;
  transform: translateY(-2px);
}
.tax-card.active {
  border: 2px solid #1d4ed8;
  background-color: #1d4ed8;
}
.tax-card.active .tax-label,
.tax-card.active .tax-amount {
  color: #ffffff;
}
</style>