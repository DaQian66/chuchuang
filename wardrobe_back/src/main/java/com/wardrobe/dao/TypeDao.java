package com.wardrobe.dao;

import com.wardrobe.model.Type;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

/**
 * 服装类型数据访问层
 */
public class TypeDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 查询所有类型
     */
    @SuppressWarnings("unchecked")
    public List<Type> getAllTypes() throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, type_name AS typeName FROM t_type ORDER BY id";
            BeanListHandler<Type> handler = new BeanListHandler<>(Type.class);
            return qr.query(conn, sql, handler);
        } finally {
            DBUtil.close(conn);
        }
    }
}
