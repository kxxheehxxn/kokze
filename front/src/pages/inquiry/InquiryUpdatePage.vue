<script setup>
import api from '@/api/inquiryApi';
import { reactive, computed, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';

const auth = userAuthStore();

const router = useRouter();
const route = useRoute();

const MAX_TITLE_LENGTH = 500;
const MAX_CONTENT_LENGTH = 1000;

const infoId = route.params.no;

const orgArticle = ref({});

const article = reactive({
  infoId: infoId,
  userId: '',
  userName: '',
  title: '',
  content: '',
  isAnswered: false,
});

const disableSubmit = computed(() => {
  return !article.title || !article.content;
});

const submit = async () => {
  if (!confirm('문의사항을 수정하시겠습니까?')) {
    return;
  }

  const updatedFields = {
    infoId: article.infoId,
    userId: article.userId,
    title: article.title,
    content: article.content,
  };

  try {
    await api.update(updatedFields);

    router.replace({
      name: 'inquiryDetail',
      params: { no: article.infoId },
      query: route.query,
    });
  } catch (e) {
    console.error('문의사항 수정 실패:', e);
    alert('문의사항 수정에 실패했습니다. 다시 시도해주세요.');
  }
};

const load = async () => {
  if (!infoId) {
    console.error('유효하지 않은 infoId:', infoId);
    alert('유효하지 않은 게시글 ID입니다.');
    router.replace('/inquiry/list');
    return;
  }

  try {
    const data = await api.get(infoId);

    if (!data || !data.infoId) {
      throw new Error(
        'API 응답 데이터가 유효하지 않거나 게시글을 찾을 수 없습니다.'
      );
    }

    orgArticle.value = { ...data };
    article.infoId = orgArticle.value.infoId;
    article.userId = orgArticle.value.userId;
    article.userName = orgArticle.value.userName;
    article.title = orgArticle.value.title;
    article.content = orgArticle.value.content;
  } catch (e) {
    console.error('게시글 로드 중 오류 발생:', e);
    alert('게시글을 불러오는 데 실패했습니다.');
    router.replace('/inquiry/list');
  }
};

const back = () => {
  router.replace({
    name: 'inquiryDetail',
    params: { no: article.infoId || infoId },
    query: route.query,
  });
};

onMounted(() => {
  load();
});
</script>

<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">문의사항 수정</h4>
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
            <button type="button" class="btn ms-3 fw-bold back" @click="back">
              취소하기
            </button>
            <button
              type="submit"
              class="btn fw-bold create ms-3"
              :disabled="disableSubmit"
            >
              확인
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
  padding-top: 60px;
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
  height: 100%;
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
/* 또는 뷰포트 기반으로 유연하게 */
@media (max-width: 1024px) and (orientation: portrait) {
  .custom-box {
    min-height: 80vh; /* 화면 높이의 80% 이상 확보 */
  }
  .textarea-input {
    height: 50vh;
  }
}
</style>
