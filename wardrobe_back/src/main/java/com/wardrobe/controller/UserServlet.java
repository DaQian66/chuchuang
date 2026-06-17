package com.wardrobe.controller;

import com.wardrobe.model.Result;
import com.wardrobe.model.User;
import com.wardrobe.service.UserService;
import com.wardrobe.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户信息 Servlet
 * 受保护：需要登录验证
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();

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
        try {
            if ("allUsers".equals(action)) {
                // 后台查询所有用户（需要管理员角色，在Filter中已校验）
                getAllUsers(req, resp);
            } else {
                getUserInfo(req, resp, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误").toJson());
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
            if ("update".equals(action)) {
                updateUserInfo(req, resp, userId);
            } else if ("delete".equals(action)) {
                // 后台删除用户
                deleteUser(req, resp);
            } else {
                resp.getWriter().write(Result.error("未知操作").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误").toJson());
        }
    }

    private void getUserInfo(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws IOException {
        Result result = userService.getUserInfo(userId);
        resp.getWriter().write(result.toJson());
    }

    private void updateUserInfo(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws IOException {
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        User user = new User();
        user.setId(userId);
        user.setPhone(phone);
        user.setAddress(address);

        Result result = userService.updateUserInfo(user);
        resp.getWriter().write(result.toJson());
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 需要管理员角色校验
        String token = req.getHeader("token");
        Integer userId = TokenUtil.parseToken(token);
        User admin = userService.getUserByToken(token);
        if (admin == null || admin.getRole() == null || admin.getRole() != 1) {
            resp.getWriter().write(Result.error(403, "无权限").toJson());
            return;
        }
        // 这里用 UserDao 直接查询
        try {
            com.wardrobe.dao.UserDao userDao = new com.wardrobe.dao.UserDao();
            java.util.List<User> list = userDao.getAllUsers();
            // 移除密码
            for (User u : list) {
                u.setPassword(null);
            }
            resp.getWriter().write(Result.success(list).toJson());
        } catch (Exception e) {
            resp.getWriter().write(Result.error("查询失败").toJson());
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        try {
            new com.wardrobe.dao.UserDao().deleteUser(Integer.parseInt(idStr));
            resp.getWriter().write(Result.success("删除成功").toJson());
        } catch (Exception e) {
            resp.getWriter().write(Result.error("删除失败").toJson());
        }
    }
}
