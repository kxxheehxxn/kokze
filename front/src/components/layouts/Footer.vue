<template>
  <footer class="footer bg-light border-top">
    <div class="footer-container container-xxl">
      <div class="d-flex align-items-center">
        <!-- 로고 영역 -->
        <div class="footer-logo-section me-4">
          <img src="@/assets/logo_black.svg" alt="로고" class="footer-logo" />
        </div>
        <!-- 회사 정보 영역 -->
        <div class="company-info">
          <div class="company-name mb-1">(주)꽁재</div>
          <div class="company-address mb-1">
            주소 : 서울 강진구 능동로 195-16 6층(군자동) 605호
          </div>
          <div class="copyright">Copyright 2025. 꽁재 All rights reserved.</div>
        </div>
      </div>
    </div>
  </footer>
</template>
<style scoped>
.footer {
  border-top: 1px solid #e5e7eb;
  background-color: #d3d3d3;
  padding: 24px 40px;
  margin-top: auto;
  width: 100%;
}
.footer-container {
  padding: 0 15px;
}
.footer-logo {
  width: 54px;
  height: 54px;
  object-fit: contain;
}
.company-info {
  line-height: 1.5;
}
.company-name {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 4px;
}
.company-address {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 4px;
}
.copyright {
  font-size: 13px;
  color: #9ca3af;
}
/* 1023px 이하 (데스크탑 -> 태블릿 전환 지점) */
@media (max-width: 1024px) {
  .footer {
    padding: 22px 35px; /* 패딩 약간 줄임 */
  }
  .footer-logo {
    width: 48px; /* 로고 크기 약간 줄임 */
    height: 48px;
  }
  .company-name {
    font-size: 13.5px; /* 폰트 크기 약간 줄임 */
  }
  .company-address,
  .copyright {
    font-size: 12.5px; /* 폰트 크기 약간 줄임 */
  }
}
/* 반응형 */
@media (max-width: 768px) {
  .footer {
    padding: 20px 28px;
  }
  .footer-container {
    padding: 0 15px;
  }
  .footer-logo {
    width: 44px;
    height: 44px;
  }
  .company-name {
    font-size: 13px;
  }
  .company-address,
  .copyright {
    font-size: 12px;
  }
}
@media (max-width: 480px) {
  .footer-container {
    padding: 0 10px;
  }

  .footer-logo-section {
  .logo-section {
    margin-right: 12px !important;
  }
}
</style>
