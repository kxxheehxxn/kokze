<script setup>
import api from '@/api/inquiryApi';
import { ref, reactive, computed, watch } from 'vue';
import moment from 'moment';
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';
const route = useRoute();
const router = useRouter();
const page = ref({});
const inquiries = computed(() => page.value.list);
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
  } catch (e) {
    console.error('검색 실패:', e);
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
  } catch {}
};
load(pageRequest);
const maskName = (name) => {
  if (!name || name.length < 2) return name;
  return name[0] + '*' + name.slice(2);
};
</script>
<template>
  <div class="position-relative">
    <div class="position-absolute custom-box shadow p-5">
      <h4 class="fw-bold m-2">문의사항</h4>
      <div class="search-container my-3">
        <input
          type="text"
          class="search-input"
          v-model="searchKeyword"
          @keyup.enter="search"
        />
        <i class="search-icon fa-solid fa-magnifying-glass" @click="search" />
      </div>
      <table class="table">
        <thead>
          <tr>
            <th style="width: 70px">No</th>
            <th>제목</th>
            <th style="width: 100px">작성자</th>
            <th style="width: 120px">작성일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(inquiry, index) in inquiries" :key="inquiry.infoId">
            <td style="width: 70px" class="text-center">
              {{
                page.totalCount -
                ((pageRequest.page - 1) * pageRequest.amount + index)
              }}
            </td>
            <td>
              <router-link
                class="ellipsis-title link-reset ms-5"
                :to="{ name: 'inquiryDetail', params: { no: inquiry.infoId } }"
              >
                {{ inquiry.title }}
              </router-link>
            </td>
            <td class="grayfont ellipsis-writer">
              {{ maskName(inquiry.userName) }}
            </td>
            <td class="grayfont">
              {{
                moment(
                  new Date(
                    inquiry.createdAt.year,
                    inquiry.createdAt.monthValue - 1,
                    inquiry.createdAt.dayOfMonth
                  )
                ).format('YYYY-MM-DD')
              }}
            </td>
          </tr>
        </tbody>
      </table>
      <div>
        <div class="flex-grow-1 text-center">
          <vue-awesome-paginate
            :total-items="page.totalCount"
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
.custom-box {
  width: 920px;
  min-height: 530px;
  top: 100px;
  left: 296px;
  bottom: 80px;
  border-radius: 28px;
  background-color: #ffffff;
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
  display: block;
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
  position: absolute;
  left: 555px;
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
