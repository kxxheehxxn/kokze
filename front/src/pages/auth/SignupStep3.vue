<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';
import axios from 'axios';
const router = useRouter();
const step = ref(0);
const selectedList = ref([]);
const scores = ref({ fast: 0, slow: 0, high: 0, low: 0 });
const authStore = userAuthStore();
const questions = [
  {
    question: '월급을 받았을 때 나는?',
    choices: [
      { text: '사고 싶었던 걸 바로 지른다', type: 'fast', score: 4 },
      { text: '지출계획을 세우고 천천히 쓴다', type: 'slow', score: 4 },
    ],
  },
  {
    question: '앱에서 투자 상품을 볼 때 나는?',
    choices: [
      { text: '직관적으로 빠르게 결정한다', type: 'fast', score: 4 },
      {
        text: '여러 번 비교하고 분석 후 결정한다',
        type: 'slow',
        score: 4,
      },
    ],
  },
  {
    question: '새 금융 서비스를 알게 되면?',
    choices: [
      { text: '일단 써보면서 배운다', type: 'fast', score: 4 },
      {
        text: '리뷰와 후기 충분히 보고 결정한다',
        type: 'slow',
        score: 4,
      },
    ],
  },
  {
    question: '투자할 때 나는?',
    choices: [
      { text: '수익이 크면 리스크도 감수한다', type: 'high', score: 4 },
      {
        text: '안정적이고 꾸준한 수익을 선호한다',
        type: 'low',
        score: 4,
      },
    ],
  },
  {
    question: '재테크 수단을 고를 때 나는?',
    choices: [
      {
        text: '새로운 코인, 스타트업 투자도 도전',
        type: 'high',
        score: 4,
      },
      { text: '예금, 적금 등 확실한 수단을 선택', type: 'low', score: 4 },
    ],
  },
  {
    question: '손실 가능성이 있는 상황에서 나는?',
    choices: [
      { text: '손해 나더라도 기회라면 베팅', type: 'high', score: 4 },
      { text: '손해는 피하고 확실한 걸 고른다', type: 'low', score: 4 },
    ],
  },
];
const mbtiResult = computed(() => {
  if (step.value < questions.length) return '';
  const isFast = scores.value.fast >= scores.value.slow;
  const isHigh = scores.value.high >= scores.value.low;
  if (isFast && isHigh) return '신속한 승부사';
  if (!isFast && isHigh) return '신중한 승부사';
  if (isFast && !isHigh) return '신속한 분석가';
  return '신중한 분석가';
});
const mbtiDesc = computed(() => {
  switch (mbtiResult.value) {
    case '신속한 승부사':
      return `빠른 결정과 과감한 투자로 기회를 잡는 타입!\n새로운 도전과 높은 수익을 추구하며, 리스크도 두려워하지 않습니다.`;
    case '신중한 승부사':
      return `분석과 신중함을 바탕으로, 기회가 오면 과감하게 승부하는 타입!\n충분한 정보와 준비 후에 도전하는 스타일입니다.`;
    case '신속한 분석가':
      return `빠른 판단으로도 리스크는 최소화하는, 실용적 투자자!\n효율과 실리를 중시하며, 안정적인 수익도 놓치지 않습니다.`;
    case '신중한 분석가':
      return `꼼꼼한 분석과 안정성을 중시하는, 계획형 투자자!\n안정적인 자산 관리와 예측 가능한 결과를 선호합니다.`;
    default:
      return '';
  }
});
function onNext() {
  const selected = selectedList.value[step.value];
  const choice = questions[step.value].choices[selected];
  scores.value[choice.type] += choice.score;
  step.value++;
}
function onPrev() {
  if (step.value === 0) {
    router.push('/signup/step2');
  } else {
    step.value--;
    const selected = selectedList.value[step.value - 1];
    const choice = questions[step.value - 1].choices[selected];
    scores.value[choice.type] -= choice.score;
  }
}
const onSubmit = async () => {
  try {
    authStore.setUserInfo('mbti', mbtiResult.value);
    authStore.setUserInfo('kakao', authStore.isKakao);
    const requiredFields = [
      'name',
      'email',
      'phoneNum',
      'birthDate',
      'sex',
      'salary',
      'payAmount',
      'mbti',
    ];
    const missing = requiredFields.filter((key) => !authStore.userInfo[key]);
    if (!authStore.isKakao && !(authStore.userInfo.password.length > 0)) {
      missing.push('password');
    }
    if (missing.length > 0) {
      alert(`다음 항목이 누락되었습니다: ${missing.join(', ')}`);
      return;
    }
    const apiUrl = authStore.isKakao
      ? 'http://localhost:8080/api/auth/signup/kakao'
      : 'http://localhost:8080/api/auth/signup';
    const response = await axios.post(apiUrl, authStore.userInfo);
    if (response.status === 200) {
      alert('회원가입이 완료되었습니다!');
      authStore.resetUserInfo();
      router.push('/auth/login');
    } else {
      alert('회원가입 실패: 서버 응답 코드 ' + response.status);
    }
  } catch (error) {
    console.error('회원가입 중 오류:', error);
    alert('회원가입 중 오류가 발생했습니다.');
  }
};
</script>
<template>
  <div class="container">
    <div class="no-nav-header">
      <router-link to="/" class="logo-section text-decoration-none">
        <div class="logo d-flex align-items-center">
          <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
        </div>
      </router-link>
    </div>
    <div class="signup-box">
      <div class="sign-top">
        <div class="title">
          콕재 서비스를 이용하려면<br />회원 가입이 필요해요
        </div>
        <div class="page-num">3/3</div>
      </div>
      <hr />
      <template v-if="step < questions.length">
        <div class="title">
          금융 MBTI 찾기 ({{ step + 1 }} / {{ questions.length }})
        </div>
        <div class="question">{{ questions[step].question }}</div>
        <div class="choices">
          <div
            v-for="(choice, idx) in questions[step].choices"
            :key="idx"
            :class="['choice', { selected: selectedList[step] === idx }]"
            @click="selectedList[step] = idx"
          >
            {{ choice.text }}
          </div>
        </div>
        <div class="button-group">
          <button class="cancel-button" @click="onPrev">
            {{ step === 0 ? '뒤로 가기' : '이전 질문' }}
          </button>
          <button
            class="next-button"
            :disabled="selectedList[step] === undefined"
            @click="onNext"
          >
            다음
          </button>
        </div>
      </template>
      <template v-else>
        <div class="mbti-type">{{ mbtiResult }}</div>
        <div class="mbti-desc">{{ mbtiDesc }}</div>
        <div class="button-group">
          <button class="next-button" @click="onSubmit">가입하기</button>
        </div>
      </template>
    </div>
  </div>
