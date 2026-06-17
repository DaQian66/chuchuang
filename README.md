# 🏪 网上衣橱管理系统 (Online Wardrobe Management System)

一个完整的网上衣橱电商平台，包含用户购物端和管理后台端。用户可以浏览服装、添加到购物车、下单购买；管理员可以管理服装、订单和用户。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
  - [用户端功能](#用户端功能)
  - [管理端功能](#管理端功能)
- [环境要求](#环境要求)
- [快速启动](#快速启动)
  - [1. 数据库初始化](#1-数据库初始化)
  - [2. 后端配置与启动](#2-后端配置与启动)
  - [3. 前端配置与启动](#3-前端配置与启动)
  - [4. 一键启动](#4-一键启动)
- [API 接口文档](#api-接口文档)
- [数据库设计](#数据库设计)
- [常见问题](#常见问题)

---

## 项目简介

网上衣橱管理系统是一个基于 Java Servlet + Vue 3 的 Web 应用项目，采用前后端分离架构。系统分为三个核心模块：

| 模块 | 说明 | 技术 |
|------|------|------|
| `wardrobe_back` | 后端接口服务 | Java Servlet + Maven + PostgreSQL |
| `wardrobe_user` | 用户购物端 | Vue 3 + Vite + Element Plus |
| `wardrobe_admin` | 管理员后台端 | Vue 3 + Vite + Element Plus |

**默认账号：**
- 管理员：`admin` / `admin123`
- 普通用户：`testuser` / `123456`

---

## 技术栈

### 后端 (wardrobe_back)

| 技术 | 用途 |
|------|------|
| Java 8+ | 主要编程语言 |
| Jakarta Servlet | Web 服务框架 |
| Maven | 项目构建与依赖管理 |
| PostgreSQL | 关系型数据库 |
| BCrypt (jBCrypt) | 密码加密 |
| JSON (Jackson) | 数据序列化 |
| JWT (io.jsonwebtoken) | 用户认证令牌 |

### 前端 (wardrobe_admin / wardrobe_user)

| 技术 | 用途 |
|------|------|
| Vue 3 (Composition API) | 前端框架 |
| Vite 6 | 构建工具 |
| Vue Router 4 | 前端路由 |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求 |

---

## 项目结构

```
实验8/                              # 项目根目录
│
├── README.md                       # 项目说明文档
├── .gitignore                      # Git 忽略配置
├── wardrobe_schema.sql             # PostgreSQL 数据库建表 + 初始数据
│
├── 启动脚本/                        # 🔧 一键启动脚本目录
│   ├── start-all.bat               #   Windows 批处理启动（需要 Node.js）
│   ├── start-all.ps1               #   PowerShell 启动（自动安装依赖）
│   └── start-all.vbs               #   VBS 后台静默启动（双击即可）
│
├── wardrobe_back/                  # ☕ Java 后端
│   ├── pom.xml                     #   Maven 项目配置
│   └── src/main/
│       ├── java/com/wardrobe/
│       │   ├── controller/         #   Servlet 控制器（7个）
│       │   │   ├── ClothesServlet.java      # 服装相关接口
│       │   │   ├── LoginOrRegServlet.java    # 登录/注册接口
│       │   │   ├── CartServlet.java          # 购物车接口
│       │   │   ├── OrderServlet.java         # 订单接口
│       │   │   ├── UserServlet.java          # 用户信息接口
│       │   │   ├── TypeSizeServlet.java      # 类型/尺码接口
│       │   │   └── UploadServlet.java        # 图片上传接口
│       │   ├── dao/               #   数据访问层（DAO）
│       │   │   ├── ClothesDao.java
│       │   │   ├── UserDao.java
│       │   │   ├── CartDao.java
│       │   │   ├── OrderDao.java
│       │   │   ├── TypeDao.java
│       │   │   └── SizeDao.java
│       │   ├── service/           #   业务逻辑层
│       │   │   ├── ClothesService.java
│       │   │   ├── UserService.java
│       │   │   └── CartOrderService.java
│       │   ├── model/             #   数据模型
│       │   │   ├── Clothes.java
│       │   │   ├── User.java
│       │   │   ├── Cart.java
│       │   │   ├── Order.java
│       │   │   ├── Type.java
│       │   │   ├── Size.java
│       │   │   └── Result.java
│       │   ├── util/              #   工具类
│       │   │   ├── DBUtil.java            # 数据库连接工具
│       │   │   ├── PasswordUtil.java      # 密码加密工具
│       │   │   └── TokenUtil.java         # JWT 令牌工具
│       │   └── filter/            #   过滤器
│       │       └── AuthFilter.java        # 登录认证过滤器
│       ├── resources/
│       │   └── db.properties      #   数据库连接配置
│       └── webapp/
│           ├── index.html         #   后端启动页
│           ├── images/            #   服装图片资源
│           └── WEB-INF/           #   Web 配置
│
├── wardrobe_user/                  # 🛒 用户购物端前端
│   ├── package.json               #   项目依赖配置
│   ├── vite.config.js             #   Vite 构建配置
│   └── src/
│       ├── main.js                #   入口文件
│       ├── App.vue                #   根组件
│       ├── style.css              #   全局样式
│       ├── api/index.js           #   API 接口封装
│       ├── router/index.js        #   路由配置
│       ├── views/                 #   页面组件
│       │   ├── Login.vue          #     登录页
│       │   ├── Register.vue       #     注册页
│       │   ├── Home.vue           #     首页（服装列表）
│       │   ├── Details.vue        #     服装详情页
│       │   ├── Cart.vue           #     购物车页
│       │   ├── Orders.vue         #     我的订单页
│       │   └── Profile.vue        #     个人中心页
│       └── components/            #   可复用组件
│
└── wardrobe_admin/                 # 🔐 管理员后台端
    ├── package.json               #   项目依赖配置
    ├── vite.config.js             #   Vite 构建配置
    └── src/
        ├── main.js                #   入口文件
        ├── App.vue                #   根组件
        ├── style.css              #   全局样式
        ├── api/index.js           #   API 接口封装
        ├── router/index.js        #   路由配置（含权限守卫）
        ├── views/                 #   页面组件
        │   ├── Login.vue          #     管理员登录页
        │   ├── ClothesManage.vue  #     服装管理页
        │   ├── OrderManage.vue    #     订单管理页
        │   └── UserManage.vue     #     用户管理页
        └── components/            #   可复用组件
```

---

## 功能模块

### 用户端功能

| 功能 | 说明 |
|------|------|
| 🔍 服装浏览 | 按类型/风格筛选，查看服装列表与详情 |
| 🛒 购物车 | 选择尺码和数量，添加/移除/修改购物车商品 |
| 📦 下单购买 | 从购物车生成订单，填写收货信息 |
| 📋 订单管理 | 查看全部订单及状态 |
| 👤 个人中心 | 修改个人信息、收货地址 |
| 🔐 登录注册 | 用户注册、登录、JWT 令牌认证 |

### 管理端功能

| 功能 | 说明 |
|------|------|
| 👗 服装管理 | 添加/编辑/删除/搜索服装，上传图片 |
| 📊 订单管理 | 查看所有用户订单，修改订单状态 |
| 👥 用户管理 | 查看用户列表，重置密码 |
| 🔐 管理员登录 | 独立管理端登录认证 |

---

## 环境要求

| 环境 | 版本要求 | 说明 |
|------|----------|------|
| **JDK** | 8 或更高 | 后端编译与运行 |
| **Maven** | 3.6+ | 后端依赖管理（可选，可用 IDEA 自带） |
| **Node.js** | 16 或更高 | 前端 npm 依赖管理 |
| **npm** | 8 或更高 | 前端包管理 |
| **PostgreSQL** | 12 或更高 | 数据库 |
| **Tomcat** | 9 或更高 | Servlet 容器（IDEA 可内置） |
| **IntelliJ IDEA** | 推荐使用 | 项目开发 IDE（.idea 配置已包含） |

---

## 快速启动

### 1. 数据库初始化

1. 安装并启动 **PostgreSQL** 服务。
2. 创建数据库：
   ```sql
   CREATE DATABASE wardrobe;
   ```
3. 执行数据库初始化脚本：
   - 用 pgAdmin 或命令行运行 `wardrobe_schema.sql`
   - 该脚本会创建所有表并插入初始数据（服装类型、尺码、示例服装、管理员账号）

```bash
# 命令行方式执行
psql -U postgres -d wardrobe -f wardrobe_schema.sql
```

### 2. 后端配置与启动

#### 配置数据库连接

编辑 `wardrobe_back/src/main/resources/db.properties`：

```properties
db.url=jdbc:postgresql://localhost:5432/wardrobe
db.username=postgres        # 改为你的 PostgreSQL 用户名
db.password=postgres        # 改为你的 PostgreSQL 密码
db.driver=org.postgresql.Driver
```

#### 启动方式（任选其一）

**方式一：IDEA 内置 Tomcat 启动（推荐）**

1. 用 IntelliJ IDEA 打开项目根目录
2. 配置 Tomcat Local Server：
   - Deployment：添加 `wardrobe_back:war exploded`
   - Application context：`/wardrobe_back`
3. 点击 Run 启动
4. 访问 `http://localhost:8080/wardrobe_back/` 确认后端已启动

**方式二：命令行 Maven + Tomcat**

```bash
cd wardrobe_back
mvn clean package          # 编译打包
# 将生成的 war 包部署到 Tomcat 的 webapps 目录
```

### 3. 前端配置与启动

#### 用户端（默认端口 5173）

```bash
cd wardrobe_user
npm install                # 首次运行需安装依赖
npm run dev                # 启动开发服务器
```

访问 `http://localhost:5173` 进入用户购物端。

#### 管理端（默认端口 5174）

```bash
cd wardrobe_admin
npm install                # 首次运行需安装依赖
npm run dev                # 启动开发服务器
```

访问 `http://localhost:5174` 进入管理员后台（使用 `/login` 路由）。

### 4. 一键启动

项目提供了便捷的一键启动脚本，位于 `启动脚本/` 目录：

> ⚠️ **注意：一键启动脚本仅启动两个前端项目**，后端需要单独启动（推荐通过 IDEA 启动 Tomcat）。

| 脚本文件 | 方式 | 说明 |
|----------|------|------|
| `start-all.bat` | 双击运行 | 打开两个 cmd 窗口分别运行用户端和管理端 |
| `start-all.ps1` | PowerShell | 自动安装依赖并打开两个窗口启动（推荐） |
| `start-all.vbs` | 双击运行 | 后台静默启动，无命令行窗口 |

**启动顺序建议：**
1. 先启动 PostgreSQL 数据库服务
2. 在 IDEA 中启动 Tomcat（后端）
3. 双击 `启动脚本/start-all.ps1` 启动前端
4. 访问用户端 `http://localhost:5173` 和管理端 `http://localhost:5174`

---

## API 接口文档

所有接口基路径：`/wardrobe_back`

### 登录注册

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| POST | `/login` | `action=register` | 用户注册 |
| POST | `/login` | `action=login` | 用户登录 |
| POST | `/login` | `action=adminLogin` | 管理员登录 |

**用户注册请求体：**
```json
{
  "username": "testuser",
  "password": "123456",
  "phone": "13800000000",
  "address": "收货地址"
}
```

**登录成功响应：**
```json
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 2,
    "username": "testuser",
    "role": 2
  }
}
```

> 📌 后续需要认证的接口需在 Header 中传递 `token: <令牌值>`

### 服装相关

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/allClothes` | 无 | 获取全部服装列表 |
| GET | `/allClothes` | `action=details&id=1` | 获取指定服装详情 |
| GET | `/allClothes` | `action=search&keyword=T恤` | 搜索服装 |
| GET | `/allClothes` | `action=filter&typeId=1` | 按类型筛选 |
| GET | `/allClothes` | `action=filter&style=简约` | 按风格筛选 |

### 类型与尺码

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/type` | 获取所有服装类型 |
| GET | `/size` | `?typeId=1` 获取指定类型的尺码 |

### 购物车（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/cart` | `action=list` | 查看购物车 |
| POST | `/cart` | `action=addToCart` | 添加商品到购物车 |
| POST | `/cart` | `action=update&cartId=1&quantity=2` | 修改数量 |
| POST | `/cart` | `action=remove&cartId=1` | 删除购物车项 |

### 订单（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/order` | `action=list` | 查看我的订单 |
| POST | `/order` | `action=create` | 创建订单（从购物车结算） |
| POST | `/order` | `action=updateStatus&orderId=1&status=2` | 更新订单状态（管理员） |

### 用户信息（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/user` | `action=info` | 获取当前用户信息 |
| POST | `/user` | `action=updateInfo` | 更新个人信息 |
| POST | `/user` | `action=updatePassword` | 修改密码 |
| GET | `/user` | `action=list` | 查看用户列表（管理员） |
| POST | `/user` | `action=resetPassword&userId=2` | 重置密码（管理员） |

### 图片上传

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/upload` | 上传服装图片（multipart/form-data） |

---

## 数据库设计

### ER 关系图

```
t_type (服装类型)           t_size (尺码)
   1 ──────────────────── N
   │                       
   │                       
   N                       
t_clothes (服装)           t_user (用户)
   │                        │
   │                        │
   N ───────────────┐       │
                     │       │
                     N       1
                  t_cart (购物车)
                     │
                     N
                     │
                  t_order (订单) ──── 1 ──── t_user
```

### 表结构

| 表名 | 说明 | 关键字段 |
|------|------|------|
| `t_type` | 服装类型 | id, type_name（上衣/裤子/鞋子/帽子/外套/裙子） |
| `t_size` | 尺码（按类型） | id, type_id(FK), size_name |
| `t_clothes` | 服装商品 | id, name, price, type_id(FK), style, stock, image |
| `t_user` | 用户 | id, username, password, phone, address, role(1=管理员,2=用户) |
| `t_cart` | 购物车 | id, user_id(FK), clothes_id(FK), size_id(FK), quantity |
| `t_order` | 订单 | id, order_no, user_id(FK), total_price, status, address |

---

## 常见问题

### Q1: 启动后端时报数据库连接错误？

检查 `db.properties` 中的数据库连接信息是否正确：
- 确保 PostgreSQL 服务正在运行
- 确认 `wardrobe` 数据库已创建
- 确认用户名和密码正确

### Q2: npm install 很慢或失败？

配置国内镜像源：

```bash
npm config set registry https://registry.npmmirror.com
```

### Q3: 前端页面请求后端接口 404？

检查 `vite.config.js` 中的代理配置：
- 用户端代理 `/wardrobe_back` 到 `http://localhost:8080`
- 管理端代理 `/wardrobe_back` 到 `http://localhost:8080`
- 确保后端 Tomcat 的 Context Path 为 `/wardrobe_back`

### Q4: 如何修改项目默认端口？

- 后端：修改 Tomcat 配置中的 HTTP Port
- 用户端：修改 `wardrobe_user/vite.config.js` 中的 `server.port`
- 管理端：修改 `wardrobe_admin/vite.config.js` 中的 `server.port`

### Q5: 如何添加新的服装类型？

方式一：通过管理端界面添加（如果支持）
方式二：直接操作数据库或执行 SQL：
```sql
INSERT INTO t_type (type_name) VALUES ('新类型名称');
```

### Q6: 需要清理并重建项目？

```bash
# 清理后端编译文件
cd wardrobe_back && mvn clean

# 清理前端依赖（重新安装）
cd wardrobe_user && rm -rf node_modules && npm install
cd wardrobe_admin && rm -rf node_modules && npm install
```

---

## 开发说明

- 后端采用 **三层架构**：Controller（Servlet）→ Service（业务逻辑）→ DAO（数据访问）
- 用户密码使用 **BCrypt** 加密存储，安全不可逆
- 用户认证使用 **JWT（JSON Web Token）**，登录后获取 token，后续请求在 Header 中携带
- 前端通过 Vite **代理** 转发 API 请求到后端，解决跨域问题
- 管理员和用户共用同一套后端接口，通过 `role` 字段区分权限

---

> 📧 如有问题，请查阅代码注释或联系项目开发者。