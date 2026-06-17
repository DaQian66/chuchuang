package com.wardrobe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 服装实体类
 */
public class Clothes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer typeId;
    private String typeName;      // 服装类型名称（联表查询时用）
    private String style;
    private String description;
    private Integer stock;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    public Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(Timestamp updateTime) { this.updateTime = updateTime; }
}
