package com.wardrobe.controller;

import com.wardrobe.model.Result;
import com.wardrobe.model.User;
import com.wardrobe.service.UserService;
import com.wardrobe.service.UserService.LoginResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录注册 Servlet
 * 路由：/login, /register, /adminLogin
 */
@WebServlet("/login")
public class LoginOrRegServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String action = req.getParameter("action");

        try {
            if ("register".equals(action)) {
                handleRegister(req, resp);
            } else if ("login".equals(action)) {
                handleLogin(req, resp);
            } else if ("adminLogin".equals(action)) {
                handleAdminLogin(req, resp);
            } else {
                resp.getWriter().write(Result.error("未知操作").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误").toJson());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     * 用户注册
     */
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        User user = new User(username, password, phone, address);
        Result result = userService.register(user);
        resp.getWriter().write(result.toJson());
    }

    /**
     * 用户登录
     */
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Result result = userService.login(username, password);

        if (result.getCode() == 200) {
            LoginResult lr = (LoginResult) result.getData();
            // 返回不含密码的用户信息
            lr.getUser().setPassword(null);
        }
        resp.getWriter().write(result.toJson());
    }

    /**
     * 管理员登录
     */
    private void handleAdminLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Result result = userService.adminLogin(username, password);

        if (result.getCode() == 200) {
            LoginResult lr = (LoginResult) result.getData();
            lr.getUser().setPassword(null);
        }
        resp.getWriter().write(result.toJson());
    }
}
