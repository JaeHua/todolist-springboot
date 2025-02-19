# **TodoList 系统工程化开发全流程指导**

## **1. 需求分析**

在开发一个 **TodoList** 系统时，我们需要明确核心功能，并确保架构设计符合业务需求。该系统主要提供 **用户管理**（注册、登录、登出）和 **任务管理**（增删改查）功能，采用 **JWT 进行认证**，并结合 **MySQL 和 Redis** 进行存储和优化。

### **1.1 功能需求**

- 用户管理
  - 用户注册（支持用户名、邮箱）
  - 用户登录（支持 JWT 认证）
  - 用户登出（清除 Redis 缓存的 Token）
- 任务管理
  - 创建任务（标题、描述、状态）
  - 查询任务（支持分页、筛选）
  - 修改任务（修改任务状态、内容）
  - 删除任务（逻辑删除）
- 权限管理
  - 仅允许用户操作自己的任务
- 日志管理
  - 记录用户操作日志
  - 记录系统错误日志
- 缓存优化
  - Redis 存储 Token
  - Redis 缓存查询数据

------

## **2. 技术架构**

**采用 Spring Boot 进行开发，技术栈如下：**

| 技术                      | 说明                       |
| ------------------------- | -------------------------- |
| **Spring Boot**           | 轻量级 Java Web 框架       |
| **Spring Security + JWT** | 认证与授权                 |
| **MyBatis-Plus**          | ORM 框架，简化数据库操作   |
| **MySQL**                 | 关系型数据库，存储任务数据 |
| **Redis**                 | 缓存 Token 及查询数据      |
| **Lombok**                | 简化 Java 代码             |
| **Swagger**               | API 文档生成               |
| **Logback**               | 记录日志                   |
| **Docker**                | 容器化部署                 |

------

## **3. 系统架构设计**

采用 **分层架构（Controller-Service-Mapper）**，并遵循 **RESTful API 设计**。

```plaintext
┌──────────────┐
│  用户前端    │
└──────────────┘
        │
        ▼
┌─────────────────────┐
│  Spring Boot 后端   │
├─────────────────────┤
│  Controller 层 (Restful API)  │
│  Service 层 (业务逻辑)        │
│  Mapper 层 (数据库交互)       │
├─────────────────────┤
│  MySQL  (存储核心数据)  │
│  Redis  (缓存 & Token) │
└─────────────────────┘
```

------

## **4. 数据库设计**

**MySQL 采用 InnoDB 存储引擎，支持事务和外键。**

### **4.1 用户表**

| 字段       | 类型         | 说明           |
| ---------- | ------------ | -------------- |
| id         | BIGINT       | 主键，自增     |
| username   | VARCHAR(50)  | 唯一，用户名   |
| password   | VARCHAR(255) | 加密存储的密码 |
| email      | VARCHAR(100) | 唯一，用户邮箱 |
| created_at | TIMESTAMP    | 注册时间       |

### **4.2 任务表**

| 字段        | 类型         | 说明                 |
| ----------- | ------------ | -------------------- |
| id          | BIGINT       | 主键                 |
| user_id     | BIGINT       | 关联用户 ID          |
| title       | VARCHAR(255) | 任务标题             |
| description | TEXT         | 任务描述             |
| status      | TINYINT      | 0: 未完成, 1: 已完成 |
| created_at  | TIMESTAMP    | 任务创建时间         |
| updated_at  | TIMESTAMP    | 任务更新时间         |

------

## **5. API 设计（RESTful 风格）**

**遵循 RESTful 规范，使用 HTTP 方法区分操作：**

| API                  | 方法     | 描述               |
| -------------------- | -------- | ------------------ |
| `/api/auth/register` | `POST`   | 用户注册           |
| `/api/auth/login`    | `POST`   | 用户登录，返回 JWT |
| `/api/auth/logout`   | `POST`   | 用户登出           |
| `/api/tasks`         | `POST`   | 创建任务           |
| `/api/tasks`         | `GET`    | 查询任务（分页）   |
| `/api/tasks/{id}`    | `PUT`    | 更新任务           |
| `/api/tasks/{id}`    | `DELETE` | 删除任务           |

**状态码设计**

| 状态码                    | 说明         |
| ------------------------- | ------------ |
| 200 OK                    | 请求成功     |
| 201 Created               | 资源创建成功 |
| 400 Bad Request           | 请求参数错误 |
| 401 Unauthorized          | 认证失败     |
| 403 Forbidden             | 无权限       |
| 404 Not Found             | 资源不存在   |
| 500 Internal Server Error | 服务器错误   |

------

## **6. 认证与安全**

采用 **JWT（JSON Web Token）** 进行身份认证：

