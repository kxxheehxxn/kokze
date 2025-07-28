<script setup>
import { useRoute, useRouter } from 'vue-router';
import api from '@/api/noticeApi';
import { ref, computed } from 'vue';
import moment from 'moment';
import { userAuthStore } from '@/stores/auth';
const auth = userAuthStore();
const route = useRoute();
const router = useRouter();
const noticeId = route.params.no;
const article = ref({});
const isAdmin = computed(() => auth.role === 'ADMIN');
const back = () => {
  router.push({ name: 'noticeList', query: route.query });
};
const update = () => {
  router.push({
    name: 'noticeUpdate',
    params: { no: noticeId },
    query: route.query,
  });
};
const remove = async () => {
  if (!confirm('삭제할까요?')) return;
  await api.delete(noticeId);
  router.push({ name: 'noticeList', query: route.query });
};
const load = async () => {
  article.value = {
    noticeId: 111,
    userId: 'user001',
    title: '임시 제목입니다.',
    content: '이건 임시 내용입니다. 프론트 확인용입니다.',
    createdAt: '2025-06-14T12:00:00',
  };
  article.value = await api.get(noticeId);
  console.log('DETAIL', article.value);
};
load();
</script>
<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">공지사항</h4>
        <div class="ms-1 mt-5">
          <h5 class="fw-bold my-4">
            {{ article.title }}
          </h5>
          <div class="notice-date">
            <span v-if="article.createdAt">
              {{ moment(article.createdAt).format('YYYY-MM-DD HH:mm') }}
            </span>
          </div>
          <div class="mt-3">
            <hr />
            <div class="content">{{ article.content }}</div>
          </div>
        </div>
      </div>
      <div class="d-flex mt-4 w-100 justify-content-between align-items-center">
        <button class="btn back" @click="back">목록</button>
        <template v-if="!article.isAnswered && isAdmin" class="w-100 text-end"
          ><div class="ms-auto">
            <button class="btn edit" @click="update">수정</button>
            <button class="btn delete" @click="remove">삭제</button>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<style scoped>
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
.notice-date {
  margin-top: 15px;
  color: #9a9a9a;
  font-size: 10pt;
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
  font-weight: bold;
}
.back {
  background-color: #3573ee;
}
.edit {
  background-color: #0d3a95;
}
.delete {
  margin-left: 15px;
  background-color: #fd5757;
}
.answer {
  background-color: #0d3a95;
}
.textarea-container {
  background: #fff;
  border-radius: 20px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 15px;
  box-sizing: border-box;
}
.textarea-input {
  width: 100%;
  height: 100px;
  border: none;
  outline: none;
  resize: none;
  font-size: 13px;
  background: transparent;
}
hr {
  margin-top: 20px;
  margin-bottom: 30px;
}
.answer-content {
  font-size: 14px;
}
</style>
