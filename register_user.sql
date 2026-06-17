-- 任务九：注册用户名为个人姓名，手机号使用学号格式
INSERT INTO t_user (username, password, phone, address, role)
VALUES ('郑甲航', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2408010230', '学生宿舍', 2)
ON CONFLICT (username) DO NOTHING;
