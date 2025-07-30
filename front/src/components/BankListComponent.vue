<template>
  <div class="bank-list">
    <h3>은행 선택</h3>
    
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>은행 목록을 불러오는 중...</p>
    </div>

    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="loadBankList" class="retry-btn">다시 시도</button>
    </div>

    <div v-else class="bank-grid">
      <div
        v-for="bank in banks"
        :key="bank.bankCode"
        class="bank-item"
        @click="selectBank(bank)"
        :class="{ 'selected': selectedBank?.bankCode === bank.bankCode }"
      >
        <div class="bank-icon">
          <img 
            v-if="bank.bankIcon" 
            :src="bank.bankIcon" 
            :alt="bank.bankName"
            @error="handleImageError"
          />
          <div v-else class="bank-placeholder">{{ bank.bankName.charAt(0) }}</div>
        </div>
        <div class="bank-name">{{ bank.bankName }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getBankList } from '@/api/userApi';

const banks = ref([]);
const loading = ref(true);
const error = ref(null);
const selectedBank = ref(null);

const emit = defineEmits(['bank-selected']);

const loadBankList = async () => {
  loading.value = true;
  error.value = null;

  try {
    const data = await getBankList();
    banks.value = data;
  } catch (err) {
    console.error('Failed to load bank list:', err);
    error.value = '은행 목록을 불러올 수 없습니다. 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
};

const selectBank = (bank) => {
  selectedBank.value = bank;
  emit('bank-selected', bank);
};

const handleImageError = (event) => {
  // 이미지 로드 실패 시 기본 아이콘으로 대체
  event.target.style.display = 'none';
  const placeholder = event.target.parentElement.querySelector('.bank-placeholder');
  if (placeholder) {
    placeholder.style.display = 'flex';
  }
};

onMounted(() => {
  loadBankList();
});
</script>

<style scoped>
.bank-list {
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.bank-list h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 18px;
  font-weight: 600;
}

.loading, .error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: #666;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #189eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.retry-btn {
  margin-top: 12px;
  padding: 8px 16px;
  background: #189eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.bank-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.bank-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 12px;
  border: 2px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #fafafa;
}

.bank-item:hover {
  border-color: #189eff;
  background: #f0f8ff;
}

.bank-item.selected {
  border-color: #189eff;
  background: #e3f2fd;
}

.bank-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bank-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.bank-placeholder {
  width: 100%;
  height: 100%;
  background: #189eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
}

.bank-name {
  font-size: 12px;
  color: #333;
  text-align: center;
  font-weight: 500;
  line-height: 1.2;
}
</style> 