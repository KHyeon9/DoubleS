# 1단계: 프론트엔드 빌더 (Frontend Builder Stage)
# 프론트엔드 빌드에는 Node.js가 필요하므로 node 기반 이미지를 사용합니다.
FROM node:20.9-alpine AS frontend_builder
WORKDIR /app/frontend

# Node.js 패키지 설치
COPY frontend/package*.json ./
RUN npm install

# 프론트엔드 빌드 실행
COPY frontend/ .
RUN npm run build

# 2단계: 백엔드 빌더 (Backend Builder Stage)
# 백엔드 빌드에는 JDK가 필요합니다.
FROM eclipse-temurin:21-jdk-jammy AS backend_builder
WORKDIR /app

# 프론트엔드 빌드 결과물 복사
COPY --from=frontend_builder /app/frontend/dist /app/src/main/resources/static
# 소스 코드 복사
COPY . .

# Gradle 권한 부여 및 bootJar 빌드 실행 (테스트 제외)
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

# 3단계: 최종 실행 이미지 (Final Run Stage)
# 실행 환경은 경량화된 JRE 이미지를 사용합니다.
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 2단계에서 빌드된 JAR 파일 복사
# 프로젝트 구조에 따라 JAR 파일명(app.jar)을 확인하여 수정하세요.
COPY --from=backend_builder /app/build/libs/*.jar app.jar

# 최종 실행 명령어는 Docker Compose에서 오버라이드할 예정이지만, 기본 정의를 유지합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]