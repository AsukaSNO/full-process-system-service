# 全流程系统服务 (Full Process System Service)

## 项目简介

这是一个基于Spring Boot 3.x的全流程系统服务后端应用，使用Java 21和Tomcat 10构建，支持前后端分离架构。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Tomcat**: 10.x (内嵌)
- **Maven**: 构建工具
- **Jackson**: JSON处理
- **Spring Boot Actuator**: 健康检查和监控

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── FullProcessSystemServiceApplication.java  # 主启动类
│   │           ├── controller/
│   │           │   └── HealthController.java                 # 健康检查控制器
│   │           ├── model/
│   │           │   ├── HealthResponse.java                   # 健康检查响应模型
│   │           │   └── ErrorResponse.java                    # 错误响应模型
│   │           ├── config/
│   │           │   └── CorsConfig.java                       # CORS配置
│   │           └── exception/
│   │               └── GlobalExceptionHandler.java           # 全局异常处理器
│   └── resources/
│       └── application.yml                                   # 应用配置文件
└── test/
    └── java/
        └── com/
            └── example/
                ├── FullProcessSystemServiceApplicationTests.java  # 应用测试
                └── controller/
                    └── HealthControllerTest.java                 # 控制器测试
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 构建和运行

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd fullProcessSystemService
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

4. **打包应用**
   ```bash
   mvn clean package
   ```

5. **运行JAR包**
   ```bash
   java -jar target/full-process-system-service-1.0.0.jar
   ```

## API接口

### 健康检查接口

- **URL**: `GET /api/health/checkHealth`
- **描述**: 检查系统健康状态
- **响应示例**:
  ```json
  {
    "status": "UP",
    "message": "系统运行正常",
    "timestamp": "2024-01-01 12:00:00",
    "version": "1.0.0"
  }
  ```

### 其他监控接口

- **健康检查**: `GET /actuator/health`
- **应用信息**: `GET /actuator/info`
- **指标信息**: `GET /actuator/metrics`

## 配置说明

### 应用配置 (application.yml)

- **服务器端口**: 8080
- **应用名称**: full-process-system-service
- **时区**: GMT+8
- **日志级别**: DEBUG (com.example包)
- **CORS**: 支持跨域请求

### 主要特性

1. **前后端分离**: 配置了CORS支持，允许前端应用跨域访问
2. **健康检查**: 提供自定义健康检查接口和Spring Boot Actuator健康检查
3. **异常处理**: 全局异常处理器，统一处理API异常
4. **日志记录**: 配置了详细的日志记录
5. **监控支持**: 集成Spring Boot Actuator提供监控端点

## 开发指南

### 添加新的API接口

1. 在`controller`包下创建新的控制器类
2. 在`model`包下创建相应的数据模型类
3. 添加相应的测试类
4. 更新API文档

### 测试

运行所有测试：
```bash
mvn test
```

运行特定测试：
```bash
mvn test -Dtest=HealthControllerTest
```

## 部署

### Docker部署

1. 构建Docker镜像：
   ```bash
   docker build -t full-process-system-service .
   ```

2. 运行容器：
   ```bash
   docker run -p 8080:8080 full-process-system-service
   ```

### 生产环境配置

建议在生产环境中：
- 修改日志级别为INFO或WARN
- 配置适当的数据库连接
- 设置安全配置
- 配置监控和告警

## 许可证

本项目采用MIT许可证。

## 联系方式

如有问题或建议，请联系开发团队。 