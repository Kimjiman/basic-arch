# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.5.9 web application (Java 21, Gradle)
- Root package: `com.example.basicarch`
- Swagger UI: `http://localhost:8085/swagger-ui/index.html`

## Build & Run

```bash
# Build
JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew clean build

# Run (with profile)
JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew bootRun -PspringProfiles=local

# Run all tests
JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew test

# Run single test class
JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew test --tests "com.example.basicarch.module.user.UserServiceTest"

# Run single test method
JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew test --tests "*.UserServiceTest.testMethodName"
```

- JDK 21 required (`C:\java\jdk-21.0.10+7`)
- In Claude Code (Git Bash): always use `./gradlew` (Unix wrapper), not `gradlew.bat`
- Git Bash path: `C:\java\...` becomes `/c/java/...`

### Docker (Local Development)

- Windows의 경우 **WSL2 + Docker** 설치 필요
- `local` 프로필 시 `LocalDockerConfig`가 `docker-compose up -d`를 자동 실행

```bash
# 수동 실행 시
docker-compose up -d

# Stop all services
docker-compose down
```

### Profiles

| Profile | Port | DB | Description |
|---------|------|----|-------------|
| `local` (default) | 8085 | PostgreSQL (localhost:15432) | DevTools enabled, show-sql on, file store: `c:/tmp/basic-arch` |
| `dev` | 8080 | PostgreSQL (container) | show-sql on, file logging, file store: `/web/file` |
| `prod` | 8080 | PostgreSQL (container) | show-sql off, file logging, file store: `/web/file` |

## Tech Stack

- **Spring Boot 3.5.9** (spring-security, spring-data-jpa, thymeleaf, webflux)
- **JPA + QueryDSL 5.0** (dynamic queries)
- **MapStruct 1.5.5** (Entity <-> Model conversion)
- **Lombok** (@SuperBuilder, @Getter/@Setter)
- **Spring Security + JWT** (jjwt 0.11.5)
- **PostgreSQL** (runtime), **Redis** (token store, cache)
- **Flyway** (DB schema migration)
- **Docker + Docker Compose** (PostgreSQL, Redis)
- **JUnit 5 + Mockito + Instancio** (testing)

## Architecture — Layered + Facade Pattern

```
Controller → Facade → Service → Repository (JPA + QueryDSL)
                ↕
            Converter (MapStruct)
```

| Layer | Role | Location |
|---|---|---|
| **Controller** | HTTP request/response, `@RestController` | `module/{name}/controller/` |
| **Facade** | Cross-service orchestration, Model conversion, **validation**, **exception throwing** (`@Facade`) | `module/{name}/facade/` |
| **Service** | Single-domain business logic | `module/{name}/service/` |
| **Repository** | JPA + QueryDSL data access | `module/{name}/repository/` |
| **Converter** | MapStruct Entity↔Model mapping | `module/{name}/converter/` |

### Auth Architecture

```
JwtAuthenticationFilter → JwtTokenProvider (JWT verification)
                        → AuthUserDetailsService (load user from DB → AuthUserDetails)

AuthFacade  → UserService (login)
            → JwtTokenService  → JwtTokenProvider (JWT creation)
                               → RefreshTokenStore (interface)
                                     ↑
                               RedisRefreshTokenStore (implementation)
```

| Class | Role |
|---|---|
| **AuthUserDetailsService** | `UserDetailsService` impl, loads user from DB → `AuthUserDetails` |
| **AuthUserDetails** | `UserDetails` record wrapping `User` entity |
| **JwtAuthenticationFilter** | Validates JWT, loads user via `AuthUserDetailsService`, sets SecurityContext |
| **JwtTokenProvider** | Pure JWT sign/verify/parse (no Redis dependency) |
| **JwtTokenService** | Token lifecycle: create, reissue, delete, duplicate login check |
| **RefreshTokenStore** | Abstraction interface for refresh token storage |
| **RedisRefreshTokenStore** | Redis impl (key: `jwt:refresh:{loginId}`) |
| **AuthFacade** | Auth facade: login, logout, token reissue |
| **AuthController** | Auth API: `/auth/login`, `/auth/logout`, `/auth/issueAccessToken` |

- JWT config in `src/main/resources/jwt.yml` (loaded via `spring.config.import`)
- To swap storage: add new `RefreshTokenStore` implementation

### Modules

- `code` — Code/CodeGroup management (CodeGroup ↔ Code: @OneToMany)
- `user` — User management & auth (JWT-based)
- `menu` — Menu management
- `file` — File upload/download

### Cache (Redis)

`Redis` 기반 캐시. Redis Pub/Sub를 이용한 캐시 무효화 전략.

| 캐시 | key → field | 리프레시 주기 |
|------|-------------|-------------|
| Code 캐시 | `cache:code` → `all` | 스케줄러 (`cron.yml`) |
| Menu 캐시 | `cache:menu` → `all` / `useYn:Y` | 스케줄러 (`cron.yml`) |
| Menu 역할 캐시 | `cache:menu:role` → `{uri}` | 스케줄러 (`cron.yml`) |

