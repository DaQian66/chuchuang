package com.wardrobe.dao;

import com.wardrobe.model.Cart;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

/**
 * 购物车数据访问层
 */
public class CartDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 查询用户的购物车（联表获取服装和尺码信息）
     */
    @SuppressWarnings("unchecked")
    public List<Cart> getCartByUserId(Integer userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT ct.id AS id, ct.user_id AS userId, ct.clothes_id AS clothesId, " +
                    "c.name AS clothesName, c.image AS clothesImage, c.price AS clothesPrice, " +
                    "ct.size_id AS sizeId, s.size_name AS sizeName, ct.quantity, ct.add_time AS addTime " +
                    "FROM t_cart ct " +
                    "JOIN t_clothes c ON ct.clothes_id = c.id " +
                    "JOIN t_size s ON ct.size_id = s.id " +
                    "WHERE ct.user_id = ? ORDER BY ct.add_time DESC";
            BeanListHandler<Cart> handler = new BeanListHandler<>(Cart.class);
            return qr.query(conn, sql, handler, userId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 添加到购物车
     */
    public void addToCart(Cart cart) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO t_cart (user_id, clothes_id, size_id, quantity) VALUES (?, ?, ?, ?)";
            qr.update(conn, sql,
                    cart.getUserId(), cart.getClothesId(), cart.getSizeId(), cart.getQuantity());
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 更新购物车数量
     */
    public void updateQuantity(Integer cartId, Integer quantity) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_cart SET quantity = ? WHERE id = ?";
            qr.update(conn, sql, quantity, cartId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 删除购物车项
     */
    public void removeFromCart(Integer cartId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM t_cart WHERE id = ?";
            qr.update(conn, sql, cartId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 清空用户购物车
     */
    public void clearCart(Integer userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM t_cart WHERE user_id = ?";
            qr.update(conn, sql, userId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据服装ID删除购物车记录（删除服装前清理关联数据）
     */
    public void deleteByClothesId(Integer clothesId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM t_cart WHERE clothes_id = ?";
            qr.update(conn, sql, clothesId);
        } finally {
            DBUtil.close(conn);
        }
    }
}
