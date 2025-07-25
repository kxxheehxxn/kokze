<script setup>
import { useRoute, useRouter } from 'vue-router';
import api from '@/api/inquiryApi';
import { ref } from 'vue';
import moment from 'moment';
import { userAuthStore } from '@/stores/auth';
const auth = userAuthStore();
const route = useRoute();
const router = useRouter();
const infoId = route.params.no;
const article = ref({});
const isEditingAnswer = ref(false);
const back = () => {
  router.push({ name: 'inquiryList', query: route.query });
};
const updateAnswer = async () => {
  if (!isEditingAnswer.value) {
    isEditingAnswer.value = true;
    return;
  }

  if (!article.value.answeredContent || !article.value.answeredContent.trim()) {
    alert('답변 내용을 입력해주세요.');
    return;
  }

  if (!confirm('답변을 수정할까요?')) return;

  const updatedFields = {
    infoId: article.value.infoId,
    userId: article.value.userId,
    answeredContent: article.value.answeredContent,
  };

  await api.updateAnswer(updatedFields);
  isEditingAnswer.value = false;

  // 새로고침 또는 데이터 재요청
  await load();
};
const update = () => {
  router.push({
    name: 'inquiryUpdate',
    params: { no: infoId },
    query: route.query,
  });
};
const submit = async () => {
  if (!confirm('답변을 등록할까요?')) return;

  const updatedFields = {
    infoId: article.value.infoId,
    userId: article.value.userId,
    answeredContent: article.value.answeredContent,
  };

  console.log(updatedFields); // undefined 방지

  await api.updateAnswer(updatedFields);

  // 페이지 이동 후 새로고침
  router
    .push({
      name: 'inquiryDetail',
      params: { no: article.value.infoId },
      query: route.query,
    })
    .then(() => {
      window.location.reload();
    });
};
const remove = async () => {
  if (!confirm('삭제할까요?')) return;
  await api.delete(infoId);
  router.push({ name: 'inquiryList', query: route.query });
};
const load = async () => {
  article.value = {
    infoId: 111,
    userId: 'user001',
    userName: '김콕재',
    title: '임시 제목입니다.',
    content: '이건 임시 내용입니다. 프론트 확인용입니다.',
    createdAt: '2025-06-14T12:00:00',
    isAnswered: true,
    answeredContent: '이건 임시 답변 내용입니다.',
  };

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
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">문의사항</h4>
        <div class="ms-1">
          <h5 class="fw-bold my-4">
            <span v-if="article.isAnswered">[답변완료] </span
            >{{ article.title }}
          </h5>
          <div class="inquiry-info">
            <span>{{ maskName(article.userName) }}</span>
            <span v-if="article.createdAt" class="ms-5">
              {{ moment(article.createdAt).format('YYYY-MM-DD HH:mm') }}
            </span>
          </div>
          <div class="mt-3">
            <hr />
            <div class="content">{{ article.content }}</div>
          </div>
          <!-- 답변 페이지 -->
          <!--  v-if 로 auth.role = 'admin'-->
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
                  :disabled="!article.answeredContent"
                >
                  입력
                </button>
              </div>
            </form>
          </div>
          <div v-else>
            <div class="fw-bold mb-3">답변</div>
            <div class="d-flex mb-3 mt-3 align-items-start">
              <div class="w-100">
                <div v-if="!isEditingAnswer">{{ article.answeredContent }}</div>
                <div v-else class="textarea-container">
                  <textarea
                    class="form-control textarea-input"
                    v-model="article.answeredContent"
                    rows="10"
                  ></textarea>
                </div>
              </div>
              <button class="btn ms-3 answer" @click="updateAnswer">
                {{ isEditingAnswer ? '입력' : '수정' }}
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- 
      isAnswered = false && role='user' => 목록, 수정, 삭제 버튼
      isAnswered = true && role='user' => 답변 화면, 목록 버튼

      isAnswered = false && role='admin' => 답변 폼, 입력 버튼
      isAnswered = true && role='admin' => 답변 화면, 수정 버튼
 -->
      <div class="d-flex mt-4 w-100 justify-content-between align-items-center">
        <button class="btn back" @click="back">목록</button>
        <!-- <template v-if auth.roles = "ADMIN" >-->
        <button class="btn delete" @click="remove">문의 삭제</button>
        <!-- !isAnswered && 현재 auth.roles = 'USER' 일 때 나타나기 -->
        <template v-if="!article.isAnswered" class="w-100 text-end"
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
