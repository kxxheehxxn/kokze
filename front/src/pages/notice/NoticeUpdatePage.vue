<script setup>
import api from '@/api/noticeApi';
import { computed, ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';

const MAX_TITLE_LENGTH = 500;
const MAX_CONTENT_LENGTH = 1000;

const auth = userAuthStore();
const router = useRouter();
const route = useRoute();

const orgArticle = ref({});
const noticeId = route.params.no;
const article = reactive({});

const disableSubmit = computed(() => !article.title);

const back = () => {
  router.back();
};
const submit = async () => {
  if (!confirm('수정할까요?')) return;

  const updatedFields = {};
  updatedFields.noticeId = article.noticeId;
  updatedFields.title = article.title;
  updatedFields.content = article.content;
  await api.update(updatedFields);

  router.replace({
    name: 'noticeDetail',
    params: { no: article.noticeId },
    query: route.query,
  });
};
const load = async () => {
  if (!noticeId) {
    console.error('유효하지 않은 noticeId:', noticeId);
    alert('유효하지 않은 공지사항 ID입니다.');
    router.replace('/notice/list');
    return;
  }

  try {
    const data = await api.get(noticeId);

    if (!data || !data.noticeId) {
      throw new Error(
        'API 응답 데이터가 유효하지 않거나 게시글을 찾을 수 없습니다.'
      );
    }
    orgArticle.value = { ...data };
    article.noticeId = orgArticle.value.noticeId;
    article.userId = orgArticle.value.userId;
    article.title = orgArticle.value.title;
    article.content = orgArticle.value.content;
  } catch (e) {
    console.error('공지사항 로드 중 오류 발생:', e);
    alert('공지사항을 불러오는 데 실패했습니다.');
    router.replace('/notice/list');
  }
};

onMounted(() => {
  if (auth.role.toLowerCase() !== 'admin') {
    alert('권한이 없습니다.');
    router.replace('/');
    return;
  }
});
load();
</script>
<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">공지사항 수정</h4>
        <form @submit.prevent="submit">
          <div class="d-flex align-items-center title-box">
            <label for="title" class="form-label ms-2">제목</label>
            <div class="title-container w-100">
              <input
                type="text"
                class="form-control title-input"
                id="title"
                v-model="article.title"
                :maxlength="MAX_TITLE_LENGTH"
              />
              <span class="char-count"
                >{{ article.title.length }} / {{ MAX_TITLE_LENGTH }}</span
              >
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
                :maxlength="MAX_CONTENT_LENGTH"
              ></textarea>
              <span class="char-count textarea-count"
                >{{ article.content.length }} / {{ MAX_CONTENT_LENGTH }}</span
              >
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
            <button type="button" class="btn ms-3 fw-bold back" @click="back">
              취소
            </button>
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
  margin: 0px 30px;
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
  font-weight: bold;
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
  position: relative;
}
.title-input {
  flex: 1;
  height: 100%; /* 부모 높이에 꽉 채우기 */
  border: none;
  outline: none;
  font-size: 13px;
  border-radius: 20px;
  background: transparent;
  padding-right: 60px;
}
.form-label {
  margin: 0 15px 0 10px;
  width: 40px;
  font-weight: bold;
  flex-shrink: 0;
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
  position: relative;
}
.textarea-input {
  width: 100%;
  height: 250px;
  border: none;
  outline: none;
  resize: none;
  font-size: 13px;
  background: transparent;
  padding-bottom: 20px;
}
.char-count {
  position: absolute;
  right: 15px;
  font-size: 12px;
  color: #888;
}
.title-container .char-count {
  top: 50%;
  transform: translateY(-50%);
}
.textarea-container .char-count.textarea-count {
  bottom: 8px;
  right: 15px;
}
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
