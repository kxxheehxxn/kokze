<template>
  <div class="container">
    <div class="goal-create-page">
      <div class="goal-create-card">
        <h2 class="title">목표 추가하기</h2>
        <hr />
        <div class="input-form">
          <!-- 목표 이름 -->
          <div class="form-group">
            <label>목표 이름</label>
            <input
              type="text"
              v-model="goalName"
              placeholder="예: 내 집 마련"
            />
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
                {{ selectedAccount.bank_name }} :
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
          <div class="text-center">
            <button class="btn cancel-btn" @click="onCancel">취소하기</button>
            <button
              class="btn submit-btn ms-4"
              :disabled="loading"
              @click="onSubmit"
            >
              {{ loading ? '처리 중...' : '목표 추가' }}
            </button>
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
  </div>
  <BaseModal
    :visible="modalVisible"
    :message="modalMessage"
    :buttons="modalButtons"
  />
</template>
<script>
import {
  getAccountsByUserId,
  createGoal,
  linkAccountToGoal,
  deleteGoalById,
} from '@/api/goalApi';
import ProductModal from '@/components/goal/ProductModal.vue';
import { userAuthStore } from '@/stores/auth';
import BaseModal from '@/components/BaseModal.vue';

export default {
  name: 'GoalCreatePage',
  components: { ProductModal, BaseModal },
  data() {
    return {
      goalName: '',
      startDate: '',
      endDate: '',
      targetAmount: 0,
      depositDate: 1,
      showProductModal: false,
      selectedAccount: null,
      accounts: [],
      MAX_NAME_LENGTH: 255,
      loading: false,
      modalVisible: false,
      modalMessage: '',
      modalButtons: [],
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
  mounted() {
    const { amount, start, end } = this.$route.query;
    if (amount && start && end) {
      this.targetAmount = parseInt(amount);
      this.startDate = this.toDateInputFormat(start);
      this.endDate = this.toDateInputFormat(end);
    }
  },
  methods: {
    onCancel() {
      this.$router.back();
    },
    toDateInputFormat(dateStr) {
      if (typeof dateStr === 'string' && dateStr.includes('-')) return dateStr;
      const date = new Date(dateStr);
      return date.toISOString().split('T')[0]; // yyyy-MM-dd
    },
    onInputChange(e) {
      this.targetAmount = e.target.value.replace(/\D/g, '');
    },
    clearInput() {
      this.targetAmount = '';
    },
    showModal(message, buttons) {
      this.modalMessage = message;
      this.modalButtons = buttons;
      this.modalVisible = true;
    },
    hideModal() {
      this.modalVisible = false;
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
      if (this.loading) return;
      this.loading = true;

      const auth = userAuthStore();
      const userId = auth.state.user.userId;
      const token = auth.getToken();

      if (
        !this.goalName ||
        !this.startDate ||
        !this.endDate ||
        !this.targetAmount
      ) {
        this.showModal('모든 필드를 입력해주세요.', [
          { text: '확인', onClick: this.hideModal },
        ]);
        return;
      }

      if (
        this.selectedAccount &&
        (this.selectedAccount.is_linked || this.selectedAccount.goal_id)
      ) {
        this.showModal('이미 연동되어있는 계좌입니다.', [
          { text: '확인', onClick: this.hideModal },
        ]);
        this.loading = false;
        return;
      }

      const body = {
        goal_name: this.goalName,
        target_amount: this.parsedAmount,
        save_amount: 0,
        start_date: this.startDate,
        end_date: this.endDate,
        deposit_date: this.depositDate,
      };

      let goalId = null;

      try {
        const res = await createGoal(userId, body, token);
        goalId = res.data.goal_id;
      } catch (err) {
        console.error(
          '[createGoal:error]',
          err?.response?.status,
          err?.response?.data
        );

        this.showModal('기간 대비 목표 금액이 과도합니다.', [
          { text: '확인', onClick: this.hideModal },
        ]);
        this.loading = false;
        return;
      }

      if (this.selectedAccount) {
        try {
          await linkAccountToGoal(
            goalId,
            this.selectedAccount.account_id,
            token
          );
          this.showModal('목표와 계좌가 성공적으로 연동되었습니다!', [
            {
              text: '확인',
              onClick: () => {
                this.hideModal();
                this.$router.push('/goals');
              },
            },
          ]);
          this.loading = false;
          return;
        } catch (err) {
          console.error(
            '[linkAccountToGoal:error]',
            err?.response?.status,
            err?.response?.data
          );

          try {
            await deleteGoalById(goalId, token);
          } catch (rbErr) {
            console.error(
              '[deleteGoalById:rollback-fail]',
              rbErr?.response?.status,
              rbErr?.response?.data
            );
          }

          this.showModal('이미 연동되어있는 계좌입니다.', [
            { text: '확인', onClick: this.hideModal },
          ]);
          this.loading = false;
          return;
        }
      }

      this.showModal('목표가 성공적으로 등록되었습니다!', [
        {
          text: '확인',
          onClick: () => {
            this.hideModal();
            this.$router.push('/goals');
          },
        },
      ]);
      this.loading = false;
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
        console.error('[getAccountsByUserId:error]', err);
        this.showModal('계좌를 불러오지 못했습니다.', [
          { text: '확인', onClick: this.hideModal },
        ]);
      }
    },

    handleProductConnect(accountId) {
      const acc = this.accounts.find((a) => a.account_id === accountId);

      if (acc?.is_linked || acc?.goal_id) {
        this.showModal('이미 연동되어있는 계좌입니다.', [
          { text: '확인', onClick: this.hideModal },
        ]);
        return;
      }
      this.selectedAccount = acc;
      this.showProductModal = false;
    },
  },
};
</script>

<style scoped>
.container {
  background-color: #fbfbfb;
}
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
.btn {
  width: 180px;
  height: 48px;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
}
.cancel-btn {
  background: #fafbfc;
  color: #222;
  border: 1.5px solid #e5e7eb;
}
.submit-btn {
  background: #2573ee;
  color: #fff;
  border: none;
}
@media (max-width: 1024px) {
  .input-form {
    padding: 1rem;
  }
}
</style>
