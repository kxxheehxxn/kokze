<script setup>
import { useRoute, useRouter } from 'vue-router';
import api from '@/api/inquiryApi';
import { ref } from 'vue';
import moment from 'moment';
//import { useAuthStore } from '@/stores/auth';
//const auth = useAuthStore();
const route = useRoute();
const router = useRouter();
const infoId = route.params.no;
const article = ref({});
const back = () => {
  router.push({ name: 'inquiryList', query: route.query });
};
const update = () => {
  router.push({
    name: 'inquiryUpdate',
    params: { no: infoId },
    query: route.query,
  });
};
const remove = async () => {
  if (!confirm('삭제할까요?')) return;
  await api.delete(infoId);
  router.push({ name: 'inquiryList', query: route.query });
};
const load = async () => {
  article.value = await api.get(infoId);
  console.log('DETAIL', article.value);
};
load();
const maskName = (name) => {
  if (!name || name.length < 2) return name;
  return name[0] + '*' + name.slice(2);
};
</script>

<template>
  <div class="position-relative">
    <div class="position-absolute custom-box shadow p-5">
      <div class="m-2">
        <h4 class="fw-bold mb-2">문의사항</h4>
        <h5 class="fw-bold my-4">{{ article.title }}</h5>
        <div class="inquiry-info">
          <span>{{ maskName(article.userName) }}</span>
          <span v-if="article.createdAt" class="ms-5">
            {{
              moment(
                new Date(
                  article.createdAt.year,
                  article.createdAt.monthValue - 1,
                  article.createdAt.dayOfMonth,
                  article.createdAt.hour,
                  article.createdAt.minute,
                  article.createdAt.second
                )
              ).format('YYYY-MM-DD HH:mm')
            }}
          </span>
        </div>
        <div class="mt-3">
          <hr />
          <div class="content">{{ article.content }}</div>
        </div>
      </div>
      <div class="mt-4">
        <button class="btn fw-bold back" @click="back">목록</button>
        <button class="btn fw-bold edit" @click="update">수정</button>
        <button class="btn fw-bold delete" @click="remove">삭제</button>
      </div>
    </div>
  </div>
</template>
<!-- 로그인 사용자와 작성자 uid가 같은 경우 -->
<!-- 로그인 사용자의 role이 admin인 경우 -->
<!-- <template v-if="auth.userId == article.userId && !article.isAnswered">
     <template v-if="String(auth.userId) === String(article.userId) && !article.isAnswered">
        <button class="btn btn-primary me-2" @click="update">
          수정
        </button>
        <button class="btn btn-danger" @click="remove">
          삭제
        </button>
      </template> -->
<style scoped>
.custom-box {
  width: 920px;
  min-height: 500px;
  top: 100px;
  left: 296px;
  bottom: 80px;
  border-radius: 28px;
  background-color: #ffffff;
}
.inquiry-info {
  margin-top: 15px;
  color: #9a9a9a;
  font-size: 14px;
}
.content {
  min-height: 100px;
}
.btn {
  width: 100px;
  height: 41px;
  color: white;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  margin-top: 85px;
}
.back {
  background-color: #3573ee;
}
.edit {
  margin-left: 500px;
  background-color: #0d3a95;
}
.delete {
  margin-left: 15px;
  background-color: #fd5757;
}
</style>
