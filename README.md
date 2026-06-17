# 🏪 网上衣橱管理系统 (Online Wardrobe Management System)

一个完整的网上衣橱电商平台，**用户端和管理端已合并为单一前端界面**。打开即显示商品首页，购买等操作需登录。普通用户登录进入商城，管理员登录自动进入管理后台，两端界面完全隔离互不可见。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [环境要求](#环境要求)
- [快速启动](#快速启动)
- [API 接口文档](#api-接口文档)
- [数据库设计](#数据库设计)
- [常见问题](#常见问题)

---

## 项目简介

网上衣橱管理系统是一个基于 **Java Servlet + Vue 3** 的 Web 应用项目，采用前后端分离架构。

| 模块 | 说明 | 技术 |
|------|------|------|
| `wardrobe_back` | 后端接口服务 | Java Servlet + Maven + PostgreSQL |
| `wardrobe_user` | **统一前端**（商城+管理后台） | Vue 3 + Vite + Element Plus |

> **架构说明：** `wardrobe_admin` 已合并到 `wardrobe_user`，不再单独启动。
> 登录页可切换用户/管理员模式，系统根据角色自动跳转到对应界面。

**默认账号：**
- 管理员：`admin` / `admin123`（选"管理员登录"进入管理后台）
- 普通用户：`123456` / `123456`（选"用户登录"进入商城界面）

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

### 前端 (wardrobe_user)

| 技术 | 用途 |
|------|------|
| Vue 3 (Composition API) | 前端框架 |
| Vite 6 | 构建工具 |
| Vue Router 4 | 前端路由（含角色权限守卫） |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求 |

---

## 项目结构

```
实验8/                              # 项目根目录
│
├── README.md                       # 项目说明文档
├── .gitignore                      # Git 忽略配置
│
├── 启动脚本/                        # 🔧 一键启动脚本目录
│   ├── start-all.bat               #   Windows 批处理启动
│   ├── start-all.ps1               #   PowerShell 启动（推荐）
│   └── start-all.vbs               #   VBS 后台静默启动
│
├── wardrobe_back/                  # ☕ Java 后端
│   ├── pom.xml                     #   Maven 项目配置
│   └── src/main/
│       ├── java/com/wardrobe/
│       │   ├── controller/         #   Servlet 控制器（7个）
│       │   │   ├── ClothesServlet.java      # 服装接口（含管理员权限校验）
│       │   │   ├── LoginOrRegServlet.java    # 登录/注册（区分用户/管理员入口）
│       │   │   ├── CartServlet.java          # 购物车接口
│       │   │   ├── OrderServlet.java         # 订单接口（含管理员权限校验）
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
│       │   │   ├── PasswordUtil.java      # BCrypt 密码加密
│       │   │   └── TokenUtil.java         # Token 生成/解析工具
│       │   └── filter/            #   过滤器
│       │       └── AuthFilter.java        # 登录认证过滤器
│       ├── resources/
│       │   └── db.properties      #   数据库连接配置
│       └── webapp/
│           ├── index.html         #   后端启动页
│           ├── images/            #   上传的服装图片
│           └── WEB-INF/web.xml    #   Web 配置
│
├── wardrobe_user/                  # 🛒 统一前端（商城+管理后台）
│   ├── package.json               #   项目依赖配置
│   ├── vite.config.js             #   Vite 构建配置
│   └── src/
│       ├── main.js                #   入口文件
│       ├── App.vue                #   根组件（角色自动切换布局）
│       ├── style.css              #   全局样式
│       ├── api/index.js           #   API 接口封装（含 token 拦截器）
│       ├── router/index.js        #   路由配置（含角色权限守卫）
│       ├── views/                 #   页面组件
│       │   ├── Login.vue          #     登录页（用户/管理员模式切换）
│       │   ├── Register.vue       #     注册页
│       │   ├── Home.vue           #     首页（服装列表，无需登录）
│       │   ├── Details.vue        #     服装详情页
│       │   ├── Cart.vue           #     购物车页（需登录）
│       │   ├── Orders.vue         #     我的订单页（需登录）
│       │   ├── Profile.vue        #     个人中心页（需登录）
│       │   ├── AdminClothes.vue   #     管理员 - 服装管理
│       │   ├── AdminOrders.vue    #     管理员 - 订单管理
│       │   └── AdminUsers.vue     #     管理员 - 用户管理
│       └── components/            #   可复用组件
│
│   （wardrobe_admin 已合并到 wardrobe_user，不再需要）
```

---

## 功能模块

### 用户端功能

| 功能 | 说明 |
|------|------|
| 🔍 服装浏览 | 按类型/风格/名称筛选，查看服装列表与详情（**无需登录**） |
| 🛒 购物车 | 选择尺码和数量，添加/移除/修改购物车商品（需登录） |
| 📦 下单购买 | 从购物车生成订单，填写收货地址和电话（需登录） |
| 📋 订单管理 | 查看订单列表及状态，取消未发货订单（需登录） |
| 👤 个人中心 | 修改手机号、收货地址（需登录） |
| 🔐 登录注册 | 用户/管理员登录、用户注册、JWT 令牌认证 |

### 管理端功能（管理员登录后自动进入）

| 功能 | 说明 |
|------|------|
| 👗 服装管理 | 添加/编辑/删除/搜索服装，上传图片，**删除前有确认弹窗** |
| 📊 订单管理 | 查看所有用户订单，标记发货 |
| 👥 用户管理 | 查看/删除用户 |

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
| **IntelliJ IDEA** | 推荐使用 | 项目开发 IDE |

---

## 快速启动

### 1. 数据库初始化

**注意：本项目使用 PostgreSQL，默认端口 1234。**

1. 安装并启动 **PostgreSQL** 服务。
2. 创建数据库：
   ```sql
   CREATE DATABASE wardrobe;
   ```
3. 执行建表脚本（建表 + 初始数据），脚本已通过 IDEA 执行。

### 2. 后端配置与启动

#### 配置数据库连接

编辑 `wardrobe_back/src/main/resources/db.properties`：

```properties
jdbc.url=jdbc:postgresql://localhost:1234/wardrobe
jdbc.username=postgres        # 改为你的 PostgreSQL 用户名
jdbc.password=3119043808      # 改为你的 PostgreSQL 密码
jdbc.driver=org.postgresql.Driver
```

#### 启动方式

**方式一：IDEA 内置 Tomcat 启动（推荐）**

1. 用 IntelliJ IDEA 打开项目根目录
2. 配置 Tomcat Local Server：
   - Deployment：添加 `wardrobe_back:war exploded`
   - Application context：`/wardrobe_back`
3. 点击 Run 启动
4. 访问 `http://localhost:8080/wardrobe_back/` 确认后端已启动

**方式二：命令行 Maven 打包后部署到 Tomcat**

```bash
cd wardrobe_back
mvn clean package
# 将 target/wardrobe_back.war 部署到 Tomcat webapps 目录
```

### 3. 前端启动

```bash
cd wardrobe_user
npm install                # 首次运行需安装依赖
npm run dev                # 启动开发服务器（默认端口 7070）
```

访问 `http://localhost:7070` 即可进入系统。

> 注意：只需启动 wardrobe_user 一个前端项目，管理后台已整合其中。

### 4. 一键启动

项目提供了便捷的一键启动脚本，位于 `启动脚本/` 目录：

| 脚本文件 | 方式 | 说明 |
|----------|------|------|
| `start-all.ps1` | PowerShell | 自动安装依赖并启动前端（推荐） |
| `start-all.bat` | 双击运行 | 启动前端 |
| `start-all.vbs` | 双击运行 | 后台静默启动，无命令行窗口 |

**启动顺序：**
1. 先启动 PostgreSQL 数据库服务
2. 在 IDEA 中启动 Tomcat（后端）
3. 双击 `启动脚本/start-all.ps1` 启动前端
4. 访问 `http://localhost:7070`

---

## API 接口文档

所有接口基路径：`/wardrobe_back`

### 登录注册

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| POST | `/login` | `action=register&username=&password=&phone=&address=` | 用户注册 |
| POST | `/login` | `action=login&username=&password=` | **用户登录**（仅允许普通用户） |
| POST | `/login` | `action=adminLogin&username=&password=` | **管理员登录**（仅允许管理员） |

> 登录接口已做角色隔离：用户入口只允许 role=2 登录，管理员入口只允许 role=1 登录。

**登录成功响应：**
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "1:1234567890:xxxx",
    "user": { "id": 1, "username": "admin", "role": 1 }
  }
}
```

> 📌 需要认证的接口需在 Header 中传递 `token: <令牌值>`

### 服装相关

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/allClothes` | 无 | 获取全部服装列表 |
| GET | `/allClothes` | `action=details&id=1` | 获取指定服装详情 |
| GET | `/allClothes` | `typeName=&style=&name=` | 按条件筛选 |
| POST | `/allClothes` | `action=add` | 添加服装（**需管理员**） |
| POST | `/allClothes` | `action=update&id=` | 修改服装（**需管理员**） |
| POST | `/allClothes` | `action=delete&id=` | 删除服装（**需管理员，自动清理购物车关联**） |

### 类型与尺码

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/type` | 获取所有服装类型 |
| GET | `/size?typeId=1` | 获取指定类型的尺码 |

### 购物车（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/cart` | 无 | 查看购物车 |
| POST | `/cart` | `action=addToCart&clothesId=&sizeId=&quantity=` | 添加商品到购物车 |
| POST | `/cart` | `action=updateQuantity&cartId=&quantity=` | 修改数量 |
| POST | `/cart` | `action=remove&cartId=` | 删除购物车项 |

### 订单（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/order` | 无 | 查看我的订单 |
| GET | `/order` | `action=all` | 查看全部订单（**需管理员**） |
| POST | `/order` | `action=create&address=&phone=` | 创建订单 |
| POST | `/order` | `action=cancel&id=` | 取消订单 |
| POST | `/order` | `action=ship&id=` | 发货（**需管理员**） |

### 用户信息（需登录 Token）

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/user` | `action=info` | 获取当前用户信息 |
| POST | `/user` | `action=update&phone=&address=` | 更新个人信息 |
| GET | `/user` | `action=allUsers` | 查看用户列表（**需管理员**） |
| POST | `/user` | `action=delete&id=` | 删除用户（**需管理员**） |

### 图片上传

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/upload` | 上传服装图片（multipart/form-data，需管理员 Token） |

---

## 数据库设计

### 表结构

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `t_type` | 服装类型 | id, type_name（上衣/裤子/鞋子/帽子/外套/裙子） |
| `t_size` | 尺码（按类型） | id, type_id(FK→t_type), size_name |
| `t_clothes` | 服装商品 | id, name, price, type_id(FK), style, stock, image |
| `t_user` | 用户 | id, username, password(BCrypt), phone, address, role(1=管理员,2=用户) |
| `t_cart` | 购物车 | id, user_id(FK), clothes_id(FK→t_clothes), size_id(FK), quantity |
| `t_order` | 订单 | id, order_no, user_id(FK), clothes_detail, total_price, status, address, phone |

### 关键约束

- `t_cart.clothes_id` 有外键约束引用 `t_clothes.id`，删除商品时自动先清理购物车关联数据
- `t_cart` 有 `UNIQUE(user_id, clothes_id, size_id)` 防止重复添加

---

## 常见问题

### Q1: 点击删除商品没反应或报外键错误？

这是 `t_cart` 外键约束导致的。已修复：删除商品时后端会先清理购物车中关联此商品的记录，再删除商品本身。**需重启后端生效。**

### Q2: 启动后端时报数据库连接错误？

检查 `db.properties` 中的数据库连接信息：
- PostgreSQL 默认端口为 **1234**（非默认的 5432）
- 确保 PostgreSQL 服务正在运行
- 确认 `wardrobe` 数据库已创建
- 确认用户名和密码正确

### Q3: npm install 很慢或失败？

配置国内镜像源：

```bash
npm config set registry https://registry.npmmirror.com
```

### Q4: 前端页面请求后端接口 404？

检查 `vite.config.js` 中的代理配置：
- `wardrobe_user` 代理 `/wardrobe_back` 到 `http://localhost:8080`
- 确保后端 Tomcat 的 Context Path 为 `/wardrobe_back`

### Q5: 修改了后端代码如何生效？

后端修改后需要重新编译并重启 Tomcat：

```bash
# 方式一：IDEA 中点击 Build → Build Artifacts → 8:war exploded → Rebuild
# 方式二：或直接点 Tomcat 重启按钮 🔄
```

### Q6: 管理员登录后看不到管理界面？

确保使用登录页的"管理员登录"模式，或者账号 role=1。
登录页顶部分段按钮可以切换用户/管理员模式：
- **用户模式** → 只允许普通用户登录（管理员会提示"请使用管理员登录"）
- **管理模式** → 只允许管理员登录

### Q7: 如何修改项目默认端口？

- 后端：修改 Tomcat 配置中的 HTTP Port
- 前端：修改 `wardrobe_user/vite.config.js` 中的 `server.port`

---

## 开发说明

- 后端采用 **三层架构**：Controller（Servlet）→ Service（业务逻辑）→ DAO（数据访问）
- 用户密码使用 **BCrypt** 加密存储
- 用户认证使用简单 **Token** 机制（`userId:timestamp:uuid`）
- 前端通过 Vite **代理** 转发 API 请求到后端，解决跨域问题
- 管理员和用户共用同一套后端接口，通过 `role` 字段 + 后端校验区分权限
- 服装图片使用 **Unsplash** 免费图源，按商品类型匹配不同服装图片

---

> 📧 如有问题，请查阅代码注释或联系项目开发者。
