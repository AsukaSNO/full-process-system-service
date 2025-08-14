# 系统架构说明

## 重构概述

本次重构将原有的认证逻辑从Controller层下沉到Service模块，实现了更清晰的分层架构和职责分离。

## 架构层次

### 1. 数据访问层（DAL）
**位置**: `dal/` 模块
**职责**: 数据持久化和基础CRUD操作

#### 实体类（Entity）
- `UserInfo`: 用户基本信息
- `UserAuth`: 用户认证信息
- `UserSession`: 用户会话信息

#### 数据访问接口（Mapper）
- `UserInfoMapper`: 用户信息数据访问
- `UserAuthMapper`: 用户认证数据访问
- `UserSessionMapper`: 用户会话数据访问

#### 基础服务（Service）
- `UserInfoService`: 用户信息基础服务
- 继承MyBatis-Plus的IService，提供通用CRUD方法

### 2. 业务服务层（Service）
**位置**: `service/` 模块
**职责**: 业务逻辑处理和业务流程编排

#### 认证模块（auth）
```
service/src/main/java/group/kiseki/auth/
├── dto/                    # 数据传输对象
│   ├── UserDTO.java       # 用户注册DTO
│   ├── LoginRequest.java  # 登录请求DTO
│   ├── LoginResult.java   # 登录结果DTO
│   └── RefreshTokenRequest.java # 刷新Token请求DTO
├── service/               # 业务服务
│   ├── AuthService.java   # 认证服务接口
│   └── impl/
│       └── AuthServiceImpl.java # 认证服务实现
└── util/                  # 工具类
    └── HttpUtil.java      # HTTP工具类
```

#### 核心业务逻辑
- 用户注册：密码加密、用户信息保存、认证信息创建
- 用户登录：密码验证、Token生成、会话管理
- Token管理：验证、刷新、登出
- 会话管理：创建、更新、清理

### 3. 控制层（Web）
**位置**: `web/` 模块
**职责**: 请求处理、参数验证、响应封装

#### 控制器（Controller）
- `AuthController`: 认证相关接口
  - 只负责参数接收和响应封装
  - 业务逻辑委托给Service层处理

#### 拦截器（Interceptor）
- `AuthInterceptor`: Token验证拦截器
- 自动验证需要认证的接口

#### 配置（Config）
- `WebConfig`: Web配置，注册拦截器
- `MybatisPlusConfig`: MyBatis-Plus配置

## 数据流

```
HTTP请求 → Controller → Service → DAL → 数据库
    ↓
HTTP响应 ← Controller ← Service ← DAL ← 数据库
```

## 关键特性

### 1. 分层清晰
- **Controller层**: 只处理HTTP请求/响应
- **Service层**: 包含所有业务逻辑
- **DAL层**: 专注于数据访问

### 2. 职责单一
- 每个类都有明确的职责
- 避免跨层调用
- 便于测试和维护

### 3. 依赖注入
- 使用Spring的依赖注入
- 松耦合设计
- 便于单元测试

### 4. 事务管理
- 在Service层使用`@Transactional`
- 确保数据一致性

## 扩展指南

### 添加新功能模块
1. 在`service`模块下创建新的包结构
2. 定义DTO类进行数据传输
3. 实现Service接口和实现类
4. 在Controller中调用Service方法

### 添加新的实体
1. 在`dal/entity`下创建实体类
2. 在`dal/mapper`下创建Mapper接口
3. 在`dal/service`下创建基础Service

## 测试策略

### 单元测试
- Service层：测试业务逻辑
- Util类：测试工具方法
- DTO类：测试数据转换

### 集成测试
- Controller层：测试接口行为
- 数据库操作：测试数据持久化

## 部署说明

### 依赖关系
```
web → service → dal
```

### 启动顺序
1. 确保数据库服务运行
2. 执行数据库初始化脚本
3. 启动web模块（会自动加载service和dal）

## 注意事项

1. **包名规范**: 严格按照分层架构组织包结构
2. **异常处理**: 在Service层统一处理业务异常
3. **日志记录**: 在关键业务节点添加日志
4. **参数验证**: 在Controller层进行基础参数验证
5. **事务边界**: 合理设置事务边界，避免长事务
