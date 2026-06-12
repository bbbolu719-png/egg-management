<<<<<<< HEAD
# egg-management
鸡蛋批发管理系统
=======

# 鸡蛋批发管理系统 — 后端

Spring Boot 3 + MyBatis-Plus 构建的 RESTful API 服务，提供鸡蛋批发业务的核心数据接口。

## 技术栈

| 类别         | 工具                           |
|--------------|--------------------------------|
| 框架         | Spring Boot 3.2.5              |
| ORM          | MyBatis-Plus 3.5.6             |
| 数据库       | MySQL 8                        |
| Java 版本    | 17                             |
| 构建工具     | Maven                          |

## 快速开始

### 前提条件

- JDK 17+
- MySQL 8
- Maven 3.8+

### 数据库初始化

MySQL 中创建数据库，应用启动时会自动执行 `schema.sql` 和 `data.sql`：

```sql
CREATE DATABASE IF NOT EXISTS egg_management
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

默认连接配置在 `application.yml` 中，可根据环境调整。

### 启动

```bash
# 开发模式（内嵌 Tomcat 热启动）
mvn spring-boot:run

# 打包
mvn package -DskipTests

# 运行 jar
java -jar target/egg-management-1.0.0.jar
```

服务启动后监听 `http://localhost:8081`，API 路径以 `/api/` 为前缀。

## 目录结构

```
server-java/
├── src/
│   ├── main/
│   │   ├── java/com/egg/management/
│   │   │   ├── common/        # 通用响应、异常处理
│   │   │   ├── config/        # CORS、MyBatis-Plus 配置
│   │   │   ├── controller/    # REST 控制器
│   │   │   ├── dto/           # 请求数据传输对象
│   │   │   ├── entity/        # 数据库实体
│   │   │   ├── mapper/        # MyBatis-Plus Mapper 接口
│   │   │   ├── service/       # 业务逻辑层
│   │   │   └── EggManagementApplication.java
│   │   └── resources/
│   │       ├── mapper/        # XML Mapper 文件
│   │       ├── application.yml
│   │       ├── schema.sql
│   │       └── data.sql
│   └── test/
├── pom.xml
└── .gitignore
```

## 相关仓库

前端：`client/` Vue 3 + Vite SPA
///egg-management-client

## HTTP API 规范

- 基础路径：`/api/`
- 请求/响应格式：`application/json`
- 统一响应结构：`ApiResponse<T>`
- 分页：使用 MyBatis-Plus `Page` 分页参数

## 许可证

MIT
>>>>>>> 7815dc4 (feat:init project)
