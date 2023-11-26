# jdk17 Image Start
FROM openjdk:17

# WORKDIR /app

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} dpmback.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "dpmback.jar"]
