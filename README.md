# 全流程系统服务 (Full Process System Service)

## 项目简介
这是一个基于Spring Boot + MyBatis-Plus的全流程系统服务项目。

## 技术栈
- Java 21
- Spring Boot 3.2.0
- MyBatis-Plus 3.5.4.1
- MySQL 8.0
- Maven

## 项目结构
```
full-process-system-service/
├── common/          # 公共模块
├── dal/            # 数据访问层模块
│   ├── entity/     # 实体类
│   ├── mapper/     # Mapper接口
│   └── service/    # 服务接口和实现
├── service/        # 业务服务模块
├── web/           # Web控制器模块
└── database/      # 数据库脚本
```

## 快速开始

### 1. 环境要求
- JDK 21+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE `fulldb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p < database/init.sql
```

3. 修改数据库连接配置（web/src/main/resources/application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fulldb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    loginName: your_username
    password: your_password
```

### 3. 启动项目
```bash
# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run -pl web
```

### 4. 测试API
项目启动后，可以通过以下接口测试：

#### 认证接口（无需token）
- 用户注册：`POST http://localhost:8081/api/auth/register`
- 用户登录：`POST http://localhost:8081/api/auth/login`
- 刷新Token：`POST http://localhost:8081/api/auth/refresh`

#### 用户管理接口（需要token）
- 获取所有用户：`GET http://localhost:8081/api/users`
- 分页查询：`GET http://localhost:8081/api/users/page?current=1&size=10`
- 根据ID查询：`GET http://localhost:8081/api/users/1`
- 创建用户：`POST http://localhost:8081/api/users`
- 更新用户：`PUT http://localhost:8081/api/users/1`
- 删除用户：`DELETE http://localhost:8081/api/users/1`

#### 用户认证接口（需要token）
- 获取用户信息：`GET http://localhost:8081/api/auth/profile`
- 用户登出：`POST http://localhost:8081/api/auth/logout`

**注意：** 需要认证的接口请在请求头中添加：`Authorization: Bearer {token}`

## 主要功能

### 用户管理
- 用户的增删改查
- 分页查询
- 条件搜索（按昵称）

### 用户认证
- 用户注册和登录
- Token认证（有效期30天）
- 自动刷新Token
- 会话管理
- 密码加密存储（MD5+盐值）

### MyBatis-Plus特性
- 通用CRUD操作
- 分页插件
- 条件构造器
- 自动填充
- 逻辑删除

## 配置说明

### MyBatis-Plus配置
- 开启驼峰命名转换
- 配置分页插件
- 支持逻辑删除
- 自动类型转换

### 数据库配置
- 支持UTF-8编码
- 配置时区为Asia/Shanghai
- 禁用SSL连接

## 开发说明

### 添加新的实体类
1. 在`dal/src/main/java/group/kiseki/dal/entity/`下创建实体类
2. 在`dal/src/main/java/group/kiseki/dal/mapper/`下创建Mapper接口
3. 在`dal/src/main/java/group/kiseki/dal/service/`下创建Service接口和实现
4. 在`web/src/main/java/group/kiseki/controller/`下创建Controller

### 实体类注解说明
- `@TableName`: 指定表名
- `@TableId`: 指定主键字段和类型
- `@TableField`: 指定字段映射（可选）

## 注意事项
1. 确保MySQL服务已启动
2. 检查数据库连接配置是否正确
3. 首次运行需要执行数据库初始化脚本
4. 默认端口为8081，可在application.yml中修改

## 许可证
MIT License 