</template>
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 0 16px 40px 16px;
  position: relative;
}
.signup-box {
  background-color: #fff;
  width: 100%;
  max-width: 900px;
  padding: 90px 140px;
  border-radius: 30px;
  box-shadow: 0 0 20px #85858540;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.title {
  font-size: 24px;
  font-weight: 600;
  text-align: left;
  flex-shrink: 0;
}
.page-num {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #777;
  white-space: nowrap;
  flex-shrink: 0;
}
.page-num::after {
  content: '';
  flex-grow: 1;
  height: 1px;
  display: inline-block;
}
.question {
  font-size: 20px;
  font-weight: 500;
  text-align: center;
  margin-bottom: 24px;
}
.choices {
  display: flex;
  justify-content: center;
  gap: 32px;
  flex-wrap: nowrap;
}
.choice {
  width: 300px;
  height: 140px;
  padding: 20px;
  font-size: 17px;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  border: 2px solid transparent;
  transition: 0.2s;
}
.choice.selected {
  border-color: #2573ee;
  box-shadow: 0 4px 16px 0 #bcdcff;
}
.button-group {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 32px;
}
.cancel-button,
.next-button {
  width: 180px;
  height: 48px;
  border-radius: 18px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}
.cancel-button {
  background: #f2f2f2;
  color: #222;
  border: none;
}
.next-button {
  background: #2573ee;
  color: white;
  border: none;
}
.next-button:disabled {
  background: #a5c2ff;
  cursor: not-allowed;
}
.mbti-type {
  font-size: 26px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 16px;
}
.mbti-desc {
  font-size: 16px;
  color: #333;
  text-align: center;
  white-space: pre-line;
  margin-bottom: 24px;
}
</style>
