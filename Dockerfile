# jdk17 Image Start
FROM openjdk:17

WORKDIR /app

EXPOSE 80 443

# 인자 설정 - JAR_File
ARG JAR_FILE=server-api/build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} dpmback.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=develop", "dpmback.jar"]

# jdk17 Image Start
#FROM openjdk:17 as builder
#
#EXPOSE 80 443
#
#WORKDIR /app
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} ./app.jar
#RUN java -Djarmode=layertools -jar ./app.jar extract
#
#FROM openjdk:17.0.2
#WORKDIR /application
#COPY --from=builder /app/dependencies ./
#COPY --from=builder /app/spring-boot-loader ./
#COPY --from=builder /app/snapshot-dependencies ./
#COPY --from=builder /app/application ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
