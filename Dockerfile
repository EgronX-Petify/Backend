# --------- BUILD STAGE ----------
FROM eclipse-temurin:24-jdk AS build

WORKDIR /app

# Copy only the necessary Maven files to leverage Docker cache
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/

# Download dependencies (cached if pom.xml hasn't changed)
RUN ./mvnw dependency:go-offline -B

# Now copy the source code
COPY src ./src

# Build application JAR
RUN ./mvnw clean package -DskipTests

# --------- RUNTIME STAGE ----------
FROM eclipse-temurin:24-jre

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
