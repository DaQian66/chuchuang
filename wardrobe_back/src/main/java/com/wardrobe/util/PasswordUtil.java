package com.wardrobe.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码工具类
 * 使用 BCrypt 加密用户密码
 */
public class PasswordUtil {
    /** 加密密码 */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /** 验证密码 */
    public static boolean matches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
