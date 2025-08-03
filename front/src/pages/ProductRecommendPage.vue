<template>
  <div class="product-recommend-page">
    <h2>금융 상품 추천</h2>

    <div class="carousel-wrapper">
      <div
        class="carousel-item"
        v-for="(product, index) in recommendedProducts"
        :key="index"
      >
        <h4>✨ {{ product.title }} ✨</h4>
        <p>{{ product.bank }} | {{ product.interestRate }}%</p>
        <p>{{ product.comment }}</p>
      </div>
    </div>

    <div class="category-filter">
      <button
        v-for="(cat, index) in categories"
        :key="index"
        :class="{ active: selectedCategory === cat }"
        @click="selectedCategory = cat"
      >
        {{ cat }}
      </button>
    </div>

    <div class="filter-box">
      <select v-model="selectedTerm">
        <option>기간 전체</option>
        <option>6개월</option>
        <option>1년</option>
        <option>2년</option>
      </select>
      <select v-model="selectedType">
        <option>상품유형 전체</option>
        <option>자유적금</option>
        <option>정기예금</option>
      </select>
    </div>

    <div class="product-list">
      <div class="product-item" v-for="item in filteredProducts" :key="item.id">
        <div class="product-bank">{{ item.bank }}</div>
        <div class="product-name">{{ item.name }}</div>
        <div class="product-rate">최고 {{ item.maxRate }}%</div>
      </div>
    </div>

    <div class="pagination">
      <button v-for="n in totalPages" :key="n" @click="currentPage = n">
        {{ n }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductRecommendPage',
  data() {
    return {
      recommendedProducts: [
        {
          title: '신한저축예금통장',
          bank: '신한',
          interestRate: 1.85,
          comment: '20대 여성 고객 Top 10',
        },
        {
          title: '국민저축예금통장',
          bank: '국민',
          interestRate: 2.0,
          comment: 'SH 여친 맞춤형 추천 상품',
        },
      ],
      categories: [
        '전체',
        '국민',
        '우리',
        '신한',
        '카카오뱅크',
        '농협',
        '토스뱅크',
        'SC제일',
      ],
      selectedCategory: '전체',
      selectedTerm: '기간 전체',
      selectedType: '상품유형 전체',
      productList: [
        { id: 1, bank: 'NH', name: 'NH고정적금', maxRate: 2.8 },
        { id: 2, bank: 'NH', name: 'NH자유적금', maxRate: 2.6 },
      ],
      currentPage: 1,
      itemsPerPage: 5,
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.productList

      if (this.selectedCategory !== '전체') {
        filtered = filtered.filter(p => p.bank === this.selectedCategory)
      }
      return filtered.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage,
      )
    },
    totalPages() {
      return Math.ceil(this.productList.length / this.itemsPerPage)
    },
  },
}
</script>

<style scoped>
.product-recommend-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}
.carousel-wrapper {
  display: flex;
  overflow-x: auto;
  gap: 1rem;
  margin-bottom: 2rem;
}
.carousel-item {
  min-width: 240px;
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 12px;
  border: 1px solid #ddd;
}
.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}
.category-filter button {
  padding: 0.4rem 1rem;
  border-radius: 24px;
  background: #eee;
  border: none;
}
.category-filter .active {
  background: #296bff;
  color: white;
}
.filter-box {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}
.product-list {
  border-top: 1px solid #ccc;
}
.product-item {
  display: flex;
  justify-content: space-between;
  padding: 0.8rem 0;
  border-bottom: 1px solid #eee;
}
.pagination {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}
</style>
