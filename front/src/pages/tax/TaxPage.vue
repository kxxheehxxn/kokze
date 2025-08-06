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

          <component v-else :is="detailComponentName" :label="selected" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DefaultInfo from './details/DefaultInfo.vue';
import MedicalDetail from './details/MEdicalDetail.vue';
import InsuranceDetail from './details/InsuranceDetail.vue';
import PensionDetail from './details/PensionDetail.vue';
import DebitCardDetail from './details/DebitCardDetail.vue';
import FunVentureDetail from './details/FunVentureDetail.vue';
import DonationDetail from './details/DonationDetail.vue';
import OverIncomeDetail from './details/OverIncomeDetail.vue';
import HealthInsuranceDetail from './details/HealthInsuranceDetail.vue';
import NationalPensionDetail from './details/NationalPensionDetail.vue';
import CashReceiptDetail from './details/CashReceiptDetail.vue';
import HousingSavingDetail from './details/HousingSavingDetail.vue';
import EducationDetail from './details/EducationDetail.vue';
import CreditCardDetail from './details/CreditCardDetail.vue';
import HousingDetail from './details/HousingDetail.vue';

export default {
  name: 'TaxPage',
  components: {
    CreditCardDetail,
    DefaultInfo,
    MedicalDetail,
    InsuranceDetail,
    PensionDetail,
    DebitCardDetail,
    HousingDetail,
    HousingSavingDetail,
    EducationDetail,
    FunVentureDetail,
    DonationDetail,
    OverIncomeDetail,
    HealthInsuranceDetail,
    NationalPensionDetail,
    CashReceiptDetail,
  },
  data() {
    return {
      selected: '',
      taxAmounts: {
        '소득기준 초과 부양가족': '0원',
        '건강/고용보험': '300,110원',
        국민연금: '0원',
        보험료: '0원',
        의료비: '314,500원',
        교육비: '0원',
        신용카드: '2,789,200원',
        '직불카드 등': '7,230,000원',
        현금영수증: '0원',
        '개인연금저축/연금계좌': '0원',
        '주택자금/월세액': '0원',
        주택마련저축: '0원',
        '장기집합투자증권저축/벤처기업투자신탁': '0원',
        기부금: '0원',
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
        '기부금',
      ],
      componentMap: {
        '소득기준 초과 부양가족': 'OverIncomeDetail',
        '건강/고용보험': 'HealthInsuranceDetail',
        국민연금: 'NationalPensionDetail',
        보험료: 'InsuranceDetail',
        의료비: 'MedicalDetail',
        교육비: 'EducationDetail',
        신용카드: 'CreditCardDetail',
        '직불카드 등': 'DebitCardDetail',
        현금영수증: 'CashReceiptDetail',
        '개인연금저축/연금계좌': 'PensionDetail',
        '주택자금/월세액': 'HousingDetail',
        주택마련저축: 'HousingSavingDetail',
        '장기집합투자증권저축/벤처기업투자신탁': 'FunVentureDetail',
        기부금: 'DonationDetail',
      },
    };
  },
  computed: {
    detailComponentName() {
      const componentName = this.componentMap[this.selected];
      return componentName ? this.$options.components[componentName] : 'DefaultInfo';
    },
  },
  methods: {
    handleCardClick(label) {
      this.selected = this.selected === label ? '' : label;
    },
  },
};
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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

@media (max-width: 1024px) {
  .tax-wrapper {
    padding: 16px; /* 패딩 약간 줄임 */
  }
  .tax-grid {
    flex: 1.2;
    grid-template-columns: 1fr 1fr; /* 2개 컬럼 고정 */
  }
  .tax-info {
    flex: 1;
  }
  /* 텍스트 크기를 점진적으로 줄임 */
  .tax-label,
  .tax-amount {
    /* 1024px에서 16px, 768px에서 10px가 되도록 계산 */
    font-size: calc(16px - (1024px - 100vw) * 0.0234);
    /* 계산식: (16-10) / (1024-768) = 6/256 ≈ 0.0234 */
  }
  .tax-card {
    padding: 12px; /* 카드 패딩 줄임 */
  }
}

@media (max-width: 768px) {
  .tax-wrapper {
    padding: 12px;
    overflow: hidden; /* 중요: wrapper에서 overflow 숨김 */
  }

  .tax-content {
    flex-direction: column; /* 세로 방향으로 정렬 */
    gap: 16px; /* 간격 줄임 */
  }

  /* tax-grid 스타일 완전히 재설정 */
  .tax-grid {
    /* 기존 grid 속성들을 모두 초기화 */
    display: flex !important;
    grid-template-columns: unset !important;
    flex: none !important;

    /* 스크롤 관련 속성 */
    overflow-x: auto;
    overflow-y: hidden;

    /* 레이아웃 속성 */
    gap: 16px;
    padding: 8px 0 16px 0;

    /* 너비 설정 - 부모 컨테이너보다 넓게 */
    width: 100%;

    /* 스크롤 동작 강제 */
    scroll-behavior: smooth;
  }

  /* 카드 크기 고정 및 축소 방지 */
  .tax-card {
    width: 150px; /* 고정 너비 */
    height: 80px; /* 고정 높이 */
    flex-shrink: 0; /* 카드 축소 방지 */
    flex-grow: 0; /* 카드 확장 방지 */
    padding: 10px;
    font-size: 12px;
  }

  .tax-label,
  .tax-amount {
    font-size: 12px; /* 폰트 크기 줄임 */
    margin: 0;
    line-height: 1.3;
  }

  .tax-label {
    margin-bottom: 4px;
  }

  /* tax-info 스타일 조정 */
  .tax-info {
    flex: none;
    min-width: auto;
    padding: 16px;
  }

  /* 스크롤바 스타일링 (웹킷 브라우저) */
  .tax-grid::-webkit-scrollbar {
    height: 6px;
  }

  .tax-grid::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 3px;
  }

  .tax-grid::-webkit-scrollbar-thumb {
    background: #cbd5e1;
    border-radius: 3px;
  }

  .tax-grid::-webkit-scrollbar-thumb:hover {
    background: #94a3b8;
  }
}
</style>
