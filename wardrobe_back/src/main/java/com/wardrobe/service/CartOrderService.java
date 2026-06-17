package com.wardrobe.service;

import com.wardrobe.dao.CartDao;
import com.wardrobe.dao.OrderDao;
import com.wardrobe.model.*;
import com.wardrobe.util.TokenUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 购物车和订单业务逻辑层
 */
public class CartOrderService {
    private CartDao cartDao = new CartDao();
    private OrderDao orderDao = new OrderDao();

    // ==================== 购物车 ====================

    /**
     * 查询购物车
     */
    public Result getCart(Integer userId) {
        try {
            List<Cart> list = cartDao.getCartByUserId(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 添加到购物车
     */
    public Result addToCart(Integer userId, Integer clothesId, Integer sizeId, Integer quantity) {
        if (clothesId == null || sizeId == null || quantity == null || quantity <= 0) {
            return Result.error("参数错误");
        }
        try {
            // 检查是否已存在
            List<Cart> existing = cartDao.getCartByUserId(userId);
            for (Cart item : existing) {
                if (item.getClothesId().equals(clothesId) && item.getSizeId().equals(sizeId)) {
                    cartDao.updateQuantity(item.getId(), item.getQuantity() + quantity);
                    return Result.success("已更新数量");
                }
            }
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setClothesId(clothesId);
            cart.setSizeId(sizeId);
            cart.setQuantity(quantity);
            cartDao.addToCart(cart);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 更新购物车数量
     */
    public Result updateQuantity(Integer cartId, Integer quantity) {
        if (quantity <= 0) {
            return Result.error("数量必须大于0");
        }
        try {
            cartDao.updateQuantity(cartId, quantity);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除购物车项
     */
    public Result removeFromCart(Integer cartId) {
        try {
            cartDao.removeFromCart(cartId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    // ==================== 订单 ====================

    /**
     * 提交订单
     */
    public Result createOrder(Integer userId, String address, String phone) {
        try {
            // 检查地址和电话
            if (address == null || address.trim().isEmpty()) {
                return Result.error("收货地址不能为空");
            }
            if (phone == null || phone.trim().isEmpty()) {
                return Result.error("联系电话不能为空");
            }

            List<Cart> items = cartDao.getCartByUserId(userId);
            if (items == null || items.isEmpty()) {
                return Result.error("购物车为空");
            }

            // 计算总价
            BigDecimal totalPrice = BigDecimal.ZERO;
            StringBuilder detail = new StringBuilder();
            for (Cart item : items) {
                if (item.getClothesPrice() != null && item.getQuantity() != null) {
                    BigDecimal subtotal = item.getClothesPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    totalPrice = totalPrice.add(subtotal);
                }
                detail.append(item.getClothesName())
                        .append(" (").append(item.getSizeName()).append(") x")
                        .append(item.getQuantity()).append("；");
            }

            // 生成订单号：ORD + 时间戳 + 用户ID(补零到4位)
            String userIdStr = String.format("%04d", userId);
            String orderNo = "ORD" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                    + userIdStr.substring(0, 4);

            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setClothesDetail(detail.toString());
            order.setTotalPrice(totalPrice);
            order.setStatus(0); // 待发货
            order.setAddress(address);
            order.setPhone(phone);

            orderDao.addOrder(order);

            // 清空购物车
            cartDao.clearCart(userId);

            return Result.success("订单创建成功", new OrderResult(orderNo, totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("下单失败：" + e.getMessage());
        }
    }

    /**
     * 查询用户订单
     */
    public Result getUserOrders(Integer userId) {
        try {
            List<Order> list = orderDao.getOrdersByUserId(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    public Result cancelOrder(Integer orderId) {
        try {
            Order order = orderDao.getOrderById(orderId);
            if (order == null) return Result.error("订单不存在");
            if (order.getStatus() != null && order.getStatus() == 0) {
                orderDao.cancelOrder(orderId);
                return Result.success("订单已取消");
            }
            return Result.error("只有待发货订单可以取消");
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 订单发货（后台用）
     */
    public Result shipOrder(Integer orderId) {
        try {
            Order order = orderDao.getOrderById(orderId);
            if (order == null) return Result.error("订单不存在");
            if (order.getStatus() != null && order.getStatus() != 0) {
                return Result.error("只有待发货订单可以发货");
            }
            orderDao.updateOrderStatus(orderId, 1); // 已发货
            return Result.success("发货成功");
        } catch (Exception e) {
            return Result.error("发货失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有订单（后台用）
     */
    public Result getAllOrders() {
        try {
            List<Order> list = orderDao.getAllOrders();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 订单结果封装
     */
    public static class OrderResult {
        private String orderNo;
        private BigDecimal totalPrice;

        public OrderResult(String orderNo, BigDecimal totalPrice) {
            this.orderNo = orderNo;
            this.totalPrice = totalPrice;
        }

        public String getOrderNo() { return orderNo; }
        public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    }
}
