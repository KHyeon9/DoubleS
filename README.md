# DoubleS
![image](https://github.com/user-attachments/assets/d00b3129-6ea4-4810-b2f7-d8f2e90a391f)

## 설명
독학하는 사람들을 위한 커뮤니티를 주제로 그룹 스터디, 채팅 기능을 넣어 커뮤니티를 만들어 봤습니다.

## 개발환경
* Intellij IDEA Ultimate 2023.2.2
* VSCode
* Java 21
* Gradle 8.5
* Spring Boot 3.3.1

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
  * 또한 Vue를 사용하면서 프론트와 백을 분리했기 때문에 CORS 문제를 해결하기 위해서 사용
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

## 주소
* ([koyeb](https://yeasty-kalie-hyeon-7cf77d77.koyeb.app/login)) (1시간 마다 꺼져서 오래걸릴 수 있음)
* ([cloudType](https://port-0-doubles-lyfraywr77b765d8.sel5.cloudtype.app/login)) (안될수도있음)

### 테스트 아이디
* 테스트 ID: tester
* 테스트 Password: 123456
### 어드민 테스트 아이디(공지사항 작성 가능)
* 어드민 테스트 ID: admin
* 어드민 테스트 Password: 123456
