<template>
  <div class="terms-page">
    <TermsSidebar :categories="categories" v-model="selectedCategory" />
    <div class="terms-content">
      <div class="terms-search-wrap">
        <input
          v-model="search"
          placeholder="검색어를 입력하세요"
          class="terms-search"
        />
        <span class="search-icon">🔍</span>
      </div>
      <TermsAccordion :terms="filteredTerms" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import TermsSidebar from '@/components/TermsSidebar.vue'
import TermsAccordion from '@/components/TermsAccordion.vue'
import { fetchTerms } from '@/api/termsApi.js'

const categories = ['예금', '적금', '보험', '세금']
const selectedCategory = ref('예금')
const search = ref('')
const terms = ref([])

onMounted(async () => {
  terms.value = await fetchTerms()
})

const filteredTerms = computed(() =>
  terms.value.filter(
    term =>
      term.category === selectedCategory.value &&
      (term.title.includes(search.value) ||
        term.description.includes(search.value)),
  ),
)
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
  padding: 40px 48px 0 48px;
  background: #f6f6f6;
}
.terms-search-wrap {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-bottom: 32px;
  position: relative;
  background: #f6f6f6;
}
.terms-search {
  width: 320px;
  padding: 12px 40px 12px 18px;
  border: none;
  border-radius: 24px;
  background: #fff;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  font-size: 16px;
  outline: none;
}
.search-icon {
  position: absolute;
  right: 18px;
  font-size: 18px;
  color: #bdbdbd;
  pointer-events: none;
}
</style>
