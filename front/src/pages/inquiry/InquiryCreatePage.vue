<script setup>
import api from '@/api/inquiryApi';
import { computed, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';
const auth = userAuthStore();
const router = useRouter();
const route = useRoute();
const back = () => {
  router.push({ name: 'inquiryList', query: route.query });
};
const article = reactive({
  userId: '24a0f5c7-66d7-11f0-8ab4-8cb0e9d84583', // 실제 로그인된 사용자 ID 넣어야 함
  userName: '김콕재',
  title: '',
  content: '',
  isAnswered: false,
});
const disableSubmit = computed(() => !article.title);
const submit = async () => {
  if (!confirm('등록할까요?')) return;
  await api.create(article);
  router.push('/inquiry/list');
};
</script>
<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">문의사항 작성</h4>
        <form @submit.prevent="submit">
          <div class="d-flex align-items-center title-box">
            <label for="title" class="form-label ms-2">제목</label>
            <div class="title-container w-100">
              <input
                type="text"
                class="form-control title-input"
                id="title"
                v-model="article.title"
              />
            </div>
          </div>
          <hr />
          <div class="d-flex mb-3 mt-3 align-items-start">
            <label for="content" class="form-label pt-2">내용</label>
            <div class="textarea-container w-100">
              <textarea
                class="form-control textarea-input"
                id="content"
                v-model="article.content"
                rows="10"
              ></textarea>
            </div>
          </div>
          <div class="mt-5 text-center">
            <button
              type="submit"
              class="btn fw-bold create"
              :disabled="disableSubmit"
            >
              확인
            </button>
            <button class="btn ms-3 fw-bold back" @click="back">취소</button>
          </div>
        </form>
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
.btn {
  width: 120px;
  height: 41px;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.create {
  background-color: #3573ee;
  color: white;
}
.back {
  color: #666666;
}
#content {
  min-height: 200px;
}
.title-container {
  height: 37px;
  background: #fff;
  border-radius: 20px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 15px;
  box-sizing: border-box;
}
.title-input {
  flex: 1;
  height: 70%; /* 적당히 세로 크기 맞춤 */
  border: none;
  outline: none;
  font-size: 13px;
  border-radius: 20px;
}
.form-label {
  margin: 0 15px 0 10px;
  width: 40px;
  font-weight: bold;
}
.title-box {
  margin: 40px 0 23px 0;
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
  height: 250px;
  border: none;
  outline: none;
  resize: none;
  font-size: 13px;
  background: transparent;
}
</style>
