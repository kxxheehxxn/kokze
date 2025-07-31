<script setup>
import api from '@/api/inquiryApi'; // inquiryApi에 조회수 증가 API 호출 메서드가 필요합니다.
import { ref, reactive, computed, watch } from 'vue';
import moment from 'moment';
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';

const route = useRoute();
const router = useRouter();

const page = ref({}); // 일반 문의사항 페이지네이션 데이터
const faqInquiries = ref([]); // FAQ 게시글 목록

// 모든 문의사항 (FAQ + 일반)을 합쳐서 계산된 속성으로 만듭니다.
const combinedInquiries = computed(() => {
  const currentPagedInquiries = page.value.list || [];
  const faqInfoIds = new Set(faqInquiries.value.map((faq) => faq.infoId));

  const filteredInquiries = currentPagedInquiries.filter(
    (inquiry) => !faqInfoIds.has(inquiry.infoId)
  );

  const combined = [...faqInquiries.value, ...filteredInquiries];
  return combined;
});

const pageRequest = reactive({
  page: parseInt(route.query.page ?? 1),
  amount: parseInt(route.query.amount ?? 10),
});
const searchKeyword = ref('');

// getOriginalIndex 함수 정의
const getOriginalIndex = (inquiry) => {
  if (!faqInquiries.value.some((faq) => faq.infoId === inquiry.infoId)) {
    return (page.value.list || []).findIndex(
      (item) => item.infoId === inquiry.infoId
    );
  }
  return -1;
};

// 조회수 증가 및 상세 페이지 이동 처리 함수 추가
const goToInquiryDetail = async (infoId) => {
  try {
    // 백엔드의 새로운 조회수 증가 API 호출
    await api.increaseViewCount(infoId); // 이 메서드는 inquiryApi에 추가해야 합니다.
    console.log(`조회수 증가 성공: ${infoId}`);
  } catch (e) {
    console.error(`조회수 증가 실패: ${infoId}`, e);
    // 조회수 증가에 실패해도 상세 페이지로 이동은 계속 진행
  } finally {
    // 상세 페이지로 라우팅
    router.push({ name: 'inquiryDetail', params: { no: infoId } });
  }
};

const search = async () => {
  try {
    const params = {
      keyword: searchKeyword.value,
      page: pageRequest.page,
      amount: pageRequest.amount,
    };
    page.value = await api.getSearchList(params);
    console.log('검색 결과:', page.value);
  } catch (e) {
    console.error('검색 실패:', e);
  }
};

const handlePageChange = async (pageNum) => {
  router.push({
    query: { page: pageNum, amount: pageRequest.amount },
  });
};

const loadInquiries = async (query) => {
  try {
    page.value = await api.getList(query);
    console.log('일반 문의 목록:', page.value);
  } catch (e) {
    console.error('일반 문의 목록 로드 실패:', e);
  }
};

const loadFaq = async () => {
  try {
    faqInquiries.value = await api.getFaqList();
    console.log('FAQ 목록:', faqInquiries.value);
  } catch (e) {
    console.error('FAQ 목록 로드 실패:', e);
  }
};

watch(route, async () => {
  pageRequest.page = parseInt(route.query.page);
  pageRequest.amount = parseInt(route.query.amount);

  if (searchKeyword.value) {
    await search();
  } else {
    await loadInquiries(pageRequest);
  }
});

