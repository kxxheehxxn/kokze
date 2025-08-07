<template>
  <div class="modal-overlay" v-if="isVisible" @click="closeModal">
    <div class="modal-container" @click.stop>
      <div class="modal-header">
        <div class="modal-title">
          <div class="small-title">오늘의</div>
          <div class="big-title">금융퀴즈</div>
        </div>
        <button class="close-btn" @click="closeModal">×</button>
      </div>
      <div class="modal-body">
        <div v-if="isLoading" class="loading-section">
          <p>퀴즈를 불러오는 중...</p>
        </div>
        <div v-else-if="alreadySolved" class="already-solved-section">
          <div class="solved-icon">✅</div>
          <div class="solved-title">오늘의 퀴즈 완료!</div>
          <div class="solved-message">
            오늘은 이미 퀴즈를 풀었습니다.<br />
            내일 다시 도전해보세요!
          </div>
        </div>
        <div v-else-if="errorMessage" class="error-section">
          <p>{{ errorMessage }}</p>
          <button @click="fetchQuiz" v-if="!currentQuiz">다시 시도</button>
        </div>
        <div v-else-if="currentQuiz" class="quiz-question-section">
          <div class="d-flex flex-column align-items-center text-center">
            <div class="question-text">Q. {{ currentQuiz.question }}</div>
          </div>
          <div class="quiz-type-indicator">
            <span class="type-badge" :class="currentQuiz.type">
              {{ currentQuiz.type === 'OX' ? 'O/X 퀴즈' : '단답형' }}
            </span>
          </div>
        </div>
        <div v-else class="no-quiz-section">
          <p>퀴즈를 찾을 수 없습니다.</p>
        </div>
        <!-- 답변 섹션 추가 -->
        <div v-if="currentQuiz" class="answer-section">
          <div v-if="currentQuiz.type === 'OX'" class="OX-quiz-section">
            <div class="answer-label">정답을 선택해주세요</div>
            <div class="OX-buttons">
              <button
                class="OX-button OX-button-O"
                :class="{ selected: selectedAnswer === 'O' }"
                @click="selectAnswer('O')"
              >
                <span class="OX-text">O</span>
                <span class="OX-label">맞다</span>
              </button>
              <button
                class="OX-button OX-button-X"
                :class="{ selected: selectedAnswer === 'X' }"
                @click="selectAnswer('X')"
              >
                <span class="OX-text">X</span>
                <span class="OX-label">틀리다</span>
              </button>
            </div>
          </div>
          <div
            v-else-if="currentQuiz.type === 'short'"
            class="short-answer-section"
          >
            <div class="answer-label">정답을 입력해주세요</div>
            <div class="input-container">
              <input
                type="text"
                v-model="selectedAnswer"
                class="answer-input"
                :placeholder="currentQuiz.placeholder || '답을 입력하세요'"
                @keyup.enter="submitAnswer"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer" v-if="currentQuiz">
        <button
          class="submit-btn"
          @click="submitAnswer"
          :disabled="
            isLoading ||
            !currentQuiz ||
            !selectedAnswer ||
            (typeof selectedAnswer === 'string' && selectedAnswer.trim() === '')
          "
          :class="{
            disabled:
              isLoading ||
              !currentQuiz ||
              !selectedAnswer ||
              (typeof selectedAnswer.value === 'string' &&
                selectedAnswer.trim() === ''),
          }"
        >
          정답 제출
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue';
import { userAuthStore } from '@/stores/auth';
import { quizApi } from '@/api/quizApi';
// Props 정의
const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
});
// Emits 정의
const emit = defineEmits(['close', 'quizSubmitted']);
// 반응형 데이터
const auth = userAuthStore();
const isVisible = ref(props.show);
const selectedAnswer = ref(null);
const currentQuiz = ref(null);
const isLoading = ref(false);
const errorMessage = ref('');
const alreadySolved = ref(false); // 추가
// 답 선택 함수
const selectAnswer = (answer) => {
  selectedAnswer.value = answer;
};
// 퀴즈 데이터 가져오는 함수
const fetchQuiz = async () => {
  isLoading.value = true;
  errorMessage.value = '';
  currentQuiz.value = null;
  selectedAnswer.value = null;
  alreadySolved.value = false; // 초기화
  if (!auth.userId) {
    errorMessage.value = '로그인이 필요합니다. (사용자 ID 없음)';
    isLoading.value = false;
    return;
  }
  try {
    const quizData = await quizApi.getTodayQuiz(auth.userId);
    currentQuiz.value = quizData;
  } catch (error) {
    // 이미 풀었다면 alreadySolved 상태로 표시
    if (error.message.includes('오늘은 이미 퀴즈를 풀었습니다')) {
      alreadySolved.value = true;
    } else {
      errorMessage.value = error.message;
    }
  } finally {
    isLoading.value = false;
  }
};
// props.show 변화 감지
watch(
  () => props.show,
  (newValue) => {
    isVisible.value = newValue;
    if (newValue) {
      fetchQuiz();
    } else {
      // 모달이 닫힐 때 상태 초기화
      selectedAnswer.value = null;
      currentQuiz.value = null;
      errorMessage.value = '';
      alreadySolved.value = false; // 추가
      isLoading.value = false;
    }
  },
  { immediate: true }
);
// 모달 닫기 함수
const closeModal = () => {
  isVisible.value = false;
  selectedAnswer.value = null;
  currentQuiz.value = null;
  errorMessage.value = '';
  alreadySolved.value = false; // 추가
  isLoading.value = false;
  emit('close');
};
// 정답 제출 함수
const submitAnswer = async () => {
  if (
    isLoading.value ||
    !currentQuiz.value ||
    !selectedAnswer.value ||
    (typeof selectedAnswer.value === 'string' &&
      selectedAnswer.value.trim() === '')
  ) {
    errorMessage.value = '답안을 입력해주세요!';
    return;
  }
  isLoading.value = true;
  errorMessage.value = '';
  if (!auth.userId) {
    errorMessage.value = '로그인이 필요합니다. (사용자 ID 없음)';
    isLoading.value = false;
    return;
  }
  try {
    const result = await quizApi.submitAnswer(
      auth.userId,
      currentQuiz.value.quiz_id,
      selectedAnswer.value
    );
    if (result.correct) {
      alert('정답입니다! 🎉');
    } else {
      alert('오답입니다. 😔');
    }
    emit('quizSubmitted', result.correct);
    closeModal();
  } catch (error) {
    console.error('퀴즈 제출 실패:', error);
    errorMessage.value = error.message;
    // 이미 풀었다는 메시지면 alreadySolved 상태로 변경
    if (error.message.includes('오늘은 이미 퀴즈를 풀었습니다')) {
      alreadySolved.value = true;
      currentQuiz.value = null; // 퀴즈 숨기기
    }
  } finally {
    isLoading.value = false;
  }
};
// ESC 키로 모달 닫기
const handleKeydown = (event) => {
  if (event.key === 'Escape') {
    closeModal();
  }
};
onMounted(() => {
  document.addEventListener('keydown', handleKeydown);
});
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown);
});
</script>
<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-container {
  background-color: #ffffff;
  color: #000;
  border-radius: 12px;
  width: 90%;
  max-width: 700px;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}
