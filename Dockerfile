# Använd officiell Java 17 image
FROM eclipse-temurin:21-jdk-alpine

# Skapa app-mapp
WORKDIR /app

# Kopiera Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Kopiera pom.xml
COPY pom.xml .

# Kopiera källkod
COPY src src

# Bygg applikationen
RUN ./mvnw clean package -DskipTests

# Exportera port
EXPOSE 9091

# Kör applikationen
ENTRYPOINT ["java", "-jar", "target/*.jar"]