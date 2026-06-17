package com.wardrobe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 购物车实体类
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer clothesId;
    private String clothesName;
    private String clothesImage;
    private BigDecimal clothesPrice;
    private Integer sizeId;
    private String sizeName;
    private Integer quantity;
    private Timestamp addTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getClothesId() { return clothesId; }
    public void setClothesId(Integer clothesId) { this.clothesId = clothesId; }

    public String getClothesName() { return clothesName; }
    public void setClothesName(String clothesName) { this.clothesName = clothesName; }

    public String getClothesImage() { return clothesImage; }
    public void setClothesImage(String clothesImage) { this.clothesImage = clothesImage; }

    public BigDecimal getClothesPrice() { return clothesPrice; }
    public void setClothesPrice(BigDecimal clothesPrice) { this.clothesPrice = clothesPrice; }

    public Integer getSizeId() { return sizeId; }
    public void setSizeId(Integer sizeId) { this.sizeId = sizeId; }

    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Timestamp getAddTime() { return addTime; }
    public void setAddTime(Timestamp addTime) { this.addTime = addTime; }

    /** 计算小计 */
    public BigDecimal getSubtotal() {
        if (clothesPrice == null || quantity == null) return BigDecimal.ZERO;
        return clothesPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
