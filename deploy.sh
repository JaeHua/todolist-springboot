#!/bin/bash

# 停止并删除旧容器
docker-compose down

# Maven 构建
./mvnw clean package -DskipTests

# 构建新镜像并启动容器
docker-compose up --build -d

# 查看日志
docker-compose logs -f 