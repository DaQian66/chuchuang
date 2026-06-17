package com.wardrobe.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 服装类型实体类
 */
public class Type implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String typeName;
    private Timestamp createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
}
