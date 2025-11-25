# Kokze 백엔드 – 금융상품 AI 추천 서비스

kokze는 MZ 세대를 위한 금융상품 추천 서비스입니다.  
사용자의 위험 성향과 로그인 여부에 따라, 크롤링한 금융 상품을 기반으로  
룰 기반 + AI 기반 추천을 제공합니다.

---

## 기술 스택

- Java 17  
- Spring Boot 3  
- Spring Security + JWT  
- Spring Data JPA (MySQL)  
- Flyway (DB 마이그레이션)  
- 도메인 기반 패키지 구조 (DDD-lite)  
- AI Summary / Recommendation Layer (개발 중)

---

# 아키텍처 개요

도메인 단위로 패키지를 분리하고,  
핵심 도메인에는 인터페이스 포트를 두어  
구현체를 자유롭게 교체할 수 있는 구조입니다.

- /auth → 로그인/회원가입/JWT  
- /assessment → 사용자 위험 성향 평가  
- /product → 상품, 크롤링 연동  
- /recommend → 추천 엔진 (룰 기반 + AI)  
- /ai → AI 요약·추천 포트  
- /common → 공통 API, 에러 핸들러  
- /config → Spring 설정  

---

# 인증 / 유저 흐름

## 1) 로그인 / 회원가입
LocalAuthService가 이메일·비밀번호 기반 인증 처리.  
회원가입 시 User 저장 → 비밀번호는 PasswordEncoder로 해시.

## 2) JWT 발급·검증
JwtProvider.generateToken(email)으로 JWT 생성.  
JwtAuthenticationFilter가 토큰 검증 후  
SecurityContextHolder의 principal을 email로 설정.

## 3) 현재 로그인 유저 조회
SecurityContextHolder에서 Authentication → principal(email) 추출 → User 조회.

---

# 성향 평가(Assessment)

사용자가 검사 제출 → 위험 성향을 4단계로 분류  
(CONSERVATIVE / MEDIUM / AGGRESSIVE / VERY_AGGRESSIVE)

DB 테이블: assessment_results  
저장소: AssessmentRepository  
최신 기록: findTopByUserAndTypeOrderByCreatedAtDesc

엔티티 구성  
- user (외래키)  
- type (RISK_TOLERANCE)  
- resultCode  
- score  
- createdAt  

---

# 상품 / 크롤링 / 카탈로그

ProductDto 엔티티 필드
- name  
- category  
- riskLevel  
- interestRate  
- minBalance  
- description  

카탈로그 포트(ProductCatalog)를 통해  
DB 기반, 크롤링 기반 어떤 방식도 교체 가능.

구현체  
- DbProductCatalog (JPA)  
- CrawlingProductCatalog (크롤링 / Primary)  

---

# 추천 엔진 구조

Recommender 인터페이스  
recommendFor(User userOrNull)

userOrNull이 null → 비로그인 추천  
user 존재 → 성향 기반 추천

구현체  
1. RuleBasedRecommender  
   - 위험 성향을 riskLevel로 매핑  
   - 상품 목록 필터링 후 추천  

2. AiRecommender  
   - 후보 상품 생성  
   - AiRecommendationClient로 전달  
   - 현재는 Dummy 구현 → 나중에 LLM으로 교체 가능  

---

# AI 연동

## 약관 3줄 요약
TermsSummarizer 인터페이스  
구현체  
- SimpleTermsSummarizer  
- AiTermsSummarizer (Primary)  
  → TextSummaryClient 사용  
  → 현재는 Dummy, 나중에 LLM 연동  

## AI 상품 추천
AiRecommendationClient  
List<ProductDto> rankProducts(User userOrNull, List<ProductDto> candidates)

구현체: DummyAiRecommendationClient  
(이자율 높은 순 정렬 → LLM으로 바꿀 준비된 구조)

---

# 추천 API

## 1) 룰 기반 추천
GET /api/recommend  
GET /api/recommend?strategy=rule  

## 2) AI 추천
GET /api/recommend?strategy=ai  
GET /api/recommend/ai  
