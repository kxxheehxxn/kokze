<script setup>
import api from '@/api/noticeApi';
import { ref, reactive, computed, watch } from 'vue';
import moment from 'moment';
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';
import { userAuthStore } from '@/stores/auth';
const auth = userAuthStore();
const route = useRoute();
const router = useRouter();
const page = ref({});
const notices = computed(() => page.value?.list || []);
const isAdmin = computed(() => auth.role?.toLowerCase() === 'admin');
const totalCount = computed(() => page.value?.totalCount || 0);
const pageRequest = reactive({
  page: parseInt(route.query.page ?? 1),
  amount: parseInt(route.query.amount ?? 10),
});
const searchKeyword = ref('');

const search = async () => {
  try {
    const params = {
      keyword: searchKeyword.value,
      page: pageRequest.page,
      amount: pageRequest.amount,
    };
    page.value = await api.getSearchList(params);
    console.log('검색 결과:', page.value);
  } catch (error) {
    console.error('검색 실패:', error);
    page.value = {
      totalCount: 0,
      totalPage: 0,
      list: [],
      amount: 10,
      pageNum: 1,
    };
  }
};
//페이지네이션 - 페이지 변경
const handlePageChange = async (pageNum) => {
  router.push({
    query: { page: pageNum, amount: pageRequest.amount },
  });
};
// pageRequest의 값 변경된 경우 호출
watch(route, async () => {
  pageRequest.page = parseInt(route.query.page);
  pageRequest.amount = parseInt(route.query.amount);

  if (searchKeyword.value) {
    await search();
  } else {
    await load(pageRequest);
  }
});
onMounted(async () => {
  if (searchKeyword.value) {
    await search();
  } else {
    await load(pageRequest);
  }
});
const load = async (query) => {
  try {
    page.value = await api.getList(query);
    console.log(page.value);
  } catch (error) {
    console.error('공지사항 목록 로드 실패:', error);
    page.value = {
      totalCount: 0,
      totalPage: 0,
      list: [],
      amount: 10,
      pageNum: 1,
    };
  }
};
load(pageRequest);
</script>

<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <h4 class="fw-bold m-2">공지사항</h4>
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
      <!-- 공지사항 없을 때 메시지 -->
      <div
        v-if="!notices || notices.length === 0"
        class="text-center text-muted py-5"
      >
        공지사항이 없습니다.
      </div>

      <!-- 공지사항 리스트 -->
      <div v-else v-for="notice in notices" :key="notice.noticeId">
        <div class="my-3 ms-2">
          <router-link
            class="ellipsis-title link-reset mt-4"
            :to="{ name: 'noticeDetail', params: { no: notice.noticeId } }"
          >
            {{ notice.title }}
          </router-link>
          <div class="mt-3 date">
            {{ moment(notice.createdAt).format('YYYY-MM-DD HH:mm') }}
          </div>
        </div>
        <hr />
      </div>
      <div>
        <div class="flex-grow-1 text-center" v-if="totalCount > 0">
          <vue-awesome-paginate
            :total-items="totalCount"
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
        <div class="text-end" v-if="isAdmin">
          <router-link
            :to="{ name: 'noticeCreate', query: route.query }"
            class="btn fw-bold"
            >공지 작성
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
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
  font-size: large;
  font-weight: bold;
  display: block;
  max-width: 470px;
}
.date {
  color: #9a9a9a;
  font-size: 10pt;
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
</style>
