package com.wardrobe.dao;

import com.wardrobe.model.Order;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

/**
 * 订单数据访问层
 */
public class OrderDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 创建订单
     */
    public void addOrder(Order order) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO t_order (order_no, user_id, clothes_detail, total_price, status, address, phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            qr.update(conn, sql,
                    order.getOrderNo(), order.getUserId(), order.getClothesDetail(),
                    order.getTotalPrice(), order.getStatus(),
                    order.getAddress(), order.getPhone());
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 查询用户订单
     */
    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByUserId(Integer userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT o.id AS id, o.order_no AS orderNo, o.user_id AS userId, " +
                    "o.clothes_detail AS clothesDetail, o.total_price AS totalPrice, " +
                    "o.status, o.address, o.phone, o.create_time AS createTime, o.update_time AS updateTime " +
                    "FROM t_order o WHERE o.user_id = ? ORDER BY o.create_time DESC";
            BeanListHandler<Order> handler = new BeanListHandler<>(Order.class);
            return qr.query(conn, sql, handler, userId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据ID查询订单
     */
    @SuppressWarnings("unchecked")
    public Order getOrderById(Integer id) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT o.id AS id, o.order_no AS orderNo, o.user_id AS userId, u.username, " +
                    "o.clothes_detail AS clothesDetail, o.total_price AS totalPrice, " +
                    "o.status, o.address, o.phone, o.create_time AS createTime, o.update_time AS updateTime " +
                    "FROM t_order o JOIN t_user u ON o.user_id = u.id WHERE o.id = ?";
            BeanHandler<Order> handler = new BeanHandler<>(Order.class);
            return qr.query(conn, sql, handler, id);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 查询所有订单（后台管理用）
     */
    @SuppressWarnings("unchecked")
    public List<Order> getAllOrders() throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT o.id AS id, o.order_no AS orderNo, o.user_id AS userId, u.username, " +
                    "o.clothes_detail AS clothesDetail, o.total_price AS totalPrice, " +
                    "o.status, o.address, o.phone, o.create_time AS createTime, o.update_time AS updateTime " +
                    "FROM t_order o JOIN t_user u ON o.user_id = u.id ORDER BY o.create_time DESC";
            BeanListHandler<Order> handler = new BeanListHandler<>(Order.class);
            return qr.query(conn, sql, handler);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 更新订单状态
     */
    public void updateOrderStatus(Integer orderId, Integer status) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_order SET status = ? WHERE id = ?";
            qr.update(conn, sql, status, orderId);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 取消订单
     */
    public void cancelOrder(Integer orderId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_order SET status = 3 WHERE id = ?";
            qr.update(conn, sql, orderId);
        } finally {
            DBUtil.close(conn);
        }
    }
}
