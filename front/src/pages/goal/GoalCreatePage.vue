<template>
  <div class="goal-create-page">
    <div class="goal-create-card">
      <h2 class="title">목표 추가하기</h2>

      <hr />

      <div class="input-form">
        <!-- 목표 이름 -->
        <div class="form-group">
          <label>목표 이름</label>
          <input type="text" v-model="goalName" placeholder="예: 내 집 마련" />
        </div>

        <!-- 목표 기간 -->
        <div class="form-group">
          <label>목표 기간</label>
          <div class="date-range">
            <input type="date" v-model="startDate" />
            <span>~</span>
            <input type="date" v-model="endDate" :min="endDateMin" />
          </div>
        </div>

        <!-- 목표 금액 -->
        <div class="form-group">
          <label>목표 금액</label>
          <div class="amount-input">
            <input
              type="text"
              v-model="targetAmount"
              @input="onInputChange"
              placeholder="금액을 입력하세요"
            />
            <span>원</span>
            <button class="clear-btn" @click="clearInput">×</button>
          </div>
          <p class="helper-text">{{ formatKoreanCurrency(parsedAmount) }}</p>
        </div>

        <!-- 입금 날짜 -->
        <div class="form-group">
          <label>입금 날짜 (매월 며칠)</label>
          <input type="number" v-model="depositDate" min="1" max="28" />
          <p class="helper-text">1~28 사이의 숫자 입력</p>
        </div>

        <!-- 금융 상품 연결 -->
        <div class="form-group">
          <label>금융 상품 연결하기</label>
          <p class="subtext">
            기존에 가입한 금융 상품이 있다면 연동해서 더 쉽게 관리하세요.
          </p>

          <div class="product-card" v-if="selectedAccount">
            <span class="product-icon">💳</span>
            <div class="product-text">
              {{ selectedAccount.bank_name }} -
              {{ selectedAccount.account_num }}
            </div>

            <button class="remove-btn" @click="selectedAccount = null">
              －
            </button>
          </div>

          <div class="product-placeholder" v-else @click="fetchAccounts">
            ＋
          </div>
        </div>

        <!-- 버튼 -->
        <div class="btn-row">
          <button class="btn cancel" @click="onCancel">취소하기</button>
          <button class="btn submit" @click="onSubmit">목표 추가하기</button>
        </div>
      </div>

      <ProductModal
        v-if="showProductModal"
        :accounts="accounts"
        @close="showProductModal = false"
        @connect="handleProductConnect"
      />
    </div>
  </div>
</template>

<script>
import {
  getAccountsByUserId,
  createGoal,
  linkAccountToGoal,
} from '@/api/goalApi';
import ProductModal from '@/components/ProductModal.vue';
import { userAuthStore } from '@/stores/auth';

export default {
  name: 'GoalCreatePage',
  components: {
    ProductModal,
  },
  data() {
    return {
      goalName: '',
      startDate: '',
      endDate: '',
      targetAmount: 0,
      depositDate: 1,
      showProductModal: false,
      selectedAccount: null,
      accounts: [], // ✅ 서버에서 불러올 수도 있음
    };
  },
  computed: {
    parsedAmount() {
      return Number(String(this.targetAmount).replace(/\D/g, '')) || 0;
    },
    formattedAmount() {
      if (!this.parsedAmount) return '';
      return this.parsedAmount.toLocaleString();
    },
    today() {
      const now = new Date();
      return now.toISOString().split('T')[0];
    },
    endDateMin() {
      if (!this.startDate) return this.today;
      return this.startDate > this.today ? this.startDate : this.today;
    },
  },
  methods: {
    onCancel() {
      this.$router.back();
    },
    onInputChange(e) {
      this.targetAmount = e.target.value.replace(/\D/g, '');
    },
    clearInput() {
      this.targetAmount = '';
    },
    formatKoreanCurrency(num) {
      if (isNaN(num) || num <= 0) return '0원';
      const jo = Math.floor(num / 1_0000_0000_0000);
      const uk = Math.floor((num % 1_0000_0000_0000) / 1_0000_0000);
      const man = Math.floor((num % 1_0000_0000) / 10000);
      const rest = num % 10000;

      let result = '';
      if (jo > 0) result += `${jo}조`;
      if (uk > 0) result += `${uk}억`;
      if (man > 0) result += `${man}만`;
      if (rest > 0) result += rest.toLocaleString();
      return result + '원';
    },
    async onSubmit() {
      const auth = userAuthStore();
      const userId = auth.state.user.userId;
      const token = auth.getToken();

      // 필드 검증
      if (
        !this.goalName ||
        !this.startDate ||
        !this.endDate ||
        !this.targetAmount
      ) {
        alert('모든 필드를 입력해주세요.');
        return;
      }

      const requestBody = {
        goal_name: this.goalName,
        target_amount: this.targetAmount,
        save_amount: 0,
        start_date: this.startDate,
        end_date: this.endDate,
        deposit_date: this.depositDate,
      };

      try {
        // 1. 목표 생성 후 goal_id 수신
        const res = await createGoal(userId, requestBody, token);
        const goalId = res.data.goal_id;

        // 2. 계좌 선택 시 연동
        if (this.selectedAccount) {
          await linkAccountToGoal(
            goalId,
            this.selectedAccount.account_id,
            token
          );
          alert('목표와 계좌가 성공적으로 연동되었습니다!');
        } else {
          alert('목표가 성공적으로 등록되었습니다!');
        }

        this.$router.push('/goals');
      } catch (error) {
        console.error('❌ 등록 실패:', error);
        alert('등록에 실패했습니다.');
      }
    },
    async fetchAccounts() {
      const auth = userAuthStore();
      const userId = auth.state.user.userId;
      const token = auth.getToken();

      try {
        const res = await getAccountsByUserId(userId, token);
        this.accounts = res.data;
        this.showProductModal = true;
      } catch (err) {
        alert('계좌를 불러오지 못했습니다.');
      }
    },
    handleProductConnect(accountId) {
      this.selectedAccount = this.accounts.find(
        (a) => a.account_id === accountId
      );
      this.showProductModal = false;
    },
  },
};
</script>

<style scoped>
.goal-create-page {
  padding: 2rem;
}

.goal-create-card {
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1rem;
}

.input-form {
  padding: 1rem 10rem;
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
  box-shadow: inset 0 0 5px #eee;
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
  position: relative;
}

.amount-input .clear-btn {
  position: absolute;
  right: 2.2rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #aaa;
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
  border-radius: 20px;
  padding: 0.5rem 3rem;
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

@media (max-width: 1024px) {
  .input-form {
    padding: 1rem;
  }
}
</style>
