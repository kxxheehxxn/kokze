<template>
  <div class="goal-edit-page">
    <h2>목표 수정하기</h2>

    <!-- 목표 이름 -->
    <div class="form-group">
      <label>목표 이름</label>
      <input type="text" v-model="goal.title" />
    </div>

    <!-- 목표 기간 -->
    <div class="form-group">
      <label>목표 기간</label>
      <div class="date-range">
        <input type="date" v-model="goal.startDate" />
        <span>~</span>
        <input type="date" v-model="goal.endDate" />
      </div>
    </div>

    <!-- 목표 금액 -->
    <div class="form-group">
      <label>목표 금액</label>
      <div class="amount-input">
        <input type="number" v-model="goal.amount" />
        <span>원</span>
      </div>
      <p class="helper-text">{{ formattedAmount }}원</p>
    </div>

    <!-- 금융 상품 연결 -->
    <div class="form-group">
      <label>금융 상품 연결하기</label>
      <p class="subtext">최소 1개 이상의 상품을 연결해야 합니다.</p>

      <div class="product-card" v-if="selectedAccount">
        <span class="product-icon">💳</span>
        <div class="product-text">
          {{ selectedAccount.bankName }} - {{ selectedAccount.accountNum }}
        </div>
        <button class="remove-btn" @click="selectedAccount = null">－</button>
      </div>

      <div class="product-placeholder" v-else @click="showProductModal = true">
        <span>＋</span>
      </div>
    </div>

    <!-- 버튼 -->
    <div class="btn-row">
      <button class="btn cancel" @click="goBack">취소하기</button>
      <button class="btn submit" @click="submitEdit">목표 수정하기</button>
    </div>

    <!-- 금융 상품 연동 모달 -->
    <ProductModal
      v-if="showProductModal"
      :accounts="accounts"
      @close="showProductModal = false"
      @connect="handleProductConnect"
    />
  </div>
</template>

<script>
import axios from 'axios';
import ProductModal from '@/components/ProductModal.vue';

export default {
  name: 'GoalEditPage',
  components: {
    ProductModal,
  },
  props: ['goalId'],
  data() {
    return {
      goal: {
        title: '',
        startDate: '',
        endDate: '',
        amount: 0,
      },
      showProductModal: false,
      selectedAccount: null,
      accounts: [
        { accountId: 1, bankName: '우리', accountNum: '1234-****-5678' },
        { accountId: 2, bankName: '카카오뱅크', accountNum: '3333-12-4567890' },
      ],
    };
  },
  computed: {
    formattedAmount() {
      return Number(this.goal.amount).toLocaleString();
    },
  },
  mounted() {
    this.fetchGoalDetail();
  },
  methods: {
    async fetchGoalDetail() {
      try {
        const res = await axios.get(`/api/goals/${this.goalId}`);
        const data = res.data;
        this.goal.title = data.goalName;
        this.goal.startDate = data.startDate;
        this.goal.endDate = data.endDate;
        this.goal.amount = data.targetAmount;
      } catch (err) {
        alert('목표 정보를 불러오지 못했습니다.');
      }
    },
    async submitEdit() {
      try {
        await axios.put(`/api/goals/${this.goalId}`, {
          goal_name: this.goal.title,
          start_date: this.goal.startDate,
          end_date: this.goal.endDate,
          target_amount: this.goal.amount,
        });
        this.$router.push('/goals');
      } catch (err) {
        alert('목표 수정 실패');
      }
    },
    goBack() {
      this.$router.back();
    },
    handleProductConnect(accountId) {
      this.selectedAccount = this.accounts.find(
        (a) => a.accountId === accountId
      );
      this.showProductModal = false;
    },
  },
};
</script>

<style scoped>
.goal-edit-page {
  max-width: 700px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
  font-family: 'Noto Sans KR', sans-serif;
}

h2 {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}
label {
  font-weight: bold;
  display: block;
  margin-bottom: 0.5rem;
}
input[type='text'],
input[type='number'],
input[type='date'] {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 1rem;
}
.date-range {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.amount-input {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.helper-text {
  font-size: 0.85rem;
  color: #999;
  margin-top: 0.3rem;
}
.subtext {
  font-size: 0.85rem;
  color: #666;
  margin-top: -0.5rem;
  margin-bottom: 0.8rem;
}
.product-card {
  display: flex;
  align-items: center;
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 1rem;
}
.product-icon {
  font-size: 1.4rem;
  margin-right: 0.7rem;
}
.product-text {
  flex-grow: 1;
}
.remove-btn {
  background: #ddd;
  border: none;
  border-radius: 50%;
  font-size: 1.2rem;
  width: 32px;
  height: 32px;
  cursor: pointer;
}
.product-placeholder {
  height: 3.5rem;
  border: 2px dashed #ccc;
  border-radius: 12px;
  text-align: center;
  line-height: 3.5rem;
  font-size: 1.5rem;
  color: #aaa;
  cursor: pointer;
}
.btn-row {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-top: 2rem;
}
.btn {
  padding: 0.7rem 2rem;
  border-radius: 24px;
  font-weight: bold;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}
.cancel {
  background: #eee;
  color: #444;
}
.submit {
  background: #296bff;
  color: white;
}
</style>
