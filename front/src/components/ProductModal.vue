<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h3>📂 금융상품 연동</h3>
      <label>연동할 계좌 선택</label>
      <select v-model="selectedAccountId">
        <option
          v-for="account in accounts"
          :key="account.accountId"
          :value="account.accountId"
        >
          {{ account.bankName }} - {{ account.accountNum }}
        </option>
      </select>

      <div class="btn-row">
        <button class="btn cancel" @click="$emit('close')">취소</button>
        <button class="btn confirm" @click="onConnect">연동</button>
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
      selectedAccountId: null,
    };
  },
  methods: {
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
  width: 400px;
}
select {
  width: 100%;
  margin: 1rem 0;
  padding: 0.6rem;
}
.btn-row {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}
.btn {
  padding: 0.6rem 1.2rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
}
.cancel {
  background: #eee;
}
.confirm {
  background: #296bff;
  color: white;
}
</style>
