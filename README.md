# DoubleS
![image](https://github.com/user-attachments/assets/d00b3129-6ea4-4810-b2f7-d8f2e90a391f)

## 설명
DoubleS는 독학하는 사용자들이 그룹 스터디 및 채팅을 통해 함께 학습하고 정보를 공유하는 커뮤니티 플랫폼입니다.
이 프로젝트는 최신 버전의 기술 스택(Spring Boot 3.3.1, Java 21, Vue.js + Vite)을 활용하여 서비스를 구축하고, 특히 성능 문제 해결 및 안정성 확보에 집중하여 문제 해결 능력을 향상하는 것을 목표로 했습니다.

## 개발환경
 - Intellij IDEA Ultimate 2023.2.2
 - VSCode
 - Docker
 - Java 21
 - Gradle 8.5
### Hosting
 - OCI (MySQL), Redis Server, Koyeb/cloudType (Web Service)
## 주요 기술 스택 및 선택 이유
### Backend
* Spring Boot 3.3.1, Java 21 (LTS)
  * 최신 버전의 Spring Boot와 Java 21 LTS 버전을 사용하여 개발 생산성을 확보를 위해 채택 (결과적으로 장기적인 안정성을 갖추게 됨)
* Lombok, Devtools
  * 중복 코드를 줄이고 개발 속도를 높여 생산성 최적화 위해 사용
### Database
* MySQL
  * 소규모 프로젝트의 안정적인 데이터 영속성 확보 및 JPA와의 연동 및 검증된 성능을 고려
### ORM
* Spring Data JPA
  * 생산성 및 유지보수 편의성 확보를 위해 주력으로 채택
  * 필요 시 JPQL 작성을 통해 JPA로 조회하기 힘든 부분 조회 쿼리 커스텀 등 JPA 기본 메서드로 해결하기 어려운 쿼리를 효율적으로 처리할 수 있음
### Authentication
* Spring Security, JWT 0.12.5
  * 로그인/권한에 따른 엔드포인트 차단 및 보안 처리 및 세션 방식의 서버 부하 문제 발생 가능성 인지 후 JWT 도입으로 상태 비저장(Stateless)으로 구현하기 위해 사용
### Caching
* Redis
  * 인증 과정의 DB 부하 제거 및 조회 속도 향상. Spring Security Context의 UserID를 기반으로 캐시를 조회하는 Look-Aside Cache 전략 적용
### Concurrency
* WebSocket
  * 그룹 스터디 채팅 등 양방향 실시간 대화 기능 구현하기 위해 채택
* SSE(Server-Sent-Events)
  * 알림 기능 한정으로 사용. WebSocket 대비 리소스 및 메모리 비용이 작아 효율적이기 때문에 사용
### Frontend
* Vue3 + Vite, Pinia
  * 백엔드와 분리된 프론트 구현 및 낮은 학습 곡선이기 때문에 채용
  * Pinia를 통해 컴포넌트 간 명확하고 모듈화된 전역 상태 관리 구현
* Bootstrap5

## 기술 성과
### 1. 성능 최적화: Redis 캐싱 및 데이터 접근 효율화
* Redis 캐싱 전략 도입: 사용자 인증 과정(Security Context Load)에서 발생하는 반복적인 DB Read 부하를 줄이기 위해 Redis Cache를 도입했습니다.
  * 성과: 조회 속도를 기존 평균 91 ~ 94ms에서 10ms~14ms 수준으로 약 90% 개선하여 DB 로드를 절감했습니다.
* N+1 문제 해결
  * 매핑 구조 변경: 연관 관계 조회에서 발생하는 일반적인 N+1 문제는 양방향 매핑 대신 단방향 매핑을 사용하도록 설계 구조 자체를 변경하여 해결했습니다.
* 복합 조회 쿼리 작성 및 최적화(JPQL 활용)
  * 단순 매핑 변경으로 해결이 불가능한 복잡한 조회 문제를 해결하기 위해 JPQL을 직접 작성하여, 연관 관계 즉시 로딩 기법과 서브 쿼리를 활용한 복합 쿼리를 구현했습니다.
### 2. 코드 안정성 확보: TDD 및 계층별 테스트
프로젝트의 안정성을 최우선 목표로, TDD(Test-Driven Development)원칙을 도입하여 300 여개의 테스트 코드를 설계 및 작성했습니다.
* 다계층 테스트 전략
  * Service Layer: Repository와 독립된 비즈니스 로직의 성공 및 예외 발생 행위를 검증하여 비즈니스 로직의 신뢰성을 확보했습니다.
  * Controller Layer: HTTP 요청(URI, Method, Parameter)과 Spring Security 기반의 인증/권한 검사를 중심으로 API의 동작을 검증했습니다.
* 인증 및 권한 검증
  * 비로그인 상태에서 인증이 필요한 API에 접근 시 접근 거부가 발생하는지를 명확히 테스트하여 보안 취약점을 사전 방지했습니다.
### 3. 예외 처리 구조
* 커스텀 예외 및 ErrorCode 설계
  *  애플리케이션 전반에 발생하는 모든 비즈니스 예외를 DoubleSApplicationException으로 캡슐화하고, 각 예외 상황에 맞는 커스텀 ErrorCode를 분리하여 사용했습니다.
* 다계층 예외 흐름 관리(ControllerAdvice 활용)
  *  Service Layer에서 예외를 발생시키고, Controller Layer의 Exception Handler를 통해 해당 ErrorCode를 HTTP Status Code 및 표준화된 JSON 응답 본문으로 매핑하여 클라이언트에게 명확하게 전달되도록 구조화했습니다.
### 4. 인증 아키텍처 개선: Refresh Token을 통한 보안 향상
Access Token만을 이용하는 경우, 토큰 유효 기간 동안 탈취된 토큰이 지속적으로 사용될 수 있는 보안 취약점이 존재했습니다.
* 구현 및 효과: 이 문제를 해결하기 위해 Access Token과 Refresh Token을 분리하여 구현했습니다.
  * Access Token: 짧은 유효 기간(Short-lived)을 설정하여 토큰 탈취 위험을 최소화했습니다.
  * Refresh Token: 긴 유효 기간(Long-lived)을 가지며, DB 대신 Redis에 저장하여 상태 관리 및 조회 효율성을을 확보했습니다.
  * 쿠키 보안 속성 최적화
    - HttpOnly 설정을 통해 클라이언트 스크립트(JS) 접근을 차단하여 XSS 공격을 방어했습니다.
    - SameSite=Lax 설정을 통해 CSRF 공격 시 토큰이 전송되지 않도록 방지했습니다.
    - Secure 속성을 추가하여 HTTPS 통신 환경에서만 토큰이 전송되도록 함으로써 네트워크 스니핑 위험을 제거했습니다.
  * Access Token 만료 시 Refresh Token을 이용해 자동 갱신되도록 구현하여 사용자에게 보안 수준을 높이면서 사용자에게는 끊김 없는 인증 세션을 제공할 수 있었습니다.
## 배포 및 접속 정보
* ([koyeb](https://yeasty-kalie-hyeon-7cf77d77.koyeb.app/login)) (1시간 마다 꺼져서 오래걸릴 수 있음)
* ([cloudType](https://port-0-doubles-lyfraywr77b765d8.sel5.cloudtype.app/login)) (안될수도있음)
### 테스트 아이디
* 테스트 ID: tester
* 테스트 Password: 123456
### 어드민 테스트 아이디(공지사항 작성 가능)
* 어드민 테스트 ID: admin
* 어드민 테스트 Password: 123456
