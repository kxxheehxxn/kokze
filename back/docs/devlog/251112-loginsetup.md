# 2025-11-12 개발일지 — 로그인 기능 초기 세팅 및 MyBatis 연동

## 오늘 한 일
- [x] **MyBatis 환경 설정 완료**
    - `applicationContext.xml`에 `dataSource`, `sqlSessionFactory`, `mapperScannerConfigurer` 설정 완료
    - `UserMapper.xml` 경로 정리 및 매퍼 등록 확인
- [x] **UserMapper 테스트 성공**
    - DB 연결 및 쿼리 테스트 정상 수행
- [x] **Tomcat 서버 구동 및 배포 확인**
    - `curl http://localhost:8080/api/health` → `{ "status": "ok" }`
    - `/api/products` 및 `/api/recommend` 정상 응답
- [x] **LoginRequest 역직렬화 오류 트러블슈팅**
    - 오류: `Cannot construct instance of LoginRequest (no default constructor)`
    - 원인: `LoginRequest`에 기본 생성자 누락
    - 해결: 기본 생성자 추가 및 Jackson 역직렬화 정상 작동 확인
- [x] **docs 디렉토리 구조 정리**
    - `docs/devlog`, `docs/troubleshooting` 생성

---

##  상세 내용

### MyBatis 연동
- `UserMapper.xml`의 XML 파싱 에러(`Unexpected end of file`) 해결 후 정상 인식
- `org.ozea.user.mapper` 경로 재정의
- DB 스키마 확인 (`users` 테이블)
  ```sql
  id BIGINT AUTO_INCREMENT,
  email VARCHAR(255),
  name VARCHAR(100),
  password_hash VARCHAR(255),
  role VARCHAR(50),
  provider VARCHAR(50),
  ...

---

### 로그인 관련 구성
- `LoginRequest` DTO 생성 (`email`, `password`)
- `AuthController`의 `/api/auth/login` 엔드포인트 생성 및 요청 처리 구현
- JSON 역직렬화 오류(`no default constructor`) 발생 → 기본 생성자 추가로 해결
- 테스트 요청
  ```bash
  curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com", "password":"test123"}'
  ```
- 응답 확인:
  ```json
  {"message": "Login successful", "user": {"email": "test@example.com"}}
  ```
  
---

### 트러블슈팅 요약
| 문제 | 원인 | 해결 |
|------|------|------|
| `mysql:mysql-connector-j:8.4.0` not found | Gradle Maven repository 인식 오류 | `mavenCentral()` 재설정 |
| `UserMapper.xml` unexpected EOF | XML 닫는 태그 누락 | 태그 수정 후 재빌드 |
| `LoginRequest` 역직렬화 실패 | 기본 생성자 없음 | Lombok `@NoArgsConstructor` 추가 |

---

### 배운 점
- Jackson은 객체 역직렬화 시 반드시 기본 생성자가 필요하다.
- MyBatis 매퍼 XML의 경로(`classpath*:` 설정)는 프로젝트 구조와 완전히 일치해야 한다.
- 로그를 통해 Spring Context가 어떤 순서로 Bean을 로드하는지 파악하면 디버깅 속도가 훨씬 빨라진다.

---

### 다음 목표
- [ ] 로그인 서비스 로직 구현 (`UserMapper.findByEmail`)
- [ ] 비밀번호 해시 검증 (`BCryptPasswordEncoder`)
- [ ] JWT 토큰 발급 및 인증 흐름 구성
- [ ] 회원가입 `/api/auth/signup` 추가
- [ ] 예외 처리 통합 (CustomExceptionHandler)
  
