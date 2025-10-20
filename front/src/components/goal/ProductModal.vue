<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h3>📂 금융상품 연동</h3>
      <label>연동할 계좌 선택</label>
      <select v-model="selectedAccountId">
        <option disabled value="">계좌를 선택해주세요</option>
        <option
          v-for="account in accounts"
          :key="account.account_id"
          :value="account.account_id"
        >
          {{ account.bank_name }} - {{ account.account_num }}
        </option>
      </select>
      <div class="btn-row">
        <button class="btn cancel" @click="$emit('close')">취소</button>
        <button
          class="btn confirm"
          @click="onConnect"
          :disabled="!selectedAccountId"
        >
          연동
        </button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ProductModal',
  props: {
    accounts: Array,
  },
  data() {
    return {
      selectedAccountId: '',
      selectedBank: null,
    };
  },
  methods: {
    onBankSelected(bank) {
      this.selectedBank = bank;
    },
    onConnect() {
      if (!this.selectedAccountId) {
        alert('계좌를 선택하세요!');
        return;
      }
      this.$emit('connect', this.selectedAccountId);
    },
  },
};
</script>
<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  color: #333;
}
.modal-content h3 {
  margin: 0 0 1.5rem 0;
  font-size: 1.5rem;
}
.bank-selection {
  margin-bottom: 1.5rem;
}
.bank-selection label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}
.account-selection {
  margin-bottom: 1.5rem;
}
.account-selection label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}
select {
  width: 100%;
  margin: 0.5rem 0;
  padding: 0.6rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}
.btn-row {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}
.btn {
  padding: 0.6rem 1.2rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.cancel {
  background: #eee;
  color: #333;
}
.cancel:hover {
  background: #ddd;
}
.confirm {
  background: #296bff;
  color: white;
}
.confirm:hover:not(:disabled) {
  background: #1e5ae6;
}
</style>
