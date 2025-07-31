<script setup>
import { reactive, ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { userAuthStore } from '@/stores/auth';
import PrivacyPolicyModal from '@/components/PrivacyPolicyModal.vue';

const router = useRouter();
const authStore = userAuthStore();

// // 실제 사용
// const userInfo = reactive({
//     gender: '',
//     birth: '',
//     phone1: '',
//     phone2: '',
//     phone3: '',
//     agreed: false,
//     agreeSub0: false,
//     agreeSub1: false,
//     agreeSub2: false,
// });

// 테스트 용
const userInfo = reactive({
    gender: 'female',
    birth: '1992-07-15',
    phone1: '010',
    phone2: '9876',
    phone3: '4321',
    agreed: true,
    agreeSub0: true,
    agreeSub1: true,
    agreeSub2: true,
});

const isPolicyModalOpen = ref(false);

// agreed 변경 시 동기화
watch(
    () => userInfo.agreed,
    (val) => {
        userInfo.agreeSub0 = val;
        userInfo.agreeSub1 = val;
        userInfo.agreeSub2 = val;
    }
);

// agreeSub0 변경 시 동기화
watch(
    () => userInfo.agreeSub0,
    (val) => {
        userInfo.agreed = val;
        userInfo.agreeSub1 = val;
        userInfo.agreeSub2 = val;
    }
);

// 동의 버튼
const openModal = () => {
    isPolicyModalOpen.value = true;
};

// 모달에서 가져온 값 적용
const handlePolicyAgree = (agreeData) => {
    userInfo.agreed = agreeData.agreed;
    userInfo.agreeSub0 = agreeData.agreeSub0;
    userInfo.agreeSub1 = agreeData.agreeSub1;
    userInfo.agreeSub2 = agreeData.agreeSub2;
    isPolicyModalOpen.value = false;
};

//
const isFormValid = computed(() => {
    return (
        userInfo.gender &&
        userInfo.birth &&
        userInfo.phone1 &&
        userInfo.phone2 &&
        userInfo.phone3 &&
        userInfo.agreed
    );
});

// 다음 페이지
const goNext = () => {
    if (!isFormValid.value) return;

    // userInfo를 Pinia 스토어에 저장
    authStore.setUserInfo('sex', userInfo.gender);
    authStore.setUserInfo('birthDate', userInfo.birth);
    authStore.setUserInfo(
        'phoneNum',
        `${userInfo.phone1}-${userInfo.phone2}-${userInfo.phone3}`
    );

    // 다음 단계로 이동
    router.push('/signup/step2');
};
</script>

<template>
    <div class="container">
        <router-link to="/" class="logo-section text-decoration-none">
            <div class="logo d-flex align-items-center">
                <img src="@/assets/logo.svg" alt="로고" class="logo-icon" />
            </div>
        </router-link>

        <div class="signup-box">
            <div class="top">
                <div class="title">
                    콕재 서비스를 이용하려면<br />회원 가입이 필요해요
                </div>
                <div class="page-num">1/3</div>
            </div>

            <hr />

            <div class="title">개인정보 입력</div>

            <div class="form-group">
                <label>성별</label>
                <div class="gender-group">
                    <label>
                        <input
                            type="radio"
                            value="male"
                            v-model="userInfo.gender"
                        />
                        남성</label
                    >
                    <label>
                        <input
                            type="radio"
                            value="female"
                            v-model="userInfo.gender"
                        />
                        여성</label
                    >
                </div>
            </div>

            <div class="form-group">
                <label>생년월일</label>
                <input type="date" v-model="userInfo.birth" />
            </div>

            <div class="form-group">
                <label>전화번호</label>
                <div class="phone-group">
                    <select v-model="userInfo.phone1">
                        <option value="">선택</option>
                        <option value="010">010</option>
                        <option value="011">011</option>
                    </select>
                    <input v-model="userInfo.phone2" maxlength="4" />
                    <input v-model="userInfo.phone3" maxlength="4" />
                </div>
            </div>

            <hr />

            <div class="agreement">
                <div class="agreement-con1">
                    <label>
                        <input type="checkbox" v-model="userInfo.agreed" />
                        <strong>[필수] 개인(신용)정보 처리 동의</strong>
                    </label>
                </div>

                <div class="agreement-con2">
                    <div class="agreement-left">
                        <div class="agreement-item">
                            <label>
                                <input
                                    type="checkbox"
                                    v-model="userInfo.agreeSub0"
                                />
                                개인(신용)정보 수집·이용 동의
                            </label>
                        </div>
                        <div class="agreement-sub-items">
                            <label>
                                <input
                                    type="checkbox"
                                    v-model="userInfo.agreeSub1"
                                    disabled
                                />
                                고유식별정보
                            </label>
                            <label>
                                <input
                                    type="checkbox"
                                    v-model="userInfo.agreeSub2"
                                    disabled
                                />
                                개인(신용)정보
                            </label>
                        </div>
                    </div>
                    <div class="agreement-right">
                        <button @click="openModal">＞</button>
                    </div>
                </div>
            </div>

            <div class="button-group">
                <button class="cancel-button" @click="router.push('/')">
                    취소하기
                </button>
                <button
                    :disabled="!isFormValid"
                    class="next-button"
                    @click="goNext"
                >
                    다음 단계
                </button>
            </div>
        </div>
    </div>
    <PrivacyPolicyModal
        v-if="isPolicyModalOpen"
        @close="isPolicyModalOpen = false"
        @agree="handlePolicyAgree"
    />
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

/* logo */
.logo-section {
    cursor: pointer;
    margin: 15px 0 0 20px;
    align-self: flex-start;
    transition: transform 0.2s ease;
}

.logo-section:hover {
    transform: scale(1.05);
}

.logo-icon {
    width: 54px;
    height: 54px;
    border-radius: 50%;
    padding: 2px;
    object-fit: contain;
}

/* signup-box */
.signup-box {
    background-color: #fff;
    width: 100%;
    max-width: 900px;
    padding: 90px 140px;
    border-radius: 30px;
    box-shadow: 0 0 20px #85858540;
    margin-top: 50px;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

/* title */
.top {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    gap: 12px;
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

/* 공통 입력 그룹 */
.form-group {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.form-group label {
    font-size: 15px;
    font-weight: 600;
    color: #222;
}

.form-group input,
.form-group select {
    border: 1px solid #ccc;
    border-radius: 30px;
    padding: 12px 14px;
    font-size: 16px;
    width: 100%;
    font-family: inherit;
}

/* 성별 선택 */
.gender-group {
    display: flex;
    gap: 40px;
}

.gender-group label {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 16px;
    font-weight: 500;
    white-space: nowrap;
}

/* 전화번호, 이메일, 인증번호 입력 줄 정렬 */
.phone-group,
.email-group,
.auth-group {
    display: flex;
    align-items: center;
    gap: 8px;
}

.phone-group select,
.email-group select {
    min-width: 100px;
}

/* 인증/확인 버튼 */
.email-group button,
.auth-group button {
    height: 42px;
    padding: 0 16px;
    font-size: 15px;
    font-weight: 500;
    background-color: #3573ee;
    color: #fff;
    border: none;
    border-radius: 30px;
    white-space: nowrap;
    cursor: pointer;
}

.email-group button:hover,
.auth-group button:hover {
    background-color: #255edb;
}

/* 비밀번호 조건 리스트 */
.password-rules {
    margin-top: 6px;
    font-size: 13px;
    padding-left: 16px;
    list-style: disc;
}

/* 동의 버튼 */
.agreement {
    display: flex;
    flex-direction: column;
    gap: 14px;
}

.agreement-con1 {
    background-color: #f5f5f5;
    padding: 16px 24px;
    border-radius: 14px;
    font-size: 16px;
    font-weight: 500;
    display: flex;
    align-items: center;
}

.agreement-con1 label {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 16px;
    font-weight: 500;
}

.agreement-con2 {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 16px 24px;
}

.agreement-left {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.agreement-item label {
    font-size: 15px;
    font-weight: 500;
    display: flex;
    align-items: center;
    gap: 8px;
}

.agreement-sub-items {
    display: flex;
    gap: 40px;
    padding-left: 28px;
    font-size: 14px;
}

.agreement-sub-items label {
    display: flex;
    align-items: center;
    gap: 6px;
}

.agreement-right button {
    background: none;
    border: none;
    font-size: 22px;
    cursor: pointer;
    padding: 0 6px;
}

.agreement-con2 input[type='checkbox'] {
    appearance: none;
    -webkit-appearance: none;
    width: 16px;
    height: 16px;
    margin: 0;
    border: none;
    background: none;
    position: relative;
    cursor: pointer;
}

/* 항상 체크 아이콘 표시 */
.agreement-con2 input[type='checkbox']::after {
    content: '✔';
    position: absolute;
    top: 0;
    left: 0;
    font-size: 16px;
    line-height: 1;
    color: gray; /* 기본 회색 */
}

/* 체크된 경우 색 변경 */
.agreement-con2 input[type='checkbox']:checked::after {
    color: #3573ee; /* 파란색 */
}

/* 힌트/성공/실패 메시지 */
.hint {
    font-size: 13px;
    color: gray;
}

.success {
    font-size: 13px;
    color: green;
}

.error {
    font-size: 13px;
    color: red;
}

/* 버튼 하단 */
.button-group {
    display: flex;
    gap: 10px;
    justify-content: space-between;
    margin-top: 20px;
}

.cancel-button {
    background: #f2f2f2;
    color: #222;
    border: none;
    border-radius: 30px;
    padding: 14px 24px;
    font-size: 16px;
    font-weight: 500;
    min-width: 200px;
    cursor: pointer;
}

.next-button {
    background: #3573ee;
    color: white;
    border: none;
    border-radius: 30px;
    padding: 14px 24px;
    font-size: 16px;
    font-weight: 500;
    min-width: 200px;
    cursor: pointer;
}

.next-button:disabled {
    background: #a5c2ff;
    cursor: not-allowed;
}

.next-button:hover:enabled {
    background-color: #255edb;
}

/* 모바일 대응 */
@media (max-width: 768px) {
    .signup-box {
        padding: 40px 30px;
        border-radius: 30px;
    }

    .title {
        font-size: 22px;
    }

    .form-group input,
    .form-group select {
        font-size: 15px;
        padding: 10px 12px;
    }

    .email-group button,
    .auth-group button,
    .next-button,
    .cancel-button {
        font-size: 14px;
        padding: 12px 16px;
    }
}
</style>
