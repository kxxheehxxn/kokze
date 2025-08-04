<script setup>
import { reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { userAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = userAuthStore();

const assetInfo = reactive({
    monthlyIncomeRaw: '',
    monthlyExpenseRaw: '',
});

const unformat = (value) => value.replace(/,/g, '').replace(/\D/g, '');

const format = (value) => {
    const num = unformat(value);
    if (!num) return '';
    return Number(num).toLocaleString();
};

const getRawNumber = (value) => {
    const raw = unformat(value);
    return raw === '' ? 0 : parseInt(raw);
};

function numberToKorean(num) {
    const units = ['', '만', '억', '조'];
    const result = [];

    let strNum = String(num);
    let i = 0;

    while (strNum.length > 0) {
        const chunk = strNum.length >= 4 ? strNum.slice(-4) : strNum;
        strNum = strNum.slice(0, -4);

        if (Number(chunk) !== 0) {
            result.unshift(`${Number(chunk)}${units[i]}`);
        }
        i++;
    }
    return result.length > 0 ? result.join(' ') + ' 원' : ' 원';
}

const monthlyIncome = computed({
    get: () => format(assetInfo.monthlyIncomeRaw),
    set: (val) => (assetInfo.monthlyIncomeRaw = unformat(val)),
});

const monthlyExpense = computed({
    get: () => format(assetInfo.monthlyExpenseRaw),
    set: (val) => (assetInfo.monthlyExpenseRaw = unformat(val)),
});

const koreanIncome = computed(() =>
    numberToKorean(getRawNumber(monthlyIncome.value))
);

const koreanExpense = computed(() =>
    numberToKorean(getRawNumber(monthlyExpense.value))
);

const resetField = (type) => {
    if (type === 'income') assetInfo.monthlyIncomeRaw = '';
    if (type === 'expense') assetInfo.monthlyExpenseRaw = '';
};

const isFormValid = computed(
    () =>
        assetInfo.monthlyIncomeRaw !== '' && assetInfo.monthlyExpenseRaw !== ''
);

const goNext = () => {
    if (!isFormValid.value) return;

    const income = Number(assetInfo.monthlyIncomeRaw);
    const expense = Number(assetInfo.monthlyExpenseRaw);

    authStore.setUserInfo('salary', income);
    authStore.setUserInfo('payAmount', expense);

    router.push('/signup/step3');
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
                <div class="page-num">2/3</div>
            </div>

            <hr />

            <div class="title">자산정보 입력</div>

            <div class="form-group with-unit">
                <label>월 소득 (원)</label>
                <div class="input-with-unit">
                    <input
                        type="text"
                        v-model="monthlyIncome"
                        placeholder="금액을 입력해주세요"
                    />
                    <button
                        class="reset-btn"
                        v-if="monthlyIncome"
                        @click="resetField('income')"
                    >
                        ×
                    </button>
                    <span class="unit">원</span>
                </div>
                <div class="visual-display">{{ koreanIncome }}</div>
            </div>

            <div class="form-group with-unit">
                <label>월 지출 (원)</label>
                <div class="input-with-unit">
                    <input
                        type="text"
                        v-model="monthlyExpense"
                        placeholder="금액을 입력해주세요"
                    />
                    <button
                        class="reset-btn"
                        v-if="monthlyExpense"
                        @click="resetField('expense')"
                    >
                        ×
                    </button>
                    <span class="unit">원</span>
                </div>
                <div class="visual-display">{{ koreanExpense }}</div>
            </div>

            <div class="button-group">
                <button
                    class="cancel-button"
                    @click="router.push('/signup/step1')"
                >
                    뒤로가기
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

.input-with-unit {
    display: flex;
    align-items: center;
    gap: 8px;
    position: relative;
}

.input-with-unit input {
    flex: 1;
    font-size: 16px;
    padding: 12px 14px;
    border: 1px solid #ccc;
    border-radius: 30px;
}

.input-with-unit input::-webkit-outer-spin-button,
.input-with-unit input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

.unit {
    font-size: 16px;
    color: #222;
}

.reset-btn {
    position: absolute;
    right: 40px;
    background: none;
    border: none;
    font-size: 20px;
    color: #888;
    cursor: pointer;
}

.visual-display {
    margin-top: 4px;
    color: #777;
    font-size: 14px;
}

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

@media (max-width: 768px) {
    .signup-box {
        padding: 40px 30px;
        border-radius: 30px;
    }

    .title {
        font-size: 22px;
    }

    .form-group input {
        font-size: 15px;
        padding: 10px 12px;
    }

    .next-button,
    .cancel-button {
        font-size: 14px;
        padding: 12px 16px;
    }
}
</style>
