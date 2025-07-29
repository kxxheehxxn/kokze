<script setup>
import api from '@/api/inquiryApi';
import { reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth'; // 사용자 인증 정보를 가져오는 스토어

const auth = userAuthStore(); // 인증 스토어 인스턴스

const router = useRouter();

// 최대 길이 정의 (DB VARCHAR 길이에 맞춰 설정)
const MAX_TITLE_LENGTH = 500;
const MAX_CONTENT_LENGTH = 1000;

// 입력 필드 데이터 정의
const article = reactive({
  // 실제 로그인된 사용자 ID를 auth 스토어에서 가져옴
  userId: auth.userId || '24a0f5c7-66d7-11f0-8ab4-8cb0e9d84583', // auth.userId가 없을 경우 기본값 설정
  userName: auth.userName || '알 수 없음', // auth.userName이 없을 경우 기본값 설정
  title: '',
  content: '',
  isAnswered: false, // 작성 시에는 항상 false로 시작
});

// 제출 버튼 활성화/비활성화 조건 (유효성 검사 통과 시에만 활성화)
const disableSubmit = computed(() => {
  // 제목과 내용이 모두 채워져 있어야 함
  return !article.title || !article.content;
});

// 게시글 생성 함수 (submit)
const submit = async () => {
  if (!confirm('문의사항을 등록하시겠습니까?')) {
    return;
  }

  try {
    await api.create(article);
    alert('문의사항이 성공적으로 등록되었습니다.');
    router.push('/inquiry/list'); // 목록 페이지로 이동 - 히스토리 스택을 교체하여 뒤로가기 방지
  } catch (e) {
    console.error('문의사항 등록 실패:', e);
    alert('문의사항 등록에 실패했습니다. 다시 시도해주세요.');
  }
};

const back = () => {
  router.back(); // 목록 페이지로 이동
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
/* 기존 스타일 유지 */
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

/* ⭐ 유효성 검사 경고 메시지 스타일 ⭐ */
.alert-danger {
  color: #fd5757;
  background-color: #f2dede;
  border: 1px solid #ebccd1;
  border-radius: 4px;
  padding: 10px 15px;
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
}

/* 버튼 비활성화 시 스타일 */
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
