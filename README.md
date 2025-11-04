# DoubleS
![image](https://github.com/user-attachments/assets/d00b3129-6ea4-4810-b2f7-d8f2e90a391f)

## 설명
DoubleS는 독학하는 사용자들이 그룹 스터디 및 채팅을 통해 함께 학습하고 정보를 공유하는 커뮤니티 플랫폼입니다.
이 프로젝트는 **최신 버전의 기술 스택(Spring Boot 3.3.1, Java 21, Vue.js 3)**을 활용하여 서비스를 구축하고, 특히 성능 병목 현상 해결 및 안정성 확보에 집중하여 주니어 개발자로서의 문제 해결 능력을 입증하는 것을 목표로 했습니다.

## 개발환경
* IDE
 - Intellij IDEA Ultimate 2023.2.2, VSCode
* Java Version
 - Java 21
* Build Tool
 - Gradle 8.5
* Hosting
 - OCI (MySQL), Redis Server, Koyeb/cloudType (Web Service)

## 기술 스택
* Spring Boot
* Spring Web
* MySQL 8
  * 소규모 프로젝트이고 빠른 성능과 많이 사용해본 RDB이고 무료이기 때문에 채용
* Spring Data JPA
  * 팀프로젝트 시에 MyBatis를 사용했었으나 생산성을 높일 수 있고 유지 보수하기 편하기 때문에 채용
  * 추가적으로 필요한 경우 직접 MyBatis와 비슷하게 Query를 입력하여 커스텀도 가능하기 때문에 장점을 모두 이용할 수 있음
* Spring Security
  * 팀 프로젝트에서는 사용하지 않았지만 로그인 유무나 Http Method, Role에 따라서 쉽게 차단과 같은 보안 처리가 가능
  * JWT를 사용하기 때문에 필터를 커스텀하기 위함
* Spring Boot DevTools
  * 코드 수정시 반영이 바로 될 수 있도록 하기 위해 사용
* Lombok
  * 중복되는 코드들을 줄이기 위해서 사용
* JWT 0.12.5
  * 이전 팀 프로젝트에서는 세션에 ID를 저장하여 인증하는 방식 사용했으나 서버의 부하가 걸릴 수있음을 인지
* WebSocket
  * 그룹 스터디등 사용자와 사용자끼리 대화할 수 있는 기능이 필요함을 느낌
  * 이를 해결하기 위해서 채팅 기능이 가능하도록 WebSocket 사용
* SSE(Server-Sent-Events)
  * 알림 기능을 사용시 WebSocket과 같이 양방향 통신은 필요없기 때문에 SSE로 채용
  * 추가적으로 Spring에서 SseEmitter를 제공하기도 하고 메모리나 리소스 비용이 더 작아서 알림 한정으로 사용
* Redis
  * 첫 페이지 이후에 모든 페이지에서 인증을 사용하므로 중복되는 인증이 많아지고 쿼리가 1개씩 추가됨
  * 이를 해결하기 위해서 Redis를 사용해 인증시 db부하를 줄이고 조회 속도를 높이기 위해서 사용
* Vue3 + Vite
  * 프론트와 백을 나누고 싶었고 추가적으로 런닝 커프가 낮기 때문에 사용
* Pinia
  * 컴포넌트/페이지 간 state를 공유하기 위해서 사용 

## 서버
* OCI(Oracle Cloud Infrastructure) -> MySQL 서버
* Redis -> Redis 서버
* Koyeb -> 웹 서비스 서버 (리전 위치가 멀어서 지연이 좀 길음. 좋은 서버 찾게되면 변경할 가능성이 있음)
* cloudType -> 매일 꺼지기 때문에 다시 켜줘야 함

## 그외
* Bootstrap5

## 코드 안정성 및 유지보수성
* ORM 전략
  - 생산성을 위해 Spring Data JPA를 주력으로 채택했습니다. MyBatis 경험을 바탕으로, 필요 시 Repository 커스텀 및 직접 SQL 작성을 통해 성능 최적화가 필요한 쿼리를 효율적으로 처리할 수 있습니다.
* TDD 원칙 적용
  - 코드 작성 전 TDD의 설계 원칙을 학습하고 적용하여 기능별 로직을 구조화. 실제 테스트 코드 작성 대신, JPQL 최적화와 계층별 명확한 예외 처리에 집중하여 서비스 안정성을 확보. (ex. 비즈니스 예외, 데이터 접근 예외 분리)
* Lombok:
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
