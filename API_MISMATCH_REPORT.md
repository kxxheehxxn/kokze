# 백엔드-프론트엔드 API 불일치 보고서

## 1. Asset API 불일치

### 문제점
`assetApi.js`에서 경로에 `api/`가 중복되어 있습니다. `index.js`의 baseURL이 이미 `/api`이므로 추가로 `api/`를 붙이면 `/api/api/...`가 됩니다.

#### 1.1 getUserAssetSummary
- **프론트엔드**: `api.get('api/${userId}/summary')` → 실제 호출: `/api/api/${userId}/summary`
- **백엔드**: `GET /api/${userId}/summary`
- **수정 필요**: `api.get('${userId}/summary')` 또는 `api.get('/${userId}/summary')`

#### 1.2 getUserBankAccounts
- **프론트엔드**: `api.get('api/${userId}/accounts')` → 실제 호출: `/api/api/${userId}/accounts`
- **백엔드**: `GET /api/${userId}/accounts`
- **수정 필요**: `api.get('${userId}/accounts')` 또는 `api.get('/${userId}/accounts')`

#### 1.3 updateBankAccount
- **프론트엔드**: `api.get('api/allaccount?userId=${userId}')` → 실제 호출: `/api/api/allaccount?userId=...`
- **백엔드**: `GET /api/allaccount?userId=...`
- **수정 필요**: `api.get('allaccount?userId=${userId}')` 또는 `api.get('/allaccount?userId=${userId}')`

## 2. User API 불일치

### 2.1 fetchUserPoint (사용되지 않을 수 있음)
- **프론트엔드**: `GET /api/point/${user_id}`
- **백엔드**: 해당 엔드포인트 없음
- **대안**: 백엔드에는 `GET /api/points/total/{userId}`가 있음
- **참고**: `getUserPoints()` 함수가 `/api/auth/points`를 사용하고 있어서 이 함수는 사용되지 않을 수 있음

### 2.2 signupUser
- **프론트엔드**: `POST /api/user/signup`
- **백엔드**: `POST /api/auth/signup`
- **수정 필요**: 프론트엔드 경로를 `/api/auth/signup`으로 변경

### 2.3 createTestUser
- **프론트엔드**: `POST /api/auth/test-user`
- **백엔드**: 해당 엔드포인트 없음
- **상태**: 백엔드에 구현되어 있지 않음 (개발/테스트용인 것으로 보임)

## 3. Inquiry API 경로 변수 불일치

### 문제점
프론트엔드에서 `{no}`를 사용하지만 백엔드는 `{infoId}` (UUID)를 사용합니다.

- **프론트엔드**: `/api/inquiry/{no}`, `/api/inquiry/{no}/answer` 등
- **백엔드**: `/api/inquiry/{infoId}`, `/api/inquiry/{infoId}/answer` 등
- **상태**: 변수명은 다르지만 기능적으로는 문제없음 (프론트엔드에서 UUID를 `no` 변수로 전달하고 있음)
- **권장**: 코드 가독성을 위해 프론트엔드에서도 `infoId`를 사용하는 것을 권장

## 4. Notice API 경로 변수 불일치

### 문제점
프론트엔드에서 `{no}`를 사용하지만 백엔드는 `{noticeId}` (UUID)를 사용합니다.

- **프론트엔드**: `/api/notice/{no}`
- **백엔드**: `/api/notice/{noticeId}`
- **상태**: 변수명은 다르지만 기능적으로는 문제없음 (프론트엔드에서 UUID를 `no` 변수로 전달하고 있음)
- **권장**: 코드 가독성을 위해 프론트엔드에서도 `noticeId`를 사용하는 것을 권장

## 5. Quiz API 파라미터 타입

### 문제점
프론트엔드와 백엔드 모두 `userId`를 query parameter로 사용하지만, 백엔드는 String 타입을 기대합니다.

- **프론트엔드**: `GET /api/quiz/today?userId=...` (UUID 문자열 전달)
- **백엔드**: `GET /api/quiz/today?userId=...` (String 타입, UUID 문자열 수신)
- **상태**: 현재는 문제없지만, 백엔드에서 UUID 타입으로 변경할 경우 문제가 될 수 있음
- **참고**: 백엔드의 `validateUserId` 메서드에서 문자열 길이 검증을 하고 있음

## 6. Goal API 파라미터 타입

### 문제점
프론트엔드에서 `userId`를 query parameter로 전달하지만, 백엔드는 UUID 타입을 요구합니다.

- **프론트엔드**: `GET /api/goal?userId=...` (문자열로 전달 가능)
- **백엔드**: `GET /api/goal?userId=...` (UUID 타입 요구)
- **상태**: UUID 문자열이 올바르게 전달되면 문제없음

## 7. Point API 엔드포인트 구조

### 비교
- **프론트엔드**: `GET /api/auth/points`, `GET /api/auth/points/history` (UserController)
- **백엔드**: 
  - `GET /api/auth/points` (UserController) ✅
  - `GET /api/auth/points/history` (UserController) ✅
  - `GET /api/points/total/{userId}` (PointController)
  - `GET /api/points/history/{userId}` (PointController)
- **상태**: 두 가지 방식이 공존하고 있음
  - UserController: JWT 토큰 기반 (현재 사용자)
  - PointController: userId 경로 변수 기반

## 요약

### 🔴 심각한 문제 (즉시 수정 필요)
1. **assetApi.js의 경로 중복**: `api/`가 중복되어 실제로는 `/api/api/...`로 호출됨
2. **signupUser 경로 불일치**: `/api/user/signup` → `/api/auth/signup`

### 🟡 주의 필요 (기능은 작동하지만 개선 권장)
1. **fetchUserPoint**: 백엔드에 해당 엔드포인트가 없음 (사용되지 않을 수 있음)
2. **createTestUser**: 백엔드에 구현되어 있지 않음
3. **변수명 불일치**: `{no}` vs `{infoId}`, `{noticeId}` (기능적으로는 문제없지만 가독성 저하)

### 🟢 정상 작동
- 나머지 API들은 정상적으로 작동합니다.

