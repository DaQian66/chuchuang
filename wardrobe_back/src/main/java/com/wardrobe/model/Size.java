package com.wardrobe.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 尺码实体类
 */
public class Size implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer typeId;
    private String sizeName;

    public Size() {}
    public Size(Integer typeId, String sizeName) {
        this.typeId = typeId;
        this.sizeName = sizeName;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }
}
