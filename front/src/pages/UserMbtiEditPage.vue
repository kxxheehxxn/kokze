<template>
  <UserCardLayout>
    <template v-if="step < questions.length">
      <h2 class="title">
        금융 MBTI 찾기 ({{ step + 1 }} / {{ questions.length }})
      </h2>
      <div class="question">{{ questions[step].question }}</div>
      <div class="choices">
        <div
          v-for="(choice, idx) in questions[step].choices"
          :key="idx"
          :class="['choice', { selected: selected === idx }]"
          @click="selected = idx"
        >
          <div>{{ choice.text }}</div>
        </div>
      </div>
      <div class="button-row">
        <button class="cancel-btn" @click="onCancel">취소하기</button>
        <button
          class="submit-btn"
          :disabled="selected === null"
          @click="onNext"
        >
          다음
        </button>
      </div>
    </template>
    <template v-else>
      <h2 class="title">{{ userName }} 님의 금융 MBTI는</h2>
      <div class="mbti-type">{{ mbtiResult }}</div>
      <div class="mbti-desc">{{ mbtiDesc }}</div>
      <div class="button-row">
        <button class="submit-btn" @click="onRetry">다시하기</button>
      </div>
    </template>
  </UserCardLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import UserCardLayout from '@/components/UserCardLayout.vue'

const userName = '김콕재' // 실제 사용자 이름 연동 시 수정

const questions = [
  // 빠름 VS 느림
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
      { text: '여러 번 비교하고 분석 후 결정한다', type: 'slow', score: 4 },
    ],
  },
  {
    question: '새 금융 서비스를 알게 되면?',
    choices: [
      { text: '일단 써보면서 배운다', type: 'fast', score: 4 },
      { text: '리뷰와 후기 충분히 보고 결정한다', type: 'slow', score: 4 },
    ],
  },
  // 하이리스크 VS 로우리스크
  {
    question: '투자할 때 나는?',
    choices: [
      { text: '수익이 크면 리스크도 감수한다', type: 'high', score: 4 },
      { text: '안정적이고 꾸준한 수익을 선호한다', type: 'low', score: 4 },
    ],
  },
  {
    question: '재테크 수단을 고를 때 나는?',
    choices: [
      { text: '새로운 코인, 스타트업 투자도 도전', type: 'high', score: 4 },
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
]

const step = ref(0)
const selected = ref(null)
const scores = ref({ fast: 0, slow: 0, high: 0, low: 0 })
const router = useRouter()

function onNext() {
  const choice = questions[step.value].choices[selected.value]
  if (choice.type === 'fast' || choice.type === 'slow') {
    scores.value[choice.type] += choice.score
  } else if (choice.type === 'high' || choice.type === 'low') {
    scores.value[choice.type] += choice.score
  }
  step.value++
  selected.value = null
}

function onCancel() {
  router.back()
}

function onRetry() {
  step.value = 0
  selected.value = null
  scores.value = { fast: 0, slow: 0, high: 0, low: 0 }
}

const mbtiResult = computed(() => {
  if (step.value < questions.length) return ''
  const isFast = scores.value.fast >= scores.value.slow
  const isHigh = scores.value.high >= scores.value.low
  if (isFast && isHigh) return '신속한 승부사'
  if (!isFast && isHigh) return '신중한 승부사'
  if (isFast && !isHigh) return '신속한 분석가'
  return '신중한 분석가'
})

const mbtiDesc = computed(() => {
  switch (mbtiResult.value) {
    case '신속한 승부사':
      return `빠른 결정과 과감한 투자로 기회를 잡는 타입!\n새로운 도전과 높은 수익을 추구하며, 리스크도 두려워하지 않습니다.`
    case '신중한 승부사':
      return `분석과 신중함을 바탕으로, 기회가 오면 과감하게 승부하는 타입!\n충분한 정보와 준비 후에 도전하는 스타일입니다.`
    case '신속한 분석가':
      return `빠른 판단으로도 리스크는 최소화하는, 실용적 투자자!\n효율과 실리를 중시하며, 안정적인 수익도 놓치지 않습니다.`
    case '신중한 분석가':
      return `꼼꼼한 분석과 안정성을 중시하는, 계획형 투자자!\n안정적인 자산 관리와 예측 가능한 결과를 선호합니다.`
    default:
      return ''
  }
})
</script>

<style scoped>
.title {
  background-color: #fff;
  font-size: 28px;
  font-weight: bold;
  margin: 0 auto 32px auto;
  text-align: center;
  width: 100%;
}
.question {
  background-color: #fff;
  font-size: 22px;
  font-weight: 500;
  text-align: center;
  margin-bottom: 32px;
}
.choices {
  background-color: #fff;
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 48px;
}
.choice > div {
  background-color: #fff;
}
.choice {
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 2px 8px 0 #e5e7eb;
  padding: 32px 36px;
  font-size: 18px;
  font-weight: 500;
  text-align: center;
  cursor: pointer;
  transition: box-shadow 0.2s, border 0.2s;
  border: 2px solid transparent;
  min-width: 260px;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.choice.selected {
  border: 2px solid #2573ee;
  box-shadow: 0 4px 16px 0 #bcdcff;
}
.button-row {
  background-color: #fff;
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 32px;
  width: 100%;
}
.cancel-btn,
.submit-btn {
  width: 180px;
  height: 48px;
  border-radius: 18px;
  font-size: 18px;
  font-weight: 500;
  border: none;
  cursor: pointer;
}
.cancel-btn {
  background: #fafbfc;
  color: #222;
  border: 1.5px solid #e5e7eb;
}
.submit-btn {
  background: #2573ee;
  color: #fff;
  border: none;
}
.mbti-type {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 18px;
}
.mbti-desc {
  font-size: 18px;
  color: #222;
  text-align: center;
  white-space: pre-line;
  margin-bottom: 32px;
}
</style>
