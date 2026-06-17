-- ============================================================
-- 网上衣橱系统 - PostgreSQL 数据库脚本
-- 数据库: wardrobe
-- ============================================================

-- 1. 服装类型表
CREATE TABLE IF NOT EXISTS t_type (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 服装尺码表（按类型关联）
CREATE TABLE IF NOT EXISTS t_size (
    id SERIAL PRIMARY KEY,
    type_id INTEGER NOT NULL REFERENCES t_type(id),
    size_name VARCHAR(20) NOT NULL,
    UNIQUE(type_id, size_name)
);

-- 3. 服装表
CREATE TABLE IF NOT EXISTS t_clothes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image VARCHAR(255),
    price NUMERIC(10,2) NOT NULL,
    type_id INTEGER REFERENCES t_type(id),
    style VARCHAR(50),
    description TEXT,
    stock INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. 用户表（role: 1=管理员, 2=普通用户）
CREATE TABLE IF NOT EXISTS t_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    role INTEGER DEFAULT 2,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. 购物车表
CREATE TABLE IF NOT EXISTS t_cart (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES t_user(id),
    clothes_id INTEGER NOT NULL REFERENCES t_clothes(id),
    size_id INTEGER NOT NULL REFERENCES t_size(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    add_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, clothes_id, size_id)
);

-- 6. 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id SERIAL PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL REFERENCES t_user(id),
    clothes_detail TEXT NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    status INTEGER DEFAULT 0,
    address VARCHAR(255),
    phone VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 初始数据
-- ============================================================

-- 服装类型
INSERT INTO t_type (type_name) VALUES
    ('上衣'), ('裤子'), ('鞋子'), ('帽子'), ('外套'), ('裙子');

-- 各类型的尺码
INSERT INTO t_size (type_id, size_name) VALUES
    (1, 'XS'), (1, 'S'), (1, 'M'), (1, 'L'), (1, 'XL'), (1, 'XXL'),
    (2, 'S'), (2, 'M'), (2, 'L'), (2, 'XL'),
    (3, '36'), (3, '37'), (3, '38'), (3, '39'), (3, '40'), (3, '41'), (3, '42'),
    (4, 'S'), (4, 'M'), (4, 'L'),
    (5, 'S'), (5, 'M'), (5, 'L'), (5, 'XL'), (5, 'XXL'),
    (6, 'XS'), (6, 'S'), (6, 'M'), (6, 'L');

-- 管理员账号 (密码: admin123, BCrypt加密后的值)
-- 注意：这里先用明文，实际项目中应使用BCrypt加密
INSERT INTO t_user (username, password, phone, address, role) VALUES
    ('admin', 'admin123', '13800000001', '管理员地址', 1);

-- 示例服装数据
INSERT INTO t_clothes (name, image, price, type_id, style, description, stock) VALUES
    ('经典白T恤', '/images/default_clothes.jpg', 89.00, 1, '简约', '纯棉材质，舒适透气，百搭基础款', 100),
    ('牛仔长裤', '/images/default_clothes.jpg', 199.00, 2, '休闲', '高弹力牛仔面料，修身直筒版型', 80),
    ('运动跑鞋', '/images/default_clothes.jpg', 359.00, 3, '运动', '轻量化鞋底，缓震科技，适合跑步健身', 50),
    ('棒球帽', '/images/default_clothes.jpg', 59.00, 4, '潮流', '可调节帽围，遮阳防晒', 200),
    ('风衣外套', '/images/default_clothes.jpg', 459.00, 5, '商务', '防风面料，经典双排扣设计', 30),
    ('碎花连衣裙', '/images/default_clothes.jpg', 239.00, 6, '甜美', '雪纺面料，清新碎花图案', 60),
    ('黑色卫衣', '/images/default_clothes.jpg', 159.00, 1, '街头', '加绒内里，宽松版型', 120),
    ('西装裤', '/images/default_clothes.jpg', 229.00, 2, '商务', '垂感面料，修身剪裁', 70),
    ('帆布鞋', '/images/default_clothes.jpg', 129.00, 3, '文艺', '经典低帮设计，耐磨橡胶底', 90),
    ('针织毛衣', '/images/default_clothes.jpg', 189.00, 1, '温柔', '柔软羊毛混纺，保暖舒适', 65),
    ('牛仔裤', '/images/default_clothes.jpg', 179.00, 2, '复古', '原色丹宁，直筒宽松', 85),
    ('皮夹克', '/images/default_clothes.jpg', 599.00, 5, '酷帅', 'PU皮质，机车风格', 25);

-- 初始购物车数据（可选，测试用）
-- INSERT INTO t_cart (user_id, clothes_id, size_id, quantity) VALUES
--     (1, 1, 3, 1), (1, 2, 5, 1);