- **用户登录时**，后端生成 JWT 并返回给前端。
- **用户请求 API 时**，前端在 `Authorization` 头中携带 `Bearer Token` 进行身份验证。
- **Redis 存储 Token**，支持 Token 失效处理。

------

## **7. Redis 缓存优化**

- 存储 Token
  - `TOKEN_{username}` → `JWT`
  - 失效时间：1 天
- 缓存任务查询
  - `TASK_LIST_{userId}` → `任务列表`
  - 失效时间：5 分钟

------

## **8. 日志管理**

采用 **Logback** 记录日志，日志类型：

1. **访问日志**（用户操作记录）
2. **错误日志**（系统异常记录）

日志格式：

```plaintext
2025-02-18 10:00:00 [INFO] User: admin 登录成功
2025-02-18 10:05:30 [ERROR] 任务创建失败: 数据库异常
```

------

## **9. 单元测试**

使用 **JUnit + Mockito** 进行单元测试：

- **用户注册测试**
- **用户登录测试**
- **任务创建测试**
- **任务查询测试**

------

## **10. 部署**

### **10.1 Docker 容器化**

编写 `Dockerfile`：

```dockerfile
FROM openjdk:17
COPY target/todolist.jar /app/todolist.jar
WORKDIR /app
CMD ["java", "-jar", "todolist.jar"]
```

构建并运行：

```sh
docker build -t todolist-app .
docker run -d -p 8080:8080 todolist-app
```

### **10.2 Nginx 反向代理**

编写 `nginx.conf`：

```nginx
server {
    listen 80;
    location / {
        proxy_pass http://localhost:8080;
    }
}
```

启动 Nginx：

```sh
docker run -d -p 80:80 -v $(pwd)/nginx.conf:/etc/nginx/nginx.conf nginx
```

------

## **11. 结论**

### **11.1 规范性**

- **工程化管理**：清晰的分层架构、日志、异常处理。
- **RESTful 设计**：符合业界标准的 API 规范。
- **状态码响应**：提供合理的 HTTP 状态码反馈。

### **11.2 安全性**

- **JWT 认证**：确保用户身份安全。
- **Redis Token 管理**：支持 Token 失效控制。
- **权限控制**：仅允许用户操作自己的数据。

### **11.3 高性能**

- **MySQL 存储数据**：保证数据一致性。
- **Redis 缓存查询**：提升查询效率。
- **Docker 部署**：支持容器化部署，便于扩展。

------

## **12. 总结**

本项目采用 **Spring Boot + MyBatis-Plus + JWT + Redis** 进行开发，符合 **RESTful 规范**，具备 **完整的增删改查 + 登录注册** 功能，并支持 **Redis 缓存、JWT 认证、日志管理**，是一个 **工程化最佳实践**。

🚀 **完整的 TodoList 系统，适用于实际生产环境！**

## **13. 技术实现说明**

### **13.1 认证流程**

1. **JWT认证实现**
   - 使用`jjwt`库实现JWT的生成和验证
   - JWT包含用户名信息和过期时间
   - 通过`JwtAuthenticationFilter`拦截请求进行token验证
   - 使用`SecurityContextHolder`存储认证信息

2. **Spring Security配置**
   - 配置无需认证的路径：`/api/auth/**`、Swagger文档路径
   - 使用`BCryptPasswordEncoder`进行密码加密
   - 实现`UserDetailsService`加载用户信息
   - 采用无状态会话管理（STATELESS）

### **13.2 数据访问层**

1. **MyBatis-Plus使用**
   - 继承`BaseMapper`实现基础CRUD操作
   - 使用`@Select`注解编写自定义SQL
   - 实现分页查询功能
   - 支持逻辑删除

2. **Redis缓存设计**
   - 使用`StringRedisSerializer`作为序列化器
   - `RedisTemplate`封装Redis操作
   - 提供key-value操作和过期时间设置
   - 用于存储Token和缓存查询结果

### **13.3 业务层实现**

1. **用户管理**
   - 注册时进行用户名和邮箱唯一性校验
   - 密码加密存储
   - 登录成功后生成JWT
   - 登出时清除SecurityContext

2. **任务管理**
   - 创建任务时自动关联当前用户
   - 任务更新时自动更新时间戳
   - 查询任务时进行权限验证
   - 支持分页查询和状态筛选

### **13.4 安全特性**

1. **权限控制**
   - 基于用户ID的数据隔离
   - 任务操作权限验证
   - JWT token验证
   - 密码加密存储

2. **异常处理**
   - 统一异常处理器`GlobalExceptionHandler`
   - 业务异常`BusinessException`
   - 认证异常处理
   - 参数校验异常处理

### **13.5 项目亮点**

