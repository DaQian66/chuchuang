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
 * 购物车 Servlet
 * 受保护：需要登录验证
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
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

        Result result = cartOrderService.getCart(userId);
        resp.getWriter().write(result.toJson());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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
        try {
            if ("addToCart".equals(action)) {
                addToCart(req, resp, userId);
            } else if ("updateQuantity".equals(action)) {
                updateQuantity(req, resp);
            } else if ("remove".equals(action)) {
                removeFromCart(req, resp);
            } else {
                doGet(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误").toJson());
        }
    }

    private void addToCart(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws IOException {
        String clothesIdStr = req.getParameter("clothesId");
        String sizeIdStr = req.getParameter("sizeId");
        String quantityStr = req.getParameter("quantity");

        try {
            Result result = cartOrderService.addToCart(
                    userId,
                    Integer.parseInt(clothesIdStr),
                    Integer.parseInt(sizeIdStr),
                    Integer.parseInt(quantityStr)
            );
            resp.getWriter().write(result.toJson());
        } catch (NumberFormatException e) {
            resp.getWriter().write(Result.error("参数格式错误").toJson());
        }
    }

    private void updateQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cartIdStr = req.getParameter("cartId");
        String quantityStr = req.getParameter("quantity");
        try {
            Result result = cartOrderService.updateQuantity(
                    Integer.parseInt(cartIdStr), Integer.parseInt(quantityStr));
            resp.getWriter().write(result.toJson());
        } catch (NumberFormatException e) {
            resp.getWriter().write(Result.error("参数格式错误").toJson());
        }
    }

    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cartIdStr = req.getParameter("cartId");
        try {
            Result result = cartOrderService.removeFromCart(Integer.parseInt(cartIdStr));
            resp.getWriter().write(result.toJson());
        } catch (NumberFormatException e) {
            resp.getWriter().write(Result.error("参数格式错误").toJson());
        }
    }
}