.modal-header {
  padding: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}
.modal-title {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.big-title {
  font-size: 28px;
  font-weight: bold;
  margin-top: 4px;
}
.small-title {
  font-size: 16px;
  font-weight: normal;
  opacity: 0.8;
}
.close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: none;
  border: none;
  font-size: 32px;
  cursor: pointer;
  color: #fd5757;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s;
  opacity: 0.7;
}
.close-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  opacity: 1;
}
.modal-body {
  padding: 0;
  max-height: 60vh;
  overflow-y: auto;
}
.answer-section {
  padding: 0 24px 24px 24px;
}
.answer-label {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  text-align: center;
}
.loading-section,
.error-section,
.no-quiz-section {
  padding: 48px 24px;
  text-align: center;
}
/* 퀴즈 문제 섹션 */
.quiz-question-section {
  padding: 24px;
}
.question-text {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 20px;
  font-weight: 500;
}
/* 퀴즈 타입 표시 */
.quiz-type-indicator {
  margin-top: 16px;
  text-align: center;
}
.type-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.type-badge.OX {
  background-color: rgba(135, 191, 255, 0.2);
  color: #0d3a95;
  border: 1px solid #0d3a95;
}
.type-badge.short {
  background-color: rgba(135, 191, 255, 0.2);
  color: #0d3a95;
  border: 1px solid #0d3a95;
}
/* O/X 퀴즈 스타일 */
.OX-quiz-section {
  text-align: center;
}
.OX-buttons {
  display: flex;
  gap: 24px;
  justify-content: center;
  margin-top: 20px;
}
.OX-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 24px;
  background-color: rgba(255, 255, 255, 0.05);
  border: 2px solid rgba(76, 76, 76, 0.2);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 100px;
  color: #989898;
}
.OX-button:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}
.OX-button.selected {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}
.OX-button-O.selected {
  background-color: rgba(34, 197, 94, 0.2);
  border-color: #22c55e;
  color: #22c55e;
}
.OX-button-X.selected {
  background-color: rgba(239, 68, 68, 0.2);
  border-color: #ef4444;
  color: #ef4444;
}
.OX-text {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 8px;
}
.OX-label {
  font-size: 14px;
  font-weight: 500;
  opacity: 0.8;
}
.OX-button.selected .OX-label {
  opacity: 1;
}
/* 단답형 퀴즈 스타일 */
.short-answer-section {
  text-align: center;
}
.input-container {
  margin-top: 20px;
}
.answer-input {
  width: 100%;
  max-width: 300px;
  padding: 16px 20px;
  border: 2px solid rgba(83, 83, 83, 0.2);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.05);
  font-size: 16px;
  text-align: center;
  transition: all 0.2s;
}
.answer-input:focus {
  outline: none;
  border-color: #0d3a95;
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
}
.answer-input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}
.modal-footer {
  padding: 14px;
  text-align: center;
}
.submit-btn {
  background: linear-gradient(135deg, #0d3a95, #0d3a95);
  color: #fff;
  border: none;
  padding: 14px 32px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  min-width: 120px;
}
.submit-btn:hover:not(.disabled) {
  background: linear-gradient(135deg, #0d3a95, #0d3a95);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);
}
.submit-btn.disabled {
  background: rgba(255, 255, 255, 0.2);
  color: rgba(255, 255, 255, 0.5);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}
/* 이미 풀었을 때 섹션 스타일 추가 */
.already-solved-section {
  padding: 48px 24px;
  text-align: center;
}
.solved-icon {
  font-size: 64px;
  margin-bottom: 30px;
}
.solved-title {
  font-size: 24px;
  font-weight: bold;
  color: #22c55e;
  margin-bottom: 16px;
}
.solved-message {
  font-size: 16px;
  line-height: 1.6;
  opacity: 0.8;
}
/* 반응형 디자인 */
@media (max-width: 600px) {
  .modal-container {
    width: 95%;
    margin: 10px;
  }
  .OX-buttons {
    gap: 16px;
  }
  .OX-button {
    min-width: 80px;
    padding: 16px 20px;
  }
  .OX-text {
    font-size: 28px;
  }
  .question-text {
    font-size: 16px;
  }
  .answer-input {
    max-width: 250px;
    padding: 14px 16px;
    font-size: 14px;
  }
}
</style>
