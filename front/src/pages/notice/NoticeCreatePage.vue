<script setup>
import api from '@/api/noticeApi';
import { computed, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';
const auth = userAuthStore();
const router = useRouter();
const MAX_TITLE_LENGTH = 500;
const MAX_CONTENT_LENGTH = 1000;
const back = () => {
  console.log('취소 버튼 클릭됨');
  router.back();
};
const article = reactive({
  adminId: auth.userId || '3e7db2f4-66d7-11f0-8ab4-8cb0e9d84583', // 임시 운영자 adminId
  title: '',
  content: '',
});
const disableSubmit = computed(() => !article.title);
const submit = async () => {
  if (!confirm('등록할까요?')) return;
  await api.create(article);
  router.push('/notice/list');
};
// 👉 권한 확인
onMounted(() => {
  if (auth.role !== 'ADMIN') {
    alert('권한이 없습니다.');
    router.replace('/'); // 또는 router.push('/') 등 원하는 경로로
    return;
  }
});
</script>
<template>
  <div class="custom-box-wrapper">
    <div class="custom-box p-5">
      <div class="m-2">
        <h4 class="fw-bold">공지사항 작성</h4>
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
  min-height: 200px; /* textarea-input의 height가 덮어씌움 */
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
  position: relative; /* 글자 수 표시를 위해 추가 */
}
.title-input {
  flex: 1;
  height: 100%; /* 부모 높이에 꽉 채우기 */
  border: none;
  outline: none;
  font-size: 13px;
  border-radius: 20px;
  background: transparent; /* 부모 배경색 보이도록 투명 설정 */
  padding-right: 60px; /* 글자 수 표시 공간 확보 */
}
.form-label {
  margin: 0 15px 0 10px;
  width: 40px;
  font-weight: bold;
  flex-shrink: 0; /* 레이블이 줄어들지 않도록 함 */
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
  position: relative; /* 글자 수 표시를 위해 추가 */
}
.textarea-input {
  width: 100%;
  height: 250px; /* 고정 높이 */
  border: none;
  outline: none;
  resize: none;
  font-size: 13px;
  background: transparent;
  padding-bottom: 20px; /* 글자 수 표시 공간 확보 */
}

/* ⭐ 새로 추가된 글자 수 표시 스타일 ⭐ */
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
.textarea-container .textarea-count {
  bottom: 8px; /* 아래쪽으로 이동 */
  right: 15px;
}
/* 버튼 비활성화 시 스타일 */
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
