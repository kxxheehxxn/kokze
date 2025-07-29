<script setup>
import api from '@/api/inquiryApi'; // inquiryApi에 조회수 증가 API 호출 메서드가 필요합니다.
import { ref, reactive, computed, watch } from 'vue';
import moment from 'moment';
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';

const route = useRoute();
const router = useRouter();

const page = ref({}); // 일반 문의사항 페이지네이션 데이터 (백엔드에서 받은 totalCount 포함)
const faqInquiries = ref([]); // FAQ 게시글 목록

// 실제 페이지네이션 및 번호 매기기에 사용될 일반 게시글의 총 개수 (totalCount 보정)
// '1번이 마지막이 아니라 2번이 마지막' 문제를 해결하기 위해 totalCount에서 1을 뺌
const adjustedTotalGeneralInquiries = computed(() => {
  // page.value.totalCount는 백엔드에서 받은 일반 게시글의 총 개수라고 가정합니다.
  // 만약 이 값이 실제보다 1 크게 온다면, 여기서 1을 빼줍니다.
  // 음수가 되지 않도록 최소 0으로 설정
  return Math.max(0, (page.value.totalCount || 0) - 1);
});

// 모든 문의사항 (FAQ + 일반)을 합쳐서 계산된 속성으로 만듭니다.
const combinedInquiries = computed(() => {
  const currentPagedInquiries = page.value.list || [];
  const faqInfoIds = new Set(faqInquiries.value.map((faq) => faq.infoId));

  const filteredInquiries = currentPagedInquiries.filter(
    (inquiry) => !faqInfoIds.has(inquiry.infoId)
  );

  let combined = [];
  // 현재 페이지가 1페이지이고 검색 중이 아닐 때만 FAQ를 추가합니다.
  // 첫 페이지에 FAQ와 일반 게시글이 함께 표시되는 것은 현재 로직상 의도된 동작입니다.
  if (pageRequest.page === 1 && !searchKeyword.value) {
    combined = [...faqInquiries.value, ...filteredInquiries];
  } else {
    combined = [...filteredInquiries];
  }
  return combined;
});

const pageRequest = reactive({
  page: parseInt(route.query.page ?? 1),
  amount: parseInt(route.query.amount ?? 10),
});
const searchKeyword = ref('');

// 화면에 표시될 일반 문의의 실제 순서를 계산하는 함수 (FAQ 제외)
// 이 함수는 현재 페이지 내에서 필터링된 일반 게시글 목록에서의 0-기반 인덱스를 반환합니다.
const getDisplayedNonFaqIndex = (inquiry) => {
  const nonFaqList = (page.value.list || []).filter(
    (item) => !faqInquiries.value.some((faq) => faq.infoId === item.infoId)
  );
  return nonFaqList.findIndex((item) => item.infoId === inquiry.infoId);
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
          <tr
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
                FAQ
              </template>
              <template v-else>
                {{
                  adjustedTotalGeneralInquiries -
                  ((pageRequest.page - 1) * pageRequest.amount +
                    getDisplayedNonFaqIndex(inquiry))
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
        <div class="flex-grow-1 text-center">
          <vue-awesome-paginate
            :total-items="adjustedTotalGeneralInquiries"
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
