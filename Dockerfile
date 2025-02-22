# 构建阶段
FROM maven:3.8.4-openjdk-17 as builder

# 设置工作目录
WORKDIR /build

# 复制 pom.xml 和源代码
COPY pom.xml .
COPY src ./src

# 执行构建
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim

WORKDIR /app

# 从构建阶段复制构建好的 jar 文件
COPY --from=builder /build/target/*.jar app.jar

# 暴露应用端口
EXPOSE 5005

# 设置 JVM 参数
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 