package com.wardrobe.controller;

import com.wardrobe.model.Clothes;
import com.wardrobe.model.Result;
import com.wardrobe.service.ClothesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 服装 Servlet
 * 路由：/allClothes, /clothDetails, /addClothes, /updateClothes, /deleteClothes
 */
@WebServlet("/allClothes")
public class ClothesServlet extends HttpServlet {
    private ClothesService clothesService = new ClothesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String action = req.getParameter("action");
        try {
            if ("details".equals(action)) {
                getDetail(req, resp);
            } else {
                getAll(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误: " + e.getMessage()).toJson());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                addClothes(req, resp);
            } else if ("update".equals(action)) {
                updateClothes(req, resp);
            } else if ("delete".equals(action)) {
                deleteClothes(req, resp);
            } else {
                doGet(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("服务器错误: " + e.getMessage()).toJson());
        }
    }

    /**
     * 查询所有服装（支持条件查询）— 使用原生 JDBC 避免 DbUtils 在 JDK17 下的兼容性问题
     */
    private void getAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeName = req.getParameter("typeName");
        String style = req.getParameter("style");
        String name = req.getParameter("name");

        java.util.List<Clothes> list = new java.util.ArrayList<>();
        java.sql.Connection conn = null;
        try {
            conn = com.wardrobe.util.DBUtil.getConnection();
            String sql;
            if (typeName == null && style == null && name == null) {
                sql = "SELECT c.id, c.name, c.image, c.price, c.type_id, c.style, " +
                      "c.description, c.stock, c.create_time, c.update_time, t.type_name " +
                      "FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id " +
                      "ORDER BY c.create_time DESC";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Clothes c = new Clothes();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setImage(rs.getString("image"));
                    c.setPrice(rs.getBigDecimal("price"));
                    c.setTypeId(rs.getInt("type_id"));
                    c.setStyle(rs.getString("style"));
                    c.setDescription(rs.getString("description"));
                    c.setStock(rs.getInt("stock"));
                    c.setCreateTime(rs.getTimestamp("create_time"));
                    c.setUpdateTime(rs.getTimestamp("update_time"));
                    c.setTypeName(rs.getString("type_name"));
                    list.add(c);
                }
                rs.close();
                ps.close();
            } else {
                sql = "SELECT c.id, c.name, c.image, c.price, c.type_id, c.style, " +
                      "c.description, c.stock, c.create_time, c.update_time, t.type_name " +
                      "FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE 1=1";
                java.util.List<String> params = new java.util.ArrayList<>();
                if (typeName != null && !typeName.isEmpty()) {
                    sql += " AND t.type_name LIKE ?";
                    params.add("%" + typeName + "%");
                }
                if (style != null && !style.isEmpty()) {
                    sql += " AND c.style LIKE ?";
                    params.add("%" + style + "%");
                }
                if (name != null && !name.isEmpty()) {
                    sql += " AND c.name LIKE ?";
                    params.add("%" + name + "%");
                }
                sql += " ORDER BY c.create_time DESC";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                for (int i = 0; i < params.size(); i++) {
                    ps.setString(i + 1, params.get(i));
                }
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Clothes c = new Clothes();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setImage(rs.getString("image"));
                    c.setPrice(rs.getBigDecimal("price"));
                    c.setTypeId(rs.getInt("type_id"));
                    c.setStyle(rs.getString("style"));
                    c.setDescription(rs.getString("description"));
                    c.setStock(rs.getInt("stock"));
                    c.setCreateTime(rs.getTimestamp("create_time"));
                    c.setUpdateTime(rs.getTimestamp("update_time"));
                    c.setTypeName(rs.getString("type_name"));
                    list.add(c);
                }
                rs.close();
                ps.close();
            }
            resp.getWriter().write(com.wardrobe.model.Result.success(list).toJson());
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(com.wardrobe.model.Result.error("查询失败: " + e.getMessage()).toJson());
        } finally {
            com.wardrobe.util.DBUtil.close(conn);
        }
    }

    /**
     * 查询服装详情 — 使用原生 JDBC
     */
    private void getDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.getWriter().write(Result.error("缺少服装ID").toJson());
            return;
        }
        java.sql.Connection conn = null;
        try {
            conn = com.wardrobe.util.DBUtil.getConnection();
            String sql = "SELECT c.id, c.name, c.image, c.price, c.type_id, c.style, " +
                         "c.description, c.stock, c.create_time, c.update_time, t.type_name " +
                         "FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE c.id = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idStr));
            java.sql.ResultSet rs = ps.executeQuery();
            Clothes clothes = null;
            if (rs.next()) {
                clothes = new Clothes();
                clothes.setId(rs.getInt("id"));
                clothes.setName(rs.getString("name"));
                clothes.setImage(rs.getString("image"));
                clothes.setPrice(rs.getBigDecimal("price"));
                clothes.setTypeId(rs.getInt("type_id"));
                clothes.setStyle(rs.getString("style"));
                clothes.setDescription(rs.getString("description"));
                clothes.setStock(rs.getInt("stock"));
                clothes.setCreateTime(rs.getTimestamp("create_time"));
                clothes.setUpdateTime(rs.getTimestamp("update_time"));
                clothes.setTypeName(rs.getString("type_name"));
            }
            rs.close();
            ps.close();
            if (clothes == null) {
                resp.getWriter().write(Result.error("服装不存在").toJson());
            } else {
                resp.getWriter().write(Result.success(clothes).toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("查询失败: " + e.getMessage()).toJson());
        } finally {
            com.wardrobe.util.DBUtil.close(conn);
        }
    }

    /**
     * 添加服装（后台管理用）
     */
    private void addClothes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String typeIdStr = req.getParameter("typeId");
        String style = req.getParameter("style");
        String description = req.getParameter("description");
        String stockStr = req.getParameter("stock");
        String image = req.getParameter("image");

        Clothes clothes = new Clothes();
        clothes.setName(name);
        clothes.setPrice(priceStr != null ? new BigDecimal(priceStr) : BigDecimal.ZERO);
        clothes.setTypeId(typeIdStr != null ? Integer.parseInt(typeIdStr) : null);
        clothes.setStyle(style);
        clothes.setDescription(description);
        clothes.setStock(stockStr != null ? Integer.parseInt(stockStr) : 0);
        clothes.setImage(image);

        Result result = clothesService.addClothes(clothes);
        resp.getWriter().write(result.toJson());
    }

    /**
     * 更新服装
     */
    private void updateClothes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String typeIdStr = req.getParameter("typeId");
        String style = req.getParameter("style");
        String description = req.getParameter("description");
        String stockStr = req.getParameter("stock");
        String image = req.getParameter("image");

        Clothes clothes = new Clothes();
        clothes.setId(Integer.parseInt(idStr));
        clothes.setName(name);
        clothes.setPrice(priceStr != null ? new BigDecimal(priceStr) : BigDecimal.ZERO);
        clothes.setTypeId(typeIdStr != null ? Integer.parseInt(typeIdStr) : null);
        clothes.setStyle(style);
        clothes.setDescription(description);
        clothes.setStock(stockStr != null ? Integer.parseInt(stockStr) : 0);
        clothes.setImage(image);

        Result result = clothesService.updateClothes(clothes);
        resp.getWriter().write(result.toJson());
    }

    /**
     * 删除服装
     */
    private void deleteClothes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        Result result = clothesService.deleteClothes(Integer.parseInt(idStr));
        resp.getWriter().write(result.toJson());
    }
}
