package com.wardrobe.controller;

import com.wardrobe.model.Result;
import com.wardrobe.service.CartOrderService;
import com.wardrobe.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 订单 Servlet
 * 受保护：需要登录验证
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private CartOrderService cartOrderService = new CartOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String token = req.getHeader("token");
        Integer userId = TokenUtil.parseToken(token);
        if (userId == null) {
            resp.getWriter().write(Result.error(401, "请先登录").toJson());
            return;
        }

        String action = req.getParameter("action");
        if ("all".equals(action)) {
            // 后台查询所有订单
            Result result = cartOrderService.getAllOrders();
            resp.getWriter().write(result.toJson());
        } else {
            // 前台查询用户订单
            Result result = cartOrderService.getUserOrders(userId);
            resp.getWriter().write(result.toJson());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String token = req.getHeader("token");
        Integer userId = TokenUtil.parseToken(token);
        if (userId == null) {
            resp.getWriter().write(Result.error(401, "请先登录").toJson());
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                createOrder(req, resp, userId);
            } else if ("cancel".equals(action)) {
                cancelOrder(req, resp);
            } else if ("ship".equals(action)) {
                shipOrder(req, resp);
            } else {
                resp.getWriter().write(Result.error("未知操作").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误").toJson());
        }
    }

    private void createOrder(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws IOException {
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        Result result = cartOrderService.createOrder(userId, address, phone);
        resp.getWriter().write(result.toJson());
    }

    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        Result result = cartOrderService.cancelOrder(Integer.parseInt(idStr));
        resp.getWriter().write(result.toJson());
    }

    private void shipOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        Result result = cartOrderService.shipOrder(Integer.parseInt(idStr));
        resp.getWriter().write(result.toJson());
    }
}
