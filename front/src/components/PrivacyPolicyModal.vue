<script setup>


const emit = defineEmits(['close', 'agree']);

const handleAgree = () => {
    emit('agree', {
        agreeSub1: true,
        agreeSub2: true,
        agreeSub0: true,
        agreed: true,
    });
    emit('close');
};

const handleReject = () => {
    emit('agree', {
        agreeSub1: false,
        agreeSub2: false,
        agreeSub0: false,
        agreed: false,
    });
    emit('close');
};
</script>

<template>
    <div class="modal-backdrop">
        <div class="privacy-modal">
            <div class="modal-header">
                <h2>개인(신용)정보 수집·이용 동의</h2>
            </div>

            <div class="modal-content-scrollable">
                <div class="modal-section">
                    <h3>
                        [필수] 전자금융거래 회원가입을 위한 개인(신용)정보 처리
                        동의서
                    </h3>

                    <h4>수집·이용 목적</h4>
                    <ul>
                        <li>전자금융거래 회원가입 및 회원관리</li>
                        <li>전자금융서비스 제공, 유지, 관리, 개선</li>
                        <li>상담, 민원 분쟁 해결, 법령상 의무 이행</li>
                        <li>본인 여부 확인, 이상금융거래 예방 및 조사 관리</li>
                    </ul>

                    <h4>보유 및 이용기간</h4>
                    <p>
                        · 동의일로부터 거래 종료 시까지<br />
                        (단, 관련 법령에 따른 보존기간 존재 시 해당 기간까지)
                    </p>
                </div>

                <div class="modal-section">
                    <h4>거부 권리 및 불이익</h4>
                    <p>
                        · 동의를 거부할 수 있으나, 본 동의는 회원가입에
                        필수이므로 거부 시 가입 불가
                    </p>
                </div>

                <div class="modal-section">
                    <h4>수집·이용 항목</h4>

                    <strong>고유식별정보</strong>
                    <p>· 주민등록번호, 외국인등록번호</p>
                    <p>※ 고유식별정보 수집·이용에 동의하십니까?</p>
                    <div class="radio-group">
                        <label><input type="radio" disabled /> 동의</label>
                        <label><input type="radio" disabled /> 동의안함</label>
                    </div>

                    <strong>개인(신용)정보</strong>
                    <p>· 일반개인정보</p>
                    <p class="info-text">
                        성명, 생년월일, 성별, 본인확인정보(C.I, D.I),
                        휴대폰번호, 이메일, 영문명, 국적, 주소, 연락처, 내외국인
                        구분, 쿠키정보, 고유식별번호(ID), ADID, IDFA,
                        모바일기기정보, OS정보, 접속로그, 브라우저정보, IP주소,
                        단말정보(UUID, IMEI, MAC주소), 악성앱 및 원격앱 설치
                        구동 정보
                    </p>
                    <p>※ 개인신용정보 수집·이용에 동의하십니까?</p>
                    <div class="radio-group">
                        <label><input type="radio" disabled /> 동의</label>
                        <label><input type="radio" disabled /> 동의안함</label>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <p class="confirm-text">
                    위 내용을 충분히 확인했으며 이에 동의합니다.
                </p>
                <div class="button-group">
                    <button class="reject" @click="handleReject">
                        동의안함
                    </button>
                    <button class="accept" @click="handleAgree">동의함</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.privacy-modal {
    display: flex;
    flex-direction: column;
    height: 90vh;
    width: 95%;
    max-width: 600px;
    background: #fff;
    border-radius: 30px;
    overflow: hidden;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    font-size: 14px;
    line-height: 1.5;
    color: #333;
}

.modal-header {
    flex-shrink: 0;
    padding: 20px 24px 8px;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
}

.modal-header h2 {
    display: inline-block;
    font-size: 18px;
    margin: 0 auto;
    text-align: center;
    font-weight: 600;
}

.modal-content-scrollable {
    flex-grow: 1;
    overflow-y: auto;
    padding: 16px 24px;
}

.modal-section {
    margin-bottom: 20px;
}

.modal-section h3 {
    font-size: 15px;
    font-weight: 600;
    margin-bottom: 8px;
}

.modal-section h4 {
    font-size: 14px;
    font-weight: 500;
    margin: 10px 0 6px;
}

.modal-section ul {
    padding-left: 18px;
    margin-bottom: 10px;
}

.modal-section li {
    font-size: 13px;
    margin-bottom: 4px;
}

.modal-section p {
    font-size: 13px;
    margin: 4px 0;
}

.info-text {
    font-size: 12.5px;
    color: #555;
    margin: 6px 0;
}

.radio-group {
    display: flex;
    gap: 16px;
    margin: 10px 0;
    opacity: 0.6;
    font-size: 13px;
}

.modal-footer {
    flex-shrink: 0;
    padding: 16px 24px;
    background-color: #fafafa;
    border-top: 1px solid #eee;
    flex-direction: column;
}

.confirm-text {
    padding-bottom: 10px;
}

.button-group {
    display: flex;
    justify-content: space-between;
    gap: 12px;
}

.reject,
.accept {
    flex: 1;
    min-width: 200px;
    padding: 12px 0;
    font-size: 14px;
    border: none;
    border-radius: 30px;
    cursor: pointer;
    text-align: center;
}

.reject {
    background: #f2f2f2;
    color: #222;
}

.accept {
    background-color: #0066ff;
    color: white;
}
</style>