onMounted(async () => {
  await loadFaq();
  if (searchKeyword.value) {
    await search();
  } else {
    await loadInquiries(pageRequest);
  }
});
</script>
<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <h4 class="fw-bold m-2">문의사항</h4>
      <div class="d-flex justify-content-end mb-3">
        <div class="search-container mt-3 text-end">
          <input
            type="text"
            class="search-input"
            v-model="searchKeyword"
            @keyup.enter="search"
          />
          <i class="search-icon fa-solid fa-magnifying-glass" @click="search" />
        </div>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th style="width: 70px">No</th>
            <th>제목</th>
            <th style="width: 100px">작성자</th>
            <th style="width: 120px">작성일</th>
            <th style="width: 100px">조회수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="combinedInquiries.length === 0">
            <td colspan="5" class="text-center py-4 text-muted">
              게시글이 없습니다.
            </td>
          </tr>
          <tr
            v-else
            v-for="(inquiry, index) in combinedInquiries"
            :key="inquiry.infoId"
            :class="{
              'faq-row': faqInquiries.some(
                (faq) => faq.infoId === inquiry.infoId
              ),
            }"
          >
            <td style="width: 70px" class="text-center">
              <template
                v-if="faqInquiries.some((faq) => faq.infoId === inquiry.infoId)"
              >
                HOT
              </template>
              <template v-else>
                {{
                  (page.totalCount || 0) -
                  ((pageRequest.page - 1) * pageRequest.amount +
                    getOriginalIndex(inquiry))
                }}
              </template>
            </td>
            <td>
              <router-link
                class="ellipsis-title link-reset ms-5"
                :to="{ name: 'inquiryDetail', params: { no: inquiry.infoId } }"
                @click.prevent="goToInquiryDetail(inquiry.infoId)"
              >
                <span v-if="inquiry.isAnswered">[답변완료] </span
                >{{ inquiry.title }}
              </router-link>
            </td>
            <td class="grayfont ellipsis-writer">
              {{ inquiry.userName }}
            </td>
            <td class="grayfont">
              {{ moment(inquiry.createdAt).format('YYYY-MM-DD') }}
            </td>
            <td class="grayfont text-center">
              {{ inquiry.viewCount }}
            </td>
          </tr>
        </tbody>
      </table>
      <div>
        <div
          class="flex-grow-1 text-center"
          v-if="combinedInquiries.length > 0"
        >
          <vue-awesome-paginate
            :total-items="page.totalCount || 0"
            :items-per-page="pageRequest.amount"
            :max-pages-shown="5"
            :show-ending-buttons="true"
            v-model="pageRequest.page"
            @click="handlePageChange"
          >
            <template #first-page-button>
              <i class="fa-solid fa-angles-left" />
            </template>
            <template #prev-button>
              <i class="fa-solid fa-angle-left" />
            </template>
            <template #next-button>
              <i class="fa-solid fa-angle-right" />
            </template>
            <template #last-page-button>
              <i class="fa-solid fa-angles-right" />
            </template>
          </vue-awesome-paginate>
        </div>
        <div class="text-end">
          <router-link
            :to="{ name: 'inquiryCreate', query: route.query }"
            class="btn fw-bold"
            >문의 작성
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
/* 기존 스타일 유지 */
th {
  text-align: center;
}
tr {
  height: 48px;
}
td {
  vertical-align: middle;
  font-size: 15px;
}
.custom-box-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 70px;
  padding-bottom: 30px;
}
.custom-box {
  width: 920px;
  min-height: 530px;
  background-color: #fff;
  border-radius: 28px;
  padding: 2rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.grayfont {
  padding-left: 25px;
  color: #9a9a9a;
}
.btn {
  width: 110px;
  height: 41px;
  background-color: #3573ee;
  color: white;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.ellipsis-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  /* display: block; */
  max-width: 470px;
}
.ellipsis-writer {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100px;
}
.link-reset {
  color: inherit;
  text-decoration: none;
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
}
.search-input {
  flex: 1;
  height: 70%; /* 적당히 세로 크기 맞춤 */
  border: none;
  outline: none;
  font-size: 13px;
  border-radius: 20px;
}
.search-icon {
  width: 20px;
  height: 20px;
  color: #666;
  cursor: pointer;
}
table {
  margin-top: 80px;
}
i {
  background-color: transparent;
}
.faq-row td {
  background-color: #dfefff; /* 연한 하늘색 */
  font-weight: bold;
}
</style>
