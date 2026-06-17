package com.wardrobe.dao;

import com.wardrobe.model.Size;
import com.wardrobe.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

/**
 * 尺码数据访问层
 */
public class SizeDao {
    private QueryRunner qr = new QueryRunner();

    /**
     * 根据类型ID查询所有尺码
     */
    @SuppressWarnings("unchecked")
    public List<Size> getSizesByTypeId(Integer typeId) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, type_id AS typeId, size_name AS sizeName FROM t_size WHERE type_id = ? ORDER BY id";
            BeanListHandler<Size> handler = new BeanListHandler<>(Size.class);
            return qr.query(conn, sql, handler, typeId);
        } finally {
            DBUtil.close(conn);
        }
    }
}
