# Use the latest eclipse-temurin image as the base
FROM eclipse-temurin:24

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build context into the container
COPY ./target/gp19CourseWork.jar /app/

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "gp19CourseWork.jar", "db:3306", "30000"]
