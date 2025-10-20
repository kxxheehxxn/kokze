<script setup>
import { useRoute, useRouter } from 'vue-router';
import api from '@/api/noticeApi';
import { ref, computed } from 'vue';
import moment from 'moment';
import { userAuthStore } from '@/stores/auth';
import BaseModal from '@/components/BaseModal.vue';
const auth = userAuthStore();
const route = useRoute();
const router = useRouter();
const noticeId = route.params.no;
const article = ref({});
const isAdmin = computed(() => (auth.role ?? '').toLowerCase() === 'admin');
const modalVisible = ref(false);
const modalMessage = ref('');
const modalButtons = ref([]);
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
  if (!(await showConfirm('삭제할까요?'))) return;
  await api.delete(noticeId);
  router.push({ name: 'noticeList', query: route.query });
};
const load = async () => {
  article.value = await api.get(noticeId);
};
function showConfirm(message) {
  return new Promise((resolve) => {
    modalMessage.value = message;
    modalButtons.value = [
      {
        text: '취소',
        onClick: () => {
          modalVisible.value = false;
          resolve(false);
        },
      },
      {
        text: '삭제',
        onClick: () => {
          modalVisible.value = false;
          resolve(true);
        },
      },
    ];
    modalVisible.value = true;
  });
}
load();
</script>
<template>
  <div class="container">
    <div class="custom-box-wrapper">
      <div class="custom-box p-5">
        <div class="m-2 content-wrapper">
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
        <div
          class="d-flex footer w-100 justify-content-between align-items-center"
        >
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
    <BaseModal
      :visible="modalVisible"
      :message="modalMessage"
      :buttons="modalButtons"
    />
  </div>
</template>
<style scoped>
.container {
  background-color: #fbfbfb;
}
.custom-box-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 70px;
  padding-bottom: 30px;
  margin: 0px 30px;
}
.custom-box {
  width: 920px;
  min-height: 530px;
  background-color: #fff;
  border-radius: 28px;
  padding: 2rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column; /* 세로 방향으로 배치 */
}
.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
}
/* 버튼 그룹을 하단으로 밀고, 아래에서 20px 띄우기 */
.footer {
  margin-top: auto; /* 아래로 밀기 */
  padding-bottom: 20px; /* 박스 하단으로부터 20px 띄우기 효과 */
  gap: 8px;
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
@media (max-width: 1024px) and (orientation: portrait) {
  .custom-box {
    min-height: 80vh; /* 화면 높이의 80% 이상 확보 */
  }
}
</style>
