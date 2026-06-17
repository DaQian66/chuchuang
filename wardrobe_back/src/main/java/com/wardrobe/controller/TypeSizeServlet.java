package com.wardrobe.controller;

import com.wardrobe.dao.SizeDao;
import com.wardrobe.dao.TypeDao;
import com.wardrobe.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类型和尺码 Servlet
 * 路由：/type, /size
 */
@WebServlet({"/type", "/size"})
public class TypeSizeServlet extends HttpServlet {
    private TypeDao typeDao = new TypeDao();
    private SizeDao sizeDao = new SizeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String servletPath = req.getServletPath();
        try {
            if (servletPath.equals("/type")) {
                java.util.List list = typeDao.getAllTypes();
                resp.getWriter().write(Result.success(list).toJson());
            } else {
                String typeId = req.getParameter("typeId");
                java.util.List list = sizeDao.getSizesByTypeId(Integer.parseInt(typeId));
                resp.getWriter().write(Result.success(list).toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("查询失败").toJson());
        }
    }
}
