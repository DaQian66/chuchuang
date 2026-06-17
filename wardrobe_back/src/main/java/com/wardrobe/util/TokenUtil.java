package com.wardrobe.util;

import java.util.UUID;

/**
 * Token 工具类
 * 用于生成和解析用户登录 Token
 */
public class TokenUtil {
    /**
     * 生成 Token
     * 格式: userId:timestamp:uuid
     */
    public static String generateToken(Integer userId) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return userId + ":" + System.currentTimeMillis() + ":" + uuid.substring(0, 16);
    }

    /**
     * 解析 Token，获取用户ID
     * 返回 null 表示 Token 无效
     */
    public static Integer parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        try {
            String[] parts = token.split(":");
            if (parts.length < 2) {
                return null;
            }
            return Integer.parseInt(parts[0].trim());
        } catch (Exception e) {
            return null;
        }
    }
}
