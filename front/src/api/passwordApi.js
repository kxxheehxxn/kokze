// 이메일 존재 여부 확인
export async function checkEmailExists(email) {
  // mock: test@example.com만 존재
  if (email === 'test@example.com') return true
  return false
}

// 인증번호 발송 (mock: 항상 123456)
export async function sendVerificationCode(email) {
  return '123456'
}

// 인증번호 확인 (mock: 123456만 성공)
export async function verifyCode(inputCode) {
  return inputCode === '123456'
}

// 비밀번호 변경 (mock: 항상 성공)
export async function changePassword(email, newPassword) {
  return true
} 