<template>
  <div class="goal-edit-page">
    <div class="goal-edit-card">
      <h2 class="title">목표 수정하기</h2>
      <hr />
      <div class="input-form">
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
            <input type="date" v-model="goal.endDate" :min="endDateMin" />
          </div>
          <p class="helper-text">입력하지 않을 시 기존 날짜로 설정됩니다.</p>
        </div>
        <!-- 목표 금액 -->
        <div class="form-group">
          <label>목표 금액</label>
          <div class="amount-input">
            <input type="text" v-model="goal.amount" @input="onInputChange" />
            <span>원</span>
            <button class="clear-btn" @click="clearInput">×</button>
          </div>
          <p class="helper-text">{{ formatKoreanCurrency(parsedAmount) }}</p>
        </div>
        <!-- 입금 날짜 -->
        <div class="form-group">
          <label>입금 날짜 (매월 며칠)</label>
          <input type="number" v-model="goal.depositDate" min="1" max="28" />
          <p class="helper-text">1~28 사이의 숫자 입력</p>
        </div>
        <!-- 금융 상품 연결 -->
        <div class="form-group">
          <label>금융 상품 연결하기</label>
          <p class="subtext">최소 1개 이상의 상품을 연결해야 합니다.</p>
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
          <button class="btn cancel" @click="goBack">취소하기</button>
          <button class="btn submit" @click="submitEdit">목표 수정하기</button>
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
  getGoalById,
  updateGoal,
  getAccountsByUserId,
  linkAccountToGoal,
  unlinkAccount,
} from '@/api/goalApi';
import ProductModal from '@/components/ProductModal.vue';
import { userAuthStore } from '@/stores/auth';
export default {
  name: 'GoalEditPage',
  components: { ProductModal },
  data() {
    return {
      goal: {
        title: '',
        startDate: '',
        endDate: '',
        amount: 0,
        depositDate: 1,
      },
      selectedAccount: null,
      prevAccountId: null,
      showProductModal: false,
      accounts: [],
    };
  },
  computed: {
    parsedAmount() {
      return Number(String(this.goal.amount).replace(/\D/g, '')) || 0;
    },
    formattedAmount() {
      if (!this.goal.amount) return '';
      return this.goal.amount.toLocaleString();
    },
    today() {
      const now = new Date();
      return now.toISOString().split('T')[0];
    },
    endDateMin() {
      if (!this.goal.startDate) return this.today;
      return this.goal.startDate > this.today
        ? this.goal.startDate
        : this.today;
    },
  },
  mounted() {
    this.fetchGoalDetail();
  },
  methods: {
    onInputChange(e) {
      this.goal.amount = e.target.value.replace(/\D/g, '');
    },
    clearInput() {
      this.goal.amount = '';
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
    formatDateForInput(dateStr) {
      const date = new Date(dateStr);
      if (isNaN(date)) return '';
      return date.toISOString().split('T')[0];
    },
    async fetchGoalDetail() {
      const goalId = this.$route.params.goalId;
      const token = userAuthStore().getToken();
      try {
        const res = await getGoalById(goalId, token);
        const data = res.data;
        this.goal.title = data.goal_name;
        this.goal.startDate = this.formatDateForInput(data.start_date);
        this.goal.endDate = this.formatDateForInput(data.end_date);
        this.goal.amount = data.target_amount;
        this.goal.depositDate = data.deposit_date;
        if (data.linked_accounts?.length > 0) {
          const acc = data.linked_accounts[0];
          this.selectedAccount = {
            account_id: acc.account_id,
            bank_name: acc.bank_name,
            account_num: acc.account_num,
          };
          this.prevAccountId = acc.account_id;
        }
      } catch (err) {
        alert('목표 정보를 불러오지 못했습니다.');
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
    async submitEdit() {
      const goalId = this.$route.params.goalId;
      const token = userAuthStore().getToken();
      try {
        await updateGoal(
          goalId,
          {
            goal_name: this.goal.title,
            start_date: this.goal.startDate,
            end_date: this.goal.endDate,
            target_amount: this.goal.amount,
            deposit_date: this.goal.depositDate,
          },
          token
        );
        const selectedId = this.selectedAccount?.account_id;
        if (selectedId !== this.prevAccountId) {
          if (this.prevAccountId) {
            await unlinkAccount(this.prevAccountId, token);
          }
          if (selectedId) {
            await linkAccountToGoal(goalId, selectedId, token);
          }
        }
        alert('목표가 성공적으로 수정되었습니다!');
        this.$router.push('/goals');
      } catch (err) {
        alert('목표 수정 실패');
        console.error(err);
      }
    },
    goBack() {
      this.$router.back();
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
.goal-edit-page {
  padding: 2rem;
}
.goal-edit-card {
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
