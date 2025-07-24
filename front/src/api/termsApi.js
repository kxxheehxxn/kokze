export async function fetchTerms() {
  // 실제 API 연동 전까지는 더미 데이터 사용
  return [
    { id: 1, category: '예금', title: '정기예금이란?', description: '정기예금은 일정 기간 동안 돈을 은행에 맡기고, 만기 시 원금과 이자를 함께 받는 금융상품입니다.' },
    { id: 2, category: '예금', title: '예금자보호란?', description: '예금자보호제도에 따라 1인당 5천만 원까지 보호됩니다.' },
    { id: 3, category: '적금', title: '적금이란?', description: '적금은 일정 기간 동안 일정 금액을 정기적으로 저축하는 금융상품입니다.' },
    { id: 4, category: '보험', title: '보험이란?', description: '보험은 위험에 대비해 일정 금액을 납입하고, 사고 발생 시 보장받는 금융상품입니다.' },
    { id: 5, category: '세금', title: '소득세란?', description: '소득에 부과되는 세금입니다.' }
  ]
} 