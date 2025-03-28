# 🌱 OLLIM 프로젝트

멀티모듈 Spring Boot 프로젝트입니다.

---

해당 프로젝트는 오로지 명령어만으로 빌드 및 dependencies refresh 사용바람

## 🔧 Gradle 명령어 모음

### 📦 전체 빌드 & 의존성 새로고침

```bash
./gradlew clean build --refresh-dependencies
```

---

### 🧪 테스트 실행

- 전체 테스트 실행

```bash
./gradlew test
```

- 특정 모듈만 테스트

```bash
./gradlew :app-api:test
./gradlew :admin-api:test
```

---

### 🚀 실행

> 실행 가능한 모듈에서만 사용 (예: `app-api`, `admin-api`)

```bash
./gradlew :app-api:bootRun
./gradlew :admin-api:bootRun
```

---

### 🧱 프로젝트 구조 보기

```bash
./gradlew projects
```

---

### 🧹 빌드 캐시, 의존성 정리

```bash
./gradlew clean
./gradlew --refresh-dependencies
```

---

### 🔍 특정 태스크 목록 보기

```bash
./gradlew tasks
./gradlew :app-api:tasks
```

---

## 📁 모듈 구성

| 모듈 | 설명 |
|------|------|
| `core` | 도메인, DTO, 서비스 등 핵심 비즈니스 로직 |
| `infrastructure` | DB/Redis/Mongo 등 외부 연동 처리 |
| `app-api` | 사용자용 API 서버 |
| `admin-api` | 운영자용 관리자 API 서버 |

---

## 📝 기타 참고

- Java 17 이상 필요
- Kotlin DSL(`.kts`) 기반 설정
- 의존성 버전은 Spring BOM(`3.4.4`) 기준
