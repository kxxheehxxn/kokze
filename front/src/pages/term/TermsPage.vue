<template>
  <div class="terms-page">
    <TermsSidebar :categories="categories" v-model="selectedCategory" />
    <div class="terms-content">
      <div class="d-flex justify-content-end mb-3">
        <div class="search-container mt-3 text-end">
          <input
            v-model="search"
            placeholder="검색어를 입력하세요"
            type="text"
            class="search-input"
            @keyup.enter="handleSearch"
          />
          <i
            class="search-icon fa-solid fa-magnifying-glass"
            @click="handleSearch"
          />
        </div>
      </div>
      <TermsAccordion :terms="filteredTerms" />
    </div>
    <div class="scroll-top-btn" @click="scrollToTop">
      <div class="scroll-top-icon">
        <i class="fa-solid fa-chevron-up" style="color: #3573ee"></i>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import TermsSidebar from '@/components/TermsSidebar.vue';
import TermsAccordion from '@/components/TermsAccordion.vue';
import api from '@/api/termsApi.js';

const categories = ref([]);
const selectedCategory = ref('');
const search = ref('');
const appliedSearch = ref('');
const terms = ref([]);

onMounted(async () => {
  try {
    const allTerms = await api.fetchTerms();
    terms.value = allTerms;

    const uniqueCategories = [
      ...new Set(allTerms.map((term) => term.category)),
    ].sort();

    categories.value = uniqueCategories;

    if (!selectedCategory.value && categories.value.length > 0) {
      selectedCategory.value = categories.value[0];
    }
  } catch (error) {
    console.error('API에서 데이터를 불러오는 데 실패했습니다:', error);
  }
});

const filteredTerms = computed(() =>
  terms.value.filter(
    (term) =>
      term.category === selectedCategory.value &&
      (term.title.includes(appliedSearch.value) ||
        term.description.includes(appliedSearch.value))
  )
);

const handleSearch = () => {
  appliedSearch.value = search.value;
};

watch(selectedCategory, (newCategory, oldCategory) => {
  if (newCategory !== oldCategory && oldCategory !== '') {
    search.value = '';
    appliedSearch.value = '';
  }
});

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
};
</script>

<style scoped>
.terms-page {
  display: flex;
  gap: 0;
  background: #f6f6f6;
  min-height: 100vh;
}
.terms-content {
  flex: 1;
  padding: 40px 48px 0 20px;
  background: #f6f6f6;
}
.search-container {
  width: 320px;
  height: 37px;
  background: #fff;
  border-radius: 20px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 15px;
  box-sizing: border-box;
  transition: box-shadow 0.2s ease;
}
.search-container:focus-within {
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.15);
}
.search-input {
  flex: 1;
  height: 70%;
  border: none;
  outline: none;
  font-size: 13px;
  border-radius: 20px;
  background: transparent;
}
.search-input::placeholder {
  color: #999;
}
.search-icon {
  width: 20px;
  height: 20px;
  color: #666;
  cursor: pointer;
  transition: color 0.2s ease;
}
.search-icon:hover {
  color: #333;
}
.scroll-top-btn {
  cursor: pointer;
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 56px;
  height: 56px;
  background-color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #374151;
  border: 2px solid #e5e7eb;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.scroll-top-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.scroll-top-icon {
  font-size: 20px;
  transition: transform 0.3s ease;
}

.scroll-top-btn:hover .scroll-top-icon {
  transform: translateY(-2px);
}
</style>
