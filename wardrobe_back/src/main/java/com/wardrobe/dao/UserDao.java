package com.wardrobe.dao;

import com.wardrobe.model.User;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.util.List;

/**
 * 用户数据访问层
 * 负责用户数据的增删查改
 */
public class UserDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 检查用户名是否已存在
     */
    public boolean isUserNameExist(String userName) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(1) FROM t_user WHERE username = ?";
            ScalarHandler handler = new ScalarHandler(1);
            Object countObj = qr.query(conn, sql, handler, userName);
            Long count = countObj instanceof Number ? ((Number) countObj).longValue() : 0L;
            return count != null && count > 0;
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 检查手机号是否已存在
     * 支持学号格式（纯数字或特定格式）
     */
    public boolean isPhoneExist(String phone) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(1) FROM t_user WHERE phone = ?";
            ScalarHandler handler = new ScalarHandler(1);
            Object countObj = qr.query(conn, sql, handler, phone);
            Long count = countObj instanceof Number ? ((Number) countObj).longValue() : 0L;
            return count != null && count > 0;
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 添加新用户
     */
    public void addUser(User user) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO t_user (username, password, phone, address, role) VALUES (?, ?, ?, ?, ?)";
            qr.update(conn, sql,
                    user.getUsername(), user.getPassword(),
                    user.getPhone(), user.getAddress(),
                    user.getRole() != null ? user.getRole() : 2);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据用户名查询用户
     */
    public User getUserByName(String userName) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, phone, address, role, create_time FROM t_user WHERE username = ?";
            return qr.query(conn, sql,
                    new BeanHandler<>(User.class), userName);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据ID查询用户
     */
    public User getUserById(Integer id) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, phone, address, role, create_time FROM t_user WHERE id = ?";
            return qr.query(conn, sql,
                    new BeanHandler<>(User.class), id);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 更新用户信息
     */
    public void updateUser(User user) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_user SET phone = ?, address = ? WHERE id = ?";
            qr.update(conn, sql, user.getPhone(), user.getAddress(), user.getId());
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 修改密码
     */
    public void updatePassword(Integer userId, String password) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_user SET password = ? WHERE id = ?";
            qr.update(conn, sql, password, userId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 查询所有用户（后台管理用）
     */
    public List<User> getAllUsers() throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, phone, address, role, create_time FROM t_user ORDER BY id";
            return qr.query(conn, sql, new BeanListHandler<>(User.class));
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据ID删除用户
     */
    public void deleteUser(Integer id) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM t_user WHERE id = ?";
            qr.update(conn, sql, id);
        } finally {
            DBUtil.close(conn);
        }
    }
}
