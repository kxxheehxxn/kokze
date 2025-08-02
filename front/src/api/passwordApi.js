import axios from './index.js'

// 사용자 정보 확인 (전화번호와 이메일이 일치하는지)
export async function verifyUserInfo(phoneNum, email) {
  try {
    const response = await axios.post('/api/auth/verify-user', {
      phoneNum,
      email
    })
    
    return response.data.success
  } catch (error) {
    console.error('사용자 정보 확인 실패:', error)
    return false
  }
}

// 인증번호 발송
export async function sendVerificationCode(email) {
  try {
    const response = await axios.post('/api/auth/send-verification-code', {
      email
    })
    
    return response.data.success
  } catch (error) {
    console.error('인증번호 발송 실패:', error)
    throw error
  }
}

// 회원가입용 인증번호 발송
export async function sendSignupVerificationCode(email) {
  try {
    const response = await axios.post('/api/auth/signup/send-verification-code', {
      email
    })
    
    return response.data.success
  } catch (error) {
    console.error('회원가입 인증번호 발송 실패:', error)
    throw error
  }
}

// 인증번호 확인
export async function verifyCode(inputCode, email) {
  try {
    const response = await axios.post('/api/auth/verify-code', {
      code: inputCode,
      email
    })
    
    return response.data.success
  } catch (error) {
    console.error('인증번호 확인 실패:', error)
    return false
  }
}

// 회원가입용 인증번호 확인
export async function verifySignupCode(inputCode, email) {
  try {
    const response = await axios.post('/api/auth/signup/verify-code', {
      code: inputCode,
      email
    })
    
    return response.data.success
  } catch (error) {
    console.error('회원가입 인증번호 확인 실패:', error)
    return false
  }
}

// 비밀번호 변경
export async function changePassword(email, newPassword) {
  try {
    const response = await axios.post('/api/auth/change-password', {
      email,
      newPassword
    })
    
    return response.data.success
  } catch (error) {
    console.error('비밀번호 변경 실패:', error)
    throw error
  }
} 