# --------- BUILD STAGE ----------
FROM eclipse-temurin:24-jdk AS build

WORKDIR /app

# Copy Maven wrapper + pom.xml first for caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build application JAR
RUN ./mvnw clean package -DskipTests

# --------- RUNTIME STAGE ----------
FROM eclipse-temurin:24-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