- 스케줄러: `CacheScheduler` (`cron.yml`에서 주기 설정)
- Menu 역할 캐시는 `RoleInterceptor`에서 URI별 권한 체크에 사용

## Key Conventions

### Model Structure (Entity / Model Separation)

```
BaseObject
├── BaseEntity<ID>   — JPA base class (@MappedSuperclass)
│   ├── id, rowNum(@Transient), createTime, createId, updateTime, updateId
│   └── @PrePersist, @PreUpdate auto-auditing
└── BaseModel<ID>    — API request/response DTO
    └── id, createId, createTime(String), updateId, updateTime(String)
```

- **Entity**: `module/{name}/entity/{Name}.java` — JPA entity, `extends BaseEntity<Long>`
- **Model**: `module/{name}/model/{Name}Model.java` — DTO for Facade/Controller, `extends BaseModel<Long>`
- **SearchParam**: `module/{name}/model/{Name}SearchParam.java` — Search criteria

### Converter Rules (MapStruct)

- `toModel(Entity)` / `toEntity(Model)` — single conversion
- `toModelList(List<Entity>)` / `toEntityList(List<Model>)` — list conversion
- `TypeConverter` — type conversions (`uses = {TypeConverter.class}`)
  - `LocalDateTime ↔ String`: `qualifiedByName = "localDateTimeToString"` / `"stringToLocalDateTime"`
  - `String ↔ YN`: `qualifiedByName = "stringToYn"` / `"ynToString"`
- Model→Entity: `@Mapping(target = "rowNum", ignore = true)`
- Entity stores `YN` columns as `String`, Model uses `YN` enum → MapStruct converts

### Service Rules

- `implements BaseService<T extends BaseEntity<ID>, P extends BaseSearchParam<ID>, ID>`
- Standard methods: `existsById`, `findById`, `findAllBy`, `save`, `update`, `deleteById`

### Repository Rules

- `{Name}Repository extends JpaRepository<T, ID>, {Name}RepositoryCustom`
- `{Name}RepositoryCustom` — QueryDSL custom query interface
- `{Name}RepositoryImpl` — QueryDSL implementation (`JPAQueryFactory`)

### Validation Strategy

**Intentional design: ToyAssert in Facade layer instead of Bean Validation (@Valid)**

Rationale:
- `@Valid` + annotation scatters validation logic across DTO classes → hard to trace
- Facade-centralized validation keeps all checks (input + business) in one place
- Easier to debug: one breakpoint in Facade catches all validation flow

```java
// All validation visible in Facade — single point of control
ToyAssert.notBlank(loginId, SystemErrorCode.REQUIRED, "Please enter login ID.");
ToyAssert.notNull(id, SystemErrorCode.REQUIRED);
ToyAssert.isTrue(condition, SystemErrorCode.DUPLICATE_LOGIN);
throw new CustomException(SystemErrorCode.FILE_ERROR, "Error during file download.");
```

### Exception Rules

```
ErrorCode (strategy interface)
    └── SystemErrorCode (common error enum)
            ↓
    CustomException (delegates to ErrorCode)
            ↓
    ExceptionAdvice (assembles response)
```

- **Exceptions should only be thrown in Facade layer**
- **Validation uses `ToyAssert`** (simple checks), complex conditions use direct `throw`
- Add errors to `SystemErrorCode` enum; extract module-specific enum when it grows large

### JPA Relationships

- `@OneToMany` + `@JoinColumn(insertable=false, updatable=false)` — read-only relation
- LAZY fields require `@ToString.Exclude`
- List queries: QueryDSL `.leftJoin().fetchJoin().distinct()`
- Single queries: `@EntityGraph(attributePaths = {...})`

### Database Migration (Flyway)

- Migration scripts: `src/main/resources/db/migration/`
- Naming: `V{version}__{description}.sql`
- `V1__init_schema.sql` — 전체 테이블 스키마 생성 (user, file, code, code_group, menu, role, user_role)
- `V2__init_data.sql` — 기본 데이터 (admin 계정, ADM/USR 역할, user_role 매핑)
- `ddl-auto: validate` — 모든 프로필에서 스키마 검증만 수행, 변경은 Flyway로만

## Commit Convention

```
[Type] Summary - Detail
```

| Type | Usage |
|---|---|
| **Feature** | New feature |
| **Fix** | Bug fix |
| **Well** | Improvement, refactoring |
| **Docs** | Documentation |
| **Chore** | Build config, dependency changes |
| **Remove** | Delete feature/code |

### Response & Pagination

