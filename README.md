# Finance Management Microservices

클라우드 네이티브 과제로 제출하기 위한,
클라우드 네이티브 환경에서 운영되는 가계부 마이크로서비스 애플리케이션

## 시스템 아키텍처

```mermaid
graph TB
    Client[Client] --> Gateway[API Gateway :8000]
    Gateway --> US[User Service :8001]
    Gateway --> TS[Transaction Service :8002]
    
    subgraph Databases
      Redis[(Redis Cache)]
      MySQL[(MySQL DB)]
    end
    
    US --> Redis
    US --> MySQL
    TS --> Redis
    TS --> MySQL
```

## 기술 스택

- **Framework:** Spring Boot
- **Database:** MySQL
- **Cache:** Redis
- **Container:** Docker
- **API Gateway:** Spring Cloud Gateway
- **Documentation:** Swagger/OpenAPI
- **Build Tool:** Gradle/Maven

## 시작하기

### 사전 요구사항
- Docker
- Docker Compose
- JDK 17+
- Maven 또는 Gradle

### 실행 방법

1. 프로젝트 클론
```bash
git clone https://github.com/yourusername/finance-microservices.git
```

2. 서비스 빌드
```bash
./gradlew clean build
```

3. 도커 컨테이너 실행
```bash
docker-compose up -d
```

4. 서비스 접속
- API Gateway: http://localhost:8000
- Swagger UI: http://localhost:8000/swagger-ui.html

## 프로젝트 구조
```
project-root/
├── api-gateway/
├── user-service/
├── transaction-service/
├── analytics-service/
├── notification-service/
├── docker-compose.yml
└── README.md
```

## 도메인 설명
### API Gateway (:8000)
- 라우팅 설정
- 인증/인가 필터
- 로드 밸런싱
```
├── api-gateway/
│   ├── src/main/java/com/finance/gateway/
│   │   ├── GatewayApplication.java
│   │   └── config/
│   │       └── RouteConfig.java
│   └── Dockerfile
```
### User Service (회원 도메인:8001)
- 사용자 관리
- 인증/인가
- 프로필 관리
- 알림 설정 관리

```
user-service/
├── src/main/java/com/finance/user/
│   ├── controller/
│   │   ├── UserController.java
│   │   └── AuthController.java
│   ├── service/
│   │   ├── UserService.java
│   │   └── AuthService.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── domain/
│   │   ├── User.java
│   │   └── UserPreference.java
│   └── dto/
│       ├── UserDTO.java
│       └── AuthDTO.java
```

### Transaction Service (거래 도메인:8002)

- 수입 관리
- 지출 관리
- 거래 내역 기록
- 카테고리 관리
```
Transaction Service
├── src/main/java/com/finance/transaction/
│   ├── controller/
│   │   ├── IncomeController.java
│   │   └── ExpenseController.java
│   ├── service/
│   │   ├── TransactionService.java
│   │   └── CategoryService.java
│   ├── repository/
│   │   ├── TransactionRepository.java
│   │   └── CategoryRepository.java
│   ├── domain/
│   │   ├── Transaction.java
│   │   └── Category.java
│   └── dto/
│       ├── TransactionDTO.java
│       └── CategoryDTO.java
```
### Analytics Service (분석 도메인:8003)

- 월별 지출 분석
- 카테고리별 분석
- 예산 대비 지출 분석
- 지출 패턴 분석
```
analytics-service/
├── src/main/java/com/finance/analytics/
│   ├── controller/
│   │   ├── BudgetAnalysisController.java
│   │   └── SpendingAnalysisController.java
│   ├── service/
│   │   ├── AnalyticsService.java
│   │   └── ReportService.java
│   ├── repository/
│   │   └── AnalyticsRepository.java
│   ├── domain/
│   │   ├── BudgetAnalysis.java
│   │   └── SpendingPattern.java
│   └── dto/
│       ├── AnalyticsDTO.java
│       └── ReportDTO.java
```
### Notification Service (알림 도메인:8004)

- 예산 초과 알림
- 월간 리포트 발송
- 중요 지출 알림
- 알림 이력 관리
```
notification-service/
├── src/main/java/com/finance/notification/
│   ├── controller/
│   │   └── NotificationController.java
│   ├── service/
│   │   ├── NotificationService.java
│   │   └── EmailService.java
│   ├── repository/
│   │   └── NotificationRepository.java
│   ├── domain/
│   │   ├── Notification.java
│   │   └── NotificationPreference.java
│   └── dto/
│       └── NotificationDTO.java
```

## 데이터베이스 구성

### MySQL
- Database: finance_db
- 각 서비스별 독립적인 스키마 사용
- 포트: 3306

### Redis
- 세션 관리 및 캐싱
- 포트: 6379

## API 문서
각 서비스의 API 문서는 Swagger UI를 통해 확인할 수 있습니다:
- User Service: http://localhost:8001/swagger-ui.html
- Transaction Service: http://localhost:8002/swagger-ui.html
- Analytics Service: http://localhost:8003/swagger-ui.html
- Notification Service: http://localhost:8004/swagger-ui.html

## 제출 시에
- 1,2 번 항목은 가능하다면, 컨테이너 구성 등의 방법을 캡쳐해서 제출
- 결과만 캡쳐하고 코드는 스냥 파일로 제출하면 됨
- 도커 관련 파일은 그냥 허브에 올리면 됨
- 용량이 정 부족하다 싶으면 구글 드라이브에 올려서 링크 공유 가능
