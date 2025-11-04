# DoubleS
![image](https://github.com/user-attachments/assets/d00b3129-6ea4-4810-b2f7-d8f2e90a391f)

## 설명
DoubleS는 독학하는 사용자들이 그룹 스터디 및 채팅을 통해 함께 학습하고 정보를 공유하는 커뮤니티 플랫폼입니다.
이 프로젝트는 최신 버전의 기술 스택(Spring Boot 3.3.1, Java 21, Vue.js + Vite)을 활용하여 서비스를 구축하고, 특히 성능 병목 현상 해결 및 안정성 확보에 집중하여 주니어 개발자로서의 문제 해결 능력을 입증하는 것을 목표로 했습니다.

## 개발환경
### IDE
 - Intellij IDEA Ultimate 2023.2.2, VSCode
### Java Version
 - Java 21
### Build Tool
 - Gradle 8.5
### Hosting
 - OCI (MySQL), Redis Server, Koyeb/cloudType (Web Service)

## 주요 기술 스택 및 선택 이유
### Backend
* Spring Boot 3.3.1, Java 21 (LTS)
  * 최신 버전의 Spring Boot와 Java 21 LTS 버전을 사용하여 개발 생산성을 확보를 위해 채택 (결과적으로 장기적인 안정성을 갖추게 됨)
### Database
* MySQL
  * 소규모 프로젝트의 안정적인 데이터 영속성 확보를 위해 채택. JPA와의 연동 및 검증된 성능을 고려.
### ORM
* Spring Data JPA
  * 생산성 및 유지보수 편의성 확보를 위해 주력으로 채택
  * 필요 시 JPQL 작성을 통해 N+1 문제 방지나 성능 최적화 등 JPA 기본 메서드로 해결하기 어려운 쿼리를 효율적으로 처리할 수 있음
### Authentication
* Spring Security, JWT 0.12.5
  * 로그인/권한에 따른 엔드포인트 차단 및 보안 처리 및 세션 방식의 서버 부하 문제 발생 가능성 인지 후 JWT 도입으로 상태 비저장(Stateless)으로 구현하기 위해 사용
### Caching
* Redis
  * 인증 과정의 DB 부하 제거 및 조회 속도 향상. Spring Security Context의 UserID를 기반으로 캐시를 조회하는 Look-Aside Cache 전략 적용
### Concurrency
* WebSocket,
  * 그룹 스터디 채팅 등 양방향 실시간 대화 기능 구현하기 위해 채택
* SSE (Server-Sent-Events)
  * 알림 기능 한정으로 사용. WebSocket 대비 리소스 및 메모리 비용이 작아 효율적이기 때문에 사용
### Frontend
* Vue3 + Vite, Pinia
  * 백엔드와 분리된 프론트 구현 및 낮은 학습 곡선이기 때문에 채용. Pinia를 통해 컴포넌트 간 명확하고 모듈화된 전역 상태 관리 구현.

## 그외
* Bootstrap5

## Redis 성능 최적화 상세
### 기술적 문제
* 반복적인 DB 쿼리로 인한 서버 부하 및 매번 DB를 조회하는 데 소요되는 시간. (DB 직접 조회 시 평균 91ms 소요)
### 해결 전략 및 성과
* Redis Look-Aside Cache 패턴 적용으로 DB 접근 횟수 획기적 제거
  * 구현: Spring Security Context의 UserID를 Key로 사용자 정보를 Value로 저장
  * 정량적 성과: 평균 응답 속도 91ms → 10ms (약 90% 개선) 달성

## 코드 안정성 및 유지보수성
### ORM 전략
  - 생산성을 위해 Spring Data JPA를 주력으로 채택했습니다. MyBatis 경험을 바탕으로, 필요 시 Repository 커스텀 및 직접 SQL 작성을 통해 성능 최적화가 필요한 쿼리를 효율적으로 처리할 수 있습니다.
### TDD 원칙 적용
  - 코드 작성 전 TDD의 설계 원칙을 학습하고 적용하여 기능별 로직을 구조화. 실제 테스트 코드 작성 대신, JPQL 최적화와 계층별 명확한 예외 처리에 집중하여 서비스 안정성을 확보. (ex. 비즈니스 예외, 데이터 접근 예외 분리)
### Lombok & DevTools:
  - 중복 코드를 줄이고 개발 속도를 높여 생산성을 최적화.

## 배포 및 접속 정보
* ([koyeb](https://yeasty-kalie-hyeon-7cf77d77.koyeb.app/login)) (1시간 마다 꺼져서 오래걸릴 수 있음)
* ([cloudType](https://port-0-doubles-lyfraywr77b765d8.sel5.cloudtype.app/login)) (안될수도있음)

### 테스트 아이디
* 테스트 ID: tester
* 테스트 Password: 123456
### 어드민 테스트 아이디(공지사항 작성 가능)
* 어드민 테스트 ID: admin
* 어드민 테스트 Password: 123456
