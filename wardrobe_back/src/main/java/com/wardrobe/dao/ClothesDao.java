package com.wardrobe.dao;

import com.wardrobe.model.Clothes;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 服装数据访问层
 */
public class ClothesDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 查询所有服装
     */
    @SuppressWarnings("unchecked")
    public List<Clothes> getAllClothes() throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.id AS cid, c.name, c.image, c.price, c.type_id AS ctype_id, c.style, " +
                    "c.description, c.stock, c.create_time AS ctime, c.update_time AS ctime2, " +
                    "t.type_name AS tname " +
                    "FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id " +
                    "ORDER BY c.create_time DESC";
            ResultSetHandler<List<Clothes>> h = new ResultSetHandler<List<Clothes>>() {
                @Override
                public List<Clothes> handle(ResultSet rs) throws SQLException {
                    List<Clothes> list = new java.util.ArrayList<>();
                    while (rs.next()) {
                        Clothes c = new Clothes();
                        c.setId(rs.getInt("cid"));
                        c.setName(rs.getString("name"));
                        c.setImage(rs.getString("image"));
                        c.setPrice(rs.getBigDecimal("price"));
                        c.setTypeId(rs.getInt("ctype_id"));
                        c.setStyle(rs.getString("style"));
                        c.setDescription(rs.getString("description"));
                        c.setStock(rs.getInt("stock"));
                        c.setCreateTime(rs.getTimestamp("ctime"));
                        c.setUpdateTime(rs.getTimestamp("ctime2"));
                        c.setTypeName(rs.getString("tname"));
                        list.add(c);
                    }
                    return list;
                }
            };
            return qr.query(conn, sql, h);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据条件查询服装
     */
    @SuppressWarnings("unchecked")
    public List<Clothes> getClothesByCondition(String typeName, String style, String name) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT c.id AS cid, c.name, c.image, c.price, c.type_id AS ctype_id, c.style, " +
                    "c.description, c.stock, c.create_time AS ctime, c.update_time AS ctime2, " +
                    "t.type_name AS tname FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE 1=1");
            java.util.List<Object> params = new java.util.ArrayList<>();

            if (typeName != null && !typeName.isEmpty()) {
                sql.append(" AND t.type_name LIKE ?");
                params.add("%" + typeName + "%");
            }
            if (style != null && !style.isEmpty()) {
                sql.append(" AND c.style LIKE ?");
                params.add("%" + style + "%");
            }
            if (name != null && !name.isEmpty()) {
                sql.append(" AND c.name LIKE ?");
                params.add("%" + name + "%");
            }
            sql.append(" ORDER BY c.create_time DESC");

            Object[] arr = params.toArray(new Object[0]);
            ResultSetHandler<List<Clothes>> h = new ResultSetHandler<List<Clothes>>() {
                @Override
                public List<Clothes> handle(ResultSet rs) throws SQLException {
                    List<Clothes> list = new java.util.ArrayList<>();
                    while (rs.next()) {
                        Clothes c = new Clothes();
                        c.setId(rs.getInt("cid"));
                        c.setName(rs.getString("name"));
                        c.setImage(rs.getString("image"));
                        c.setPrice(rs.getBigDecimal("price"));
                        c.setTypeId(rs.getInt("ctype_id"));
                        c.setStyle(rs.getString("style"));
                        c.setDescription(rs.getString("description"));
                        c.setStock(rs.getInt("stock"));
                        c.setCreateTime(rs.getTimestamp("ctime"));
                        c.setUpdateTime(rs.getTimestamp("ctime2"));
                        c.setTypeName(rs.getString("tname"));
                        list.add(c);
                    }
                    return list;
                }
            };
            return qr.query(conn, sql.toString(), h, arr);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 根据ID查询服装详情
     */
    @SuppressWarnings("unchecked")
    public Clothes getClothesById(Integer id) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.id AS cid, c.name, c.image, c.price, c.type_id AS ctype_id, c.style, " +
                    "c.description, c.stock, c.create_time AS ctime, c.update_time AS ctime2, " +
                    "t.type_name AS tname " +
                    "FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE c.id = ?";
            ResultSetHandler<Clothes> h = new ResultSetHandler<Clothes>() {
                @Override
                public Clothes handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        Clothes c = new Clothes();
                        c.setId(rs.getInt("cid"));
                        c.setName(rs.getString("name"));
                        c.setImage(rs.getString("image"));
                        c.setPrice(rs.getBigDecimal("price"));
                        c.setTypeId(rs.getInt("ctype_id"));
                        c.setStyle(rs.getString("style"));
                        c.setDescription(rs.getString("description"));
                        c.setStock(rs.getInt("stock"));
                        c.setCreateTime(rs.getTimestamp("ctime"));
                        c.setUpdateTime(rs.getTimestamp("ctime2"));
                        c.setTypeName(rs.getString("tname"));
                        return c;
                    }
                    return null;
                }
            };
            return qr.query(conn, sql, h, id);
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 添加服装
     */
    public void addClothes(Clothes clothes) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO t_clothes (name, image, price, type_id, style, description, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
            qr.update(conn, sql,
                    clothes.getName(), clothes.getImage(), clothes.getPrice(),
                    clothes.getTypeId(), clothes.getStyle(),
                    clothes.getDescription(), clothes.getStock());
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 更新服装
     */
    public void updateClothes(Clothes clothes) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE t_clothes SET name=?, image=?, price=?, type_id=?, " +
                    "style=?, description=?, stock=? WHERE id=?";
            qr.update(conn, sql,
                    clothes.getName(), clothes.getImage(), clothes.getPrice(),
                    clothes.getTypeId(), clothes.getStyle(),
                    clothes.getDescription(), clothes.getStock(), clothes.getId());
        } finally {
            DBUtil.close(conn);
        }
    }

    /**
     * 删除服装
     */
    public void deleteClothes(Integer id) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM t_clothes WHERE id = ?";
            qr.update(conn, sql, id);
        } finally {
            DBUtil.close(conn);
        }
    }
}
