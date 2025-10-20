<script setup>
import { useRoute, useRouter } from 'vue-router';
import api from '@/api/inquiryApi';
import { ref, computed } from 'vue';
import moment from 'moment';
import { userAuthStore } from '@/stores/auth';
import BaseModal from '@/components/BaseModal.vue';

const isLoading = ref(false);
const MAX_CONTENT_LENGTH = 1000;
const auth = userAuthStore();
const route = useRoute();
const router = useRouter();
const infoId = route.params.no;
const article = ref({});
const isEditingAnswer = ref(false);
const isAdmin = computed(() => (auth.role ?? '').toLowerCase() === 'admin');
const modalVisible = ref(false);
const modalMessage = ref('');
const modalButtons = ref([]);
function showModal(message) {
  return new Promise((resolve) => {
    modalMessage.value = message;
    modalButtons.value = [
      {
        text: '확인',
        onClick: () => {
          modalVisible.value = false;
          resolve();
        },
      },
    ];
    modalVisible.value = true;
  });
}

// confirm 대체
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
        text: '확인',
        onClick: () => {
          modalVisible.value = false;
          resolve(true);
        },
      },
    ];
    modalVisible.value = true;
  });
}

const back = () => {
  router.push({ name: 'inquiryList', query: route.query });
};

const updateAnswer = async () => {
  if (!isEditingAnswer.value) {
    isEditingAnswer.value = true;
    return;
  }
  if (!article.value.answeredContent || !article.value.answeredContent.trim()) {
    await showModal('답변 내용을 입력해주세요.');
    return;
  }
  if (!(await showConfirm('답변을 수정할까요?'))) return;

  const updatedFields = {
    title: article.value.title,
    infoId: article.value.infoId,
    userId: article.value.userId,
    answeredContent: article.value.answeredContent,
  };
  try {
    await api.updateAnswer(updatedFields);
    isEditingAnswer.value = false;
    await load();
  } catch (error) {
    console.error('답변 수정 중 오류 발생:', error);
    await showModal('답변 수정 중 오류가 발생했습니다.');
  }
};

const update = () => {
  router.push({
    name: 'inquiryUpdate',
    params: { no: infoId },
    query: route.query,
  });
};

const submit = async () => {
  if (!(await showConfirm('답변을 등록할까요?'))) return;
  if (!article.value.answeredContent || !article.value.answeredContent.trim()) {
    await showModal('답변 내용을 입력해주세요.');
    return;
  }
  isLoading.value = true;
  const updatedFields = {
    title: article.value.title,
    infoId: article.value.infoId,
    userId: article.value.userId,
    isAnswered: article.value.isAnswered,
    answeredContent: article.value.answeredContent,
  };
  try {
    await api.updateAnswer(updatedFields);
    isLoading.value = false; // 먼저 로딩 끔
    await showModal('답변이 성공적으로 등록되었습니다.');
    router.push({
      name: 'inquiryDetail',
      params: { no: article.value.infoId },
      query: route.query,
    });
    await load();
  } catch (error) {
    isLoading.value = false;
    await showModal('답변 등록 중 오류가 발생했습니다.');
  }
};

const remove = async () => {
  if (!(await showConfirm('게시글을 삭제할까요?'))) return;
  await api.delete(infoId);
  router.push({ name: 'inquiryList', query: route.query });
};

const load = async () => {
  article.value = await api.get(infoId);
};
load();
</script>
<template>
  <div class="container">
    <div class="custom-box-wrapper">
      <div class="custom-box p-5">
        <div class="m-2 content-wrapper">
          <h4 class="fw-bold">문의사항</h4>
          <div class="ms-1">
            <h5 class="fw-bold my-4">
              <span v-if="article.isAnswered">[답변완료] </span
              >{{ article.title }}
            </h5>
            <div class="inquiry-info">
              <span>{{ article.userName }}</span>
              <span v-if="article.createdAt" class="ms-5">
                {{ moment(article.createdAt).format('YYYY-MM-DD HH:mm') }}
              </span>
            </div>
            <div class="mt-3">
              <hr />
              <div class="content">{{ article.content }}</div>
            </div>
            <!-- 답변 페이지 -->
            <!-- 1. 관리자 화면 -->
            <div class="mt-5" v-if="isAdmin">
              <div v-if="!article.isAnswered">
                <form @submit.prevent="submit">
                  <div class="d-flex mb-3 mt-3 align-items-start">
                    <div class="textarea-container w-100">
                      <textarea
                        class="form-control textarea-input"
                        v-model="article.answeredContent"
                        rows="10"
                      ></textarea>
                    </div>
                    <button
                      class="btn ms-3 answer"
                      type="submit"
                      :disabled="!article.answeredContent || isLoading"
                    >
                      입력
                    </button>
                  </div>
                </form>
              </div>
              <div v-else>
                <div class="d-flex footer w-100 justify-content-between">
                  <div class="w-100">
                    <div v-if="!isEditingAnswer">
                      {{ article.answeredContent }}
                    </div>
                    <div v-else class="textarea-container">
                      <textarea
                        class="form-control textarea-input"
                        v-model="article.answeredContent"
                        rows="10"
                        :maxlength="MAX_CONTENT_LENGTH"
                      ></textarea>
                    </div>
                  </div>
                  <button class="btn ms-3 answer" @click="updateAnswer">
                    {{ isEditingAnswer ? '입력' : '수정' }}
                  </button>
                </div>
              </div>
            </div>
            <!-- 2. 유저 화면  -->
            <div v-else>
              <div class="d-flex mb-3 mt-3 align-items-start">
                <div class="w-100" v-if="article.isAnswered">
                  <div class="fw-bold mb-3">답변</div>
                  {{ article.answeredContent }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          class="d-flex mt-4 w-100 justify-content-between align-items-center"
        >
          <button class="btn back" @click="back">목록</button>
          <button class="btn delete" @click="remove" v-if="isAdmin">
            문의 삭제
          </button>
          <template
            v-if="
              !article.isAnswered && !isAdmin && auth.userId == article.userId
            "
            class="w-100 text-end"
          >
            <div class="ms-auto">
              <button class="btn edit" @click="update">수정</button>
              <button class="btn delete" @click="remove">삭제</button>
            </div>
          </template>
        </div>
      </div>
    </div>
    <div v-if="isLoading" class="callback-container">
      <div class="loading-spinner">
        <div class="spinner"></div>
        <p>이메일 전송 중...</p>
      </div>
    </div>
  </div>
  <BaseModal
    :visible="modalVisible"
    :message="modalMessage"
    :buttons="modalButtons"
  />
</template>
<style scoped>
.container {
  background-color: #fbfbfb;
}
.custom-box-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 60px;
  padding-bottom: 30px;
  margin: 0px 30px;
}
.custom-box {
  width: 920px;
  min-height: 565px;
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
.footer {
  margin-top: auto; /* 아래로 밀기 */
  padding-bottom: 20px; /* 박스 하단으로부터 20px 띄우기 효과 */
  gap: 8px;
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
.callback-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  color: white;
}
.loading-spinner {
  text-align: center;
}
.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3573ee;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}
.loading-spinner p {
  font-size: 1.2em;
  font-weight: bold;
  margin: 0;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
/* 또는 뷰포트 기반으로 유연하게 */
@media (max-width: 1024px) and (orientation: portrait) {
  .custom-box {
    min-height: 87vh; /* 화면 높이의 80% 이상 확보 */
  }
}
</style>
