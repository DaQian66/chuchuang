package com.wardrobe.filter;

import com.wardrobe.model.Result;
import com.wardrobe.model.User;
import com.wardrobe.service.UserService;
import com.wardrobe.util.TokenUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 * 校验 token，验证用户登录状态
 * 对受保护的接口进行访问控制
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String servletPath = httpRequest.getServletPath();
        String method = httpRequest.getMethod();

        // 排除不需要认证的接口（公共浏览接口）
        if (servletPath.equals("/login") || servletPath.equals("/upload")
                || servletPath.equals("/allClothes") || servletPath.equals("/type")
                || servletPath.equals("/size")) {
            chain.doFilter(request, response);
            return;
        }

        // 获取 token
        String token = httpRequest.getHeader("token");
        String cookieToken = extractTokenFromCookie(httpRequest);
        if (token == null || token.trim().isEmpty()) {
            token = cookieToken;
        }

        if (token == null || token.trim().isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(Result.error(401, "请先登录").toJson());
            return;
        }

        // 验证 token 并获取用户信息
        Integer userId = TokenUtil.parseToken(token);
        if (userId == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(Result.error(401, "登录已过期，请重新登录").toJson());
            return;
        }

        // 验证用户是否存在（简单验证，通过DBUtil检查）
        UserService userService = new UserService();
        User user = userService.getUserByToken(token);
        if (user == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(Result.error(401, "用户不存在").toJson());
            return;
        }

        // 将用户信息设置到 request 中供 Servlet 使用
        httpRequest.setAttribute("userId", userId);
        httpRequest.setAttribute("user", user);

        chain.doFilter(request, response);
    }

    /**
     * 从 Cookie 中提取 token（备用方案）
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void destroy() {}
}
