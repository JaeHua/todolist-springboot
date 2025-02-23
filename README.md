# TodoList 后端项目

这是一个基于 Spring Boot 开发的待办事项管理系统后端工程,也是一个简单的用户认证和任务管理的后台入门模板。项目采用 Spring Boot + Spring Security + JWT + Redis + MySQL 等技术栈进行开发。通过这个项目可以了解到 Spring Security 认证授权、JWT Token、Redis 缓存、MySQL 数据库操作、AOP 日志、统一异常处理以及单元测试等企业级开发知识点。

## 技术栈

- Spring Boot 基础
- Spring Security 认证授权
- JWT 原理
- Redis 缓存
- MySQL 数据库
- Docker 容器化
- CI/CD 流程
- 单元测试

### 后端框架
- Spring Boot 3.x
- Spring Security 
- MyBatis-Plus
- Swagger/OpenAPI

### 数据存储
- MySQL 8.0
- Redis 6.2

### 认证授权
- JWT Token
- Spring Security

### 部署和运维
- Docker & Docker Compose
- devops-aliyun
- SonarQube (代码质量)

## 功能特性

### 用户管理
- 用户注册
- 用户登录
- JWT Token 认证
- 用户登出 (Token 黑名单)

### 任务管理
- 创建任务
- 更新任务
- 删除任务
- 查询任务列表
- 任务状态切换

### 系统功能
- Redis 缓存
- AOP 操作日志
- 统一异常处理
- Swagger API 文档
- 单元测试覆盖

## 项目结构
```
src/main/java/com/jaehua/todolist
├── TodolistApplication.java
├── aspect # AOP 切面
│ └── LogAspect.java # 操作日志切面
├── common # 通用类
│ └── Result.java # 统一返回结果
├── config # 配置类
│ ├── RedisConfig.java # Redis配置
│ ├── SecurityConfig.java # 安全配置
│ └── SwaggerConfig.java # API文档配置
├── controller # 控制器
│ └── v1 # API版本控制
├── dto # 数据传输对象
├── entity # 实体类
├── exception # 异常处理
├── mapper # MyBatis mapper
├── security # 安全相关
├── service # 服务层
└── utils # 工具类
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0
- Redis 6.2

### 本地开发

1. 克隆项目
```bash
git clone https://github.com/JaeHua/todolist-springboot.git
cd todolist
```

2. 修改配置
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist
    username: your-username
    password: your-password
  redis:
    host: localhost
    port: 6379
```

3. 运行项目
```bash
mvn spring-boot:run
```

### Docker 部署

1. 构建镜像
```bash
docker build -t todolist:latest .
```

2. 使用 docker-compose 启动
```bash
docker-compose up -d
```

## CI/CD 流程
这里使用的是阿里云的云效，在云效中配置好CI/CD流程，然后就可以在云效中进行构建和部署了
![image](https://github.com/user-attachments/assets/f24817b5-d585-4b82-a8e0-a7543d7147f9)



## API 文档

启动项目后访问: http://localhost:5005/swagger-ui.html
![image](https://github.com/user-attachments/assets/5324c29c-a934-4a53-8824-74b3f4e559e0)


## 测试

### 单元测试
```bash
mvn test
```

### 代码质量检测
```bash
mvn sonar:sonar
```


## 常见问题

1. 启动失败检查清单
   - 数据库连接配置
   - Redis 连接配置
   - 端口占用情况

2. Token 相关问题
   - Token 格式: Bearer {token}
   - Token 有效期: 24小时
   - 刷新机制: Redis 存储

## 参考资料

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Security 官方文档](https://spring.io/projects/spring-security)
- [JWT 介绍](https://jwt.io/introduction)
- [Redis 官方文档](https://redis.io/documentation)
- [Docker 官方文档](https://docs.docker.com/)



## 许可证

[MIT](LICENSE)

