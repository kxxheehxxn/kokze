<template>
  <UserCardLayout>
    <h2 class="title">회원탈퇴 유의사항</h2>
    <div class="desc">
      <p>회원탈퇴를 신청하기 전에 안내 사항을 꼭 확인해 주세요.</p>
      <ul>
        <li>사용하고 계신 이메일은 탈퇴한 경우 재사용 및 복구가 불가능합니다.</li>
        <li>탈퇴한 아이디는 본인과 타인 모두 재사용 및 복구가 불가하오니 신중하게 선택하시기 바랍니다.</li>
        <li>부정 가입 또는 부정 이용이 의심되는 아이디는 탈퇴 후 6개월간 동일한 실명정보로 재가입 할 수 없습니다.</li>
        <li>탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다.</li>
        <li>회원정보 등 개인형 서비스 이용기록은 모두 삭제되며, 삭제된 데이터는 복구되지 않습니다.</li>
        <li>삭제되는 내용을 확인하시고 필요한 데이터는 미리 백업을 해주세요.</li>
      </ul>
      <p class="warn">탈퇴 후에는 아이디와 데이터는 복구할 수 없습니다.</p>
      <table>
        <thead>
          <tr>
            <th>항목</th>
            <th>처리 방식</th>
            <th>보관 기간</th>
            <th>보관 사유</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>회원 계정 정보</td>
            <td>즉시 삭제</td>
            <td>-</td>
            <td>-</td>
          </tr>
          <tr>
            <td>서비스 이용 기록</td>
            <td>내부 백업 보존</td>
            <td>6개월</td>
            <td>정보통신망법 제29조</td>
          </tr>
          <tr>
            <td>고객 문의/민원 기록</td>
            <td>내부 백업 보존</td>
            <td>3년</td>
            <td>전자상거래법 제6조</td>
          </tr>
          <tr>
            <td>로그인/접속 기록</td>
            <td>내부 백업 보존</td>
            <td>6개월</td>
            <td>정보통신망법 제15조</td>
          </tr>
        </tbody>
      </table>
      <p class="info">※ 0zea는 결제 시스템을 사용하지 않기 때문에, 결제 및 환불 관련 기록은 별도로 보관하지 않습니다.</p>
      <ul>
        <li>SNS 계정(카카오, 네이버 등)으로 가입한 회원도 동일하게 탈퇴할 수 있으며, 연동된 SNS 정보도 함께 삭제됩니다.</li>
        <li>서비스 이용 중 생성된 게시글, 댓글 등은 탈퇴 시 함께 삭제됩니다.</li>
        <li>일부 데이터는 관련 법령에 따라 일정 기간 보관 후 안전하게 삭제됩니다.</li>
        <li>탈퇴 관련 문의는 고객센터 이메일 또는 문의하기 페이지를 통해 가능합니다.</li>
      </ul>
    </div>
    <div class="agree-row">
      <input type="checkbox" id="agree" v-model="agree" />
      <label for="agree">안내 사항을 모두 확인하였으며, 이에 동의합니다.</label>
    </div>
    <div class="button-row">
      <button class="submit-btn" :disabled="!agree" @click="onWithdraw">확인</button>
    </div>
  </UserCardLayout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import UserCardLayout from '@/components/UserCardLayout.vue'
import { userAuthStore } from '@/stores/auth.js'

const agree = ref(false)
const router = useRouter()
const auth = userAuthStore()
function onWithdraw() {
  if (!agree.value) return
  // 실제 탈퇴 처리 로직
  auth.logout()
  alert('탈퇴가 처리되었습니다.')
  router.push('/')
}
</script>

<style scoped>
.title {
  font-size: 28px;
  font-weight: bold;
  margin: 0 auto 32px auto;
  text-align: left;
  width: 100%;
}
.desc {
  font-size: 17px;
  color: #222;
  margin-bottom: 24px;
}
.desc ul {
  margin: 12px 0 12px 0;
  padding-left: 20px;
}
.warn {
  color: #e74c3c;
  font-size: 16px;
  margin: 18px 0 12px 0;
  font-weight: 500;
}
.info {
  color: #888;
  font-size: 15px;
  margin: 12px 0 12px 0;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin: 18px 0 18px 0;
  font-size: 15px;
}
th, td {
  border: 1px solid #e5e7eb;
  padding: 10px 12px;
  text-align: left;
}
th {
  background: #f8f8f8;
  font-weight: 600;
}
.agree-row {
  margin: 32px 0 18px 0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}
.button-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
.submit-btn {
  width: 120px;
  height: 44px;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 500;
  border: none;
  background: #e74c3c;
  color: #fff;
  cursor: pointer;
  transition: background 0.2s;
}
.submit-btn:disabled {
  background: #f3bcbc;
  color: #fff;
  cursor: not-allowed;
}
</style> 