package com.wardrobe.service;

import com.wardrobe.dao.UserDao;
import com.wardrobe.model.Result;
import com.wardrobe.model.User;
import com.wardrobe.util.PasswordUtil;
import com.wardrobe.util.TokenUtil;

import java.util.regex.Pattern;

/**
 * 用户业务逻辑层
 */
public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 用户注册
     * 支持手机号和学号格式验证
     */
    public Result register(User user) {
        // 参数校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return Result.error("密码至少6位");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }

        // 用户名长度校验（2-20个字符）
        if (!user.getUsername().trim().matches("^\\w{2,20}$")) {
            return Result.error("用户名长度为2-20个字符");
        }

        // 手机号/学号校验：支持11位手机号或学号格式
        String phone = user.getPhone().trim();
        Pattern phonePattern = Pattern.compile("^(1[3-9]\\d{9})$");
        Pattern studentIdPattern = Pattern.compile("^(24\\d{10})$"); // 学号格式：24开头12位

        if (!phonePattern.matcher(phone).matches() && !studentIdPattern.matcher(phone).matches()) {
            return Result.error("请输入正确的手机号（11位）或学号（24开头12位）");
        }

        try {
            // 检查用户名是否已存在
            if (userDao.isUserNameExist(user.getUsername().trim())) {
                return Result.error("用户名已存在");
            }
            // 检查手机号/学号是否已存在
            if (userDao.isPhoneExist(phone)) {
                return Result.error("手机号/学号已存在");
            }

            // 密码加密
            String hashedPassword = PasswordUtil.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUsername(user.getUsername().trim());
            user.setPhone(phone);

            // 插入用户
            userDao.addUser(user);
            return Result.success("注册成功");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 用户登录
     * 返回 token 和用户信息
     */
    public Result login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            return Result.error("用户名或密码不能为空");
        }

        try {
            User user = userDao.getUserByName(username.trim());
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证密码
            if (!PasswordUtil.matches(password, user.getPassword())) {
                // 兼容未加密的旧密码（如 SQL 初始化数据），自动升级为 BCrypt
                if (password.equals(user.getPassword())) {
                    userDao.updatePassword(user.getId(), PasswordUtil.encode(password));
                } else {
                    return Result.error("密码错误");
                }
            }

            // 生成 token
            String token = TokenUtil.generateToken(user.getId());

            // 返回用户信息（不含密码）
            user.setPassword(null);
            return Result.success("登录成功", new LoginResult(user, token));

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 后台管理员登录
     * 额外校验管理员角色
     */
    public Result adminLogin(String username, String password) {
        Result result = login(username, password);

        if (result.getCode() == 200) {
            LoginResult loginResult = (LoginResult) result.getData();
            if (loginResult.getUser() == null || loginResult.getUser().getRole() == null
                    || loginResult.getUser().getRole() != 1) {
                return Result.error("无权访问管理后台，需要管理员账号");
            }
        }
        return result;
    }

    /**
     * 根据token获取用户信息
     */
    public User getUserByToken(String token) {
        Integer userId = TokenUtil.parseToken(token);
        if (userId == null) return null;
        try {
            User user = userDao.getUserById(userId);
            if (user != null) {
                user.setPassword(null); // 移除密码
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户详情
     */
    public Result getUserInfo(Integer userId) {
        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    public Result updateUserInfo(User user) {
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            try {
                if (userDao.isPhoneExist(user.getPhone().trim())) {
                    // 排除自己
                    User existing = userDao.getUserById(user.getId());
                    if (existing != null && !existing.getPhone().equals(user.getPhone().trim())) {
                        return Result.error("手机号/学号已被使用");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            userDao.updateUser(user);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 登录结果封装
     */
    public static class LoginResult {
        private User user;
        private String token;

        public LoginResult(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