- **`Response<T>`**: API envelope — `Response.success(data)` / `Response.fail(status, message)`, `@JsonInclude(NON_NULL)`
- **`PageResponse<T>`**: Wraps `PageInfo` + `List<T>`, auto-assigns descending `rowNum` in constructor
- **`PageInfo`**: Static factory `PageInfo.of(page, totalRow)` or `PageInfo.from(Page<?>)` for Spring Data

### YN Enum Pattern

Entity stores `String` ("Y"/"N"), Model uses `YN` enum. MapStruct converts via `TypeConverter`.
- `YN.of("true"/"false")` — from JSON boolean-like input (`@JsonCreator`)
- `YN.fromValue("Y"/"N")` — from DB value

### Auditing

`BaseEntity` auto-sets `createTime`/`updateTime` via `@PrePersist`/`@PreUpdate`, and `createId`/`updateId` via `SessionUtils.getId()`. System operations use `entity.setSystemUser()` (sets ID to 0L).

### Key Utilities

| Utility | Purpose |
|---|---|
| `SessionUtils` | Static access to current user from SecurityContext (`getPrincipal`, `getId`, `getUser`, `hasRole`) |
| `ToyAssert` | Validation: `notBlank`, `notNull`, `notEmpty`, `isTrue` — throws `CustomException` on failure |
| `DateUtils` | 40+ date/time operations, pattern: `"yyyy-MM-dd HH:mm:ss"` |
| `StringUtils` | Null-safe string ops, masking, padding, regex |
| `CollectionUtils` | `safeStream`, `merge`, `extractList`, `toMap`, `removeDuplicates` |
| `JsonUtils` | GSON-based with LocalDateTime adapters |

## Infrastructure

### Docker Compose Services

| Service      | Image             | Port  | Purpose            |
|--------------|-------------------|-------|--------------------|
| `postgres`   | postgres:15       | 15432 | Primary database   |
| `redis`      | redis:7-alpine    | 16379 | Token store, cache |
| `prometheus` | prometheus-latest | 19090 | Log Store          |
| `grafana`    | grafana-latest    | 13000 | GUI                |

### AWS (ap-southeast-1 싱가포르)

| 서비스 | 스펙 | 용도 |
|------|------|------|
| EC2 | t3.micro | Spring Boot 서버 |
| RDS | db.t3.micro, PostgreSQL 15 | 데이터베이스 |
| ElastiCache | cache.t3.micro, Redis 7 | 토큰 저장소, 캐시 |
| Nginx | EC2 내 설치 | 리버스 프록시 (:80 → :8080) |

### CI/CD

- **Railway**: main 브랜치 push 시 자동 배포
- **GitHub Actions**: main 브랜치 push 시 EC2 자동 배포 (`.github/workflows/deploy_dev.yml`)
  - GitHub Actions IP 동적 허용/제거 (보안 그룹 자동 관리)
  - app.pid 기반 프로세스 종료

## AWS 비용 정책

- **ElastiCache 제외한 모든 AWS 유료 리소스 생성 금지**
- EC2, RDS 등은 프리티어 범위 내에서만 사용
- 작업 종료 시 반드시 확인: "현재 실행 중인 유료 AWS 리소스가 있습니다. 종료하시겠습니까?"
- 비용이 발생할 수 있는 작업 전에는 반드시 먼저 물어볼 것

## Learning Roadmap

이직 준비 학습 순서 (CKA 목표)

| 순서 | 기술 | 목표 | 상태 |
|------|------|------|------|
| 1 | **Prometheus + Grafana** | Actuator 메트릭 수집, 대시보드 구성 | 완료 |
| 2 | **AWS + ElastiCache** | EC2 배포, RDS(PostgreSQL), ElastiCache(Redis) 연동 | 진행중 |
| 3 | **Kubernetes** | minikube로 현재 프로젝트 배포, Deployment/Service/ConfigMap/Secret/HPA 실습 | 대기 |
| 4 | **Kafka** | docker-compose에 Kafka 추가, 이벤트 드리븐 아키텍처 실습, DLT 처리 | 대기 |

## Project Structure

```
src/main/java/com/example/basicarch/
├── base/           — Shared infrastructure (annotations, components, constants, converters,
│                     exceptions, models, redis, security/jwt, services, utils)
├── config/         — Spring config (Security, CORS, Redis, Swagger, QueryDSL, WebConfig)
│   ├── advice/     — ResponseAdvice, ExceptionAdvice
│   ├── interceptor/ — RoleInterceptor
│   └── scheduler/  — CacheScheduler
└── module/         — Feature modules (code, user, menu, file, main, test)
                      Each module: controller/, converter/, entity/, facade/, model/, repository/, service/

src/main/resources/
├── db/migration/   — Flyway scripts (V1__init_schema.sql, V2__init_data.sql)
├── application.yml — Spring config (profiles: local, dev, prod)
├── jwt.yml         — JWT configuration
└── cron.yml        — Cache scheduler intervals

docker-compose.yml  — Local dev environment (PostgreSQL, Redis, Prometheus, Grafana)
```
