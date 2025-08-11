<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'

const props = defineProps({
  product: {
    type: Object,
    required: true,
  },
})

// 표시용 상태
const summaryText = ref('')
const loading = ref(false)
const errorMsg = ref('')

// 로컬 캐시 키 (요약 재사용)
function cacheKey(p) {
  // id가 없으면 이름+은행+기간 등으로 키를 만들어도 됨
  return `ai_summary_${p?.id || p?.productId || p?.name || 'unknown'}`
}

function readToken() {
  try {
    const raw = localStorage.getItem('auth')
    if (!raw) return null
    const parsed = JSON.parse(raw)
    return parsed?.token || null
  } catch {
    return null
  }
}

// 실제 요약 호출
async function fetchSummary() {
  const p = props.product
  if (!p) return

  errorMsg.value = ''
  loading.value = true

  // 1) 서버에서 이미 내려준 요약이 있으면 그대로 사용
  const pre = (p.summary || '').trim()
  if (pre) {
    summaryText.value = pre
    loading.value = false
    return
  }

  // 2) 로컬 캐시에 있으면 사용 (1일 캐시 예시)
  const key = cacheKey(p)
  const cached = localStorage.getItem(key)
  if (cached) {
    try {
      const { text, ts } = JSON.parse(cached)
      const oneDay = 24 * 60 * 60 * 1000
      if (Date.now() - (ts || 0) < oneDay && text) {
        summaryText.value = text
        loading.value = false
        return
      }
    } catch {}
  }

  // 3) 없으면 백엔드 호출
  try {
    // 요약 입력에 쓸 텍스트(설명/약관/혜택 등 우선순위로 골라서 전송)
    const sourceText =
      p.description?.trim() || p.detail?.trim() || p.terms?.trim() || ''

    if (!sourceText) {
      summaryText.value = '이 상품에 대한 요약 정보가 존재하지 않습니다.'
      loading.value = false
      return
    }

    const token = readToken()
    const res = await axios.post(
      '/api/ai/summarize',
      { text: sourceText, maxLines: 3 }, // 백엔드가 받는 파라미터 구조에 맞춰 주세요
      {
        headers: {
          'Content-Type': 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
      },
    )

    // 응답 스키마에 맞춰 파싱 (둘 중 하나로 내려오는 경우 대응)
    const data = res.data || {}
    const threeLines =
      data.data /* { success, data } 포맷 */ ||
      data.summary /* { summary: "..." } 포맷 */ ||
      ''

    summaryText.value =
      (threeLines && threeLines.trim()) ||
      '이 상품에 대한 요약 정보가 존재하지 않습니다.'

    // 4) 로컬 캐시 저장
    localStorage.setItem(
      key,
      JSON.stringify({ text: summaryText.value, ts: Date.now() }),
    )
  } catch (err) {
    console.error('AI 요약 실패', err)
    errorMsg.value = '요약 생성 중 오류가 발생했습니다.'
    summaryText.value = '이 상품에 대한 요약 정보가 존재하지 않습니다.'
  } finally {
    loading.value = false
  }
}

// product가 바뀔 때마다 즉시 실행
watch(
  () => props.product,
  () => fetchSummary(),
  { immediate: true },
)
onMounted(fetchSummary)
</script>

<template>
  <div class="summary-note-box">
    <div class="header">
      <div class="title-row">
        <span>✨</span>
        <span class="title">상품 3줄 요약</span>
        <span>✨</span>
      </div>
      <div class="desc">
        해당 요약은 AI가 자동 생성한 설명입니다. 실제 상품 약관과는 다를 수
        있습니다.
      </div>
    </div>

    <hr />

    <div class="content">
      <p v-if="loading">요약 생성 중…</p>
      <p v-else>{{ summaryText }}</p>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<style scoped>
.summary-note-box {
  background: white;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1.5rem;
  font-size: 1rem;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 1rem;
}
.title-row {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
  font-weight: bold;
  font-size: 1.5rem;
}
.desc {
  font-size: 0.9rem;
  color: #666;
  white-space: nowrap;
}
.content p {
  white-space: pre-wrap;
  line-height: 1.6;
  font-size: 1rem;
  color: #222;
}
.error {
  margin-top: 0.5rem;
  color: #c00;
  font-size: 0.9rem;
}
</style>
