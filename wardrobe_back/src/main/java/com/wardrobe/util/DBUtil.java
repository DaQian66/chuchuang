package com.wardrobe.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 数据库连接工具类
 * 从 db.properties 读取 PostgreSQL 连接信息
 */
public class DBUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try {
            Properties props = new Properties();
            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (is == null) {
                // 开发环境直接从 resources 目录加载
                is = DBUtil.class.getResourceAsStream("/db.properties");
            }
            if (is == null) {
                // 尝试从类路径加载
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            }
            props.load(is);
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");
            driver = props.getProperty("jdbc.driver");

            // 加载驱动
            if (driver != null) {
                Class.forName(driver);
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库配置加载失败", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable r : resources) {
            if (r != null) {
                try { r.close(); } catch (Exception ignored) {}
            }
        }
    }
}
