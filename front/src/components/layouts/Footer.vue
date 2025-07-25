<template>
  <footer class="footer bg-light border-top">
    <div class="footer-container container-xxl">
      <div class="d-flex align-items-center">
        <!-- 로고 영역 -->
        <div class="logo-section me-4">
          <img src="@/assets/logo_black.svg" alt="로고" class="footer-logo" />
        </div>

        <!-- 회사 정보 영역 -->
        <div class="company-info">
          <div class="company-name mb-1">(주)꽁재</div>
          <div class="company-address mb-1">주소 : 서울 강진구 능동로 195-16 6층(군자동) 605호</div>
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
  padding: 24px 0;
  margin-top: auto;
}

.footer-container {
  padding: 0 15px;
}

.footer-logo {
  width: 60px;
  height: 60px;
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

/* 반응형 */
@media (max-width: 768px) {
  .footer {
    padding: 20px 0;
  }

  .footer-container {
    padding: 0 15px;
  }

  .footer-logo {
    width: 50px;
    height: 50px;
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

  .logo-section {
    margin-right: 12px !important;
  }
}
</style>