1. **工程化规范**
   - 分层架构清晰
   - 统一响应格式
   - 全局异常处理
   - 参数校验

2. **性能优化**
   - Redis缓存支持
   - MyBatis-Plus分页
   - 密码加密
   - JWT无状态认证

3. **代码质量**
   - Lombok简化代码
   - 统一的命名规范
   - 完善的注释
   - 模块化设计

这个项目展示了一个完整的Spring Boot后端应用开发流程，包含了常见的企业级应用功能和最佳实践。通过分层架构和模块化设计，代码具有良好的可维护性和扩展性。同时，项目集成了主流的技术框架，展示了如何在实际项目中应用这些技术。

## **14. 代码实现顺序与解析**

### **14.1 项目基础结构**

1. **项目配置文件**
   - `pom.xml`: Maven项目配置，定义项目依赖
   - `application.yml`: Spring Boot配置文件，包含数据库、Redis等配置
   - `TodoListApplication.java`: 应用程序入口类

2. **数据库设计**
   - `schema.sql`: 数据库表结构定义
   ```sql
   - user表: 用户信息
   - task表: 任务信息，与user表关联
   ```

### **14.2 代码层次结构**

按照依赖关系从底层到上层：

1. **实体层（Entity）**
   - `User.java`: 用户实体
   - `Task.java`: 任务实体
   ```java
   @Data
   @TableName("user")
   public class User {
       @TableId(type = IdType.AUTO)
       private Long id;
       // ...
   }
   ```

2. **数据访问层（Mapper）**
   - `UserMapper.java`: 用户数据访问接口
   - `TaskMapper.java`: 任务数据访问接口
   ```java
   @Mapper
   public interface UserMapper extends BaseMapper<User> {
       User findByUsername(String username);
       // ...
   }
   ```

3. **DTO层（数据传输对象）**
   - `LoginRequest.java`: 登录请求对象
   - `RegisterRequest.java`: 注册请求对象
   - `TaskRequest.java`: 任务请求对象

4. **通用组件**
   - `Result.java`: 统一响应结果封装
   - `BusinessException.java`: 业务异常定义
   - `GlobalExceptionHandler.java`: 全局异常处理

5. **配置类**
   - `SecurityConfig.java`: Spring Security配置
   - `JwtConfig.java`: JWT配置
   - `RedisConfig.java`: Redis配置
   - `MybatisPlusConfig.java`: MyBatis-Plus配置
   - `SwaggerConfig.java`: Swagger文档配置
   - `WebConfig.java`: Web相关配置（如跨域）

6. **工具类**
   - `JwtUtil.java`: JWT工具类
   - `RedisUtil.java`: Redis工具类

7. **安全相关**
   - `UserDetailsImpl.java`: Spring Security用户详情实现
   - `UserDetailsServiceImpl.java`: 用户详情服务实现
   - `JwtAuthenticationFilter.java`: JWT认证过滤器

8. **服务层（Service）**
   - `AuthService.java`: 认证服务接口
   - `AuthServiceImpl.java`: 认证服务实现
   - `TaskService.java`: 任务服务接口
   - `TaskServiceImpl.java`: 任务服务实现

9. **控制层（Controller）**
   - `AuthController.java`: 认证控制器
   - `TaskController.java`: 任务控制器

### **14.3 请求流程示例**

以创建任务为例：

1. **请求入口**
   ```java
   @PostMapping
   public Result<Task> createTask(@Valid @RequestBody TaskRequest request)
   ```

2. **认证过滤**
   - `JwtAuthenticationFilter` 验证token
   - 解析用户信息并设置到 SecurityContext

3. **业务处理**
   - Controller接收请求并验证
   - 调用Service层处理业务逻辑
   - Service层调用Mapper层操作数据库
   - 返回统一格式的响应结果

4. **异常处理**
   - 由 `GlobalExceptionHandler` 统一处理异常

### **14.4 关键技术点**

1. **认证流程**
   - 基于JWT的无状态认证
   - Spring Security集成
   - 用户认证信息缓存

2. **数据访问**
   - MyBatis-Plus提供基础CRUD
   - 自定义SQL处理复杂查询
   - 分页查询支持

3. **缓存处理**
   - Redis缓存token
   - 缓存常用数据
   - 缓存过期策略

4. **安全特性**
   - 密码加密存储
   - Token认证
   - 跨域配置
   - 权限控制

### **14.5 测试流程**

1. 启动项目
2. 访问 Swagger 文档：`http://localhost:8080/swagger-ui.html`
3. 注册用户
4. 登录获取token
5. 使用token访问任务相关接口

这个顺序可以帮助你更好地理解代码结构和实现流程。建议按照这个顺序阅读代码，从底层到上层，从基础设施到业务逻辑。