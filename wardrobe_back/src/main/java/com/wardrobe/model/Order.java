package com.wardrobe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单实体类
 * status: 0=待发货, 1=已发货, 2=已完成, 3=已取消
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String orderNo;
    private Integer userId;
    private String username;
    private String clothesDetail;
    private BigDecimal totalPrice;
    private Integer status;
    private String statusText;
    private String address;
    private String phone;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getClothesDetail() { return clothesDetail; }
    public void setClothesDetail(String clothesDetail) { this.clothesDetail = clothesDetail; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) {
            switch (status) {
                case 0: statusText = "待发货"; break;
                case 1: statusText = "已发货"; break;
                case 2: statusText = "已完成"; break;
                case 3: statusText = "已取消"; break;
                default: statusText = "未知"; break;
            }
        }
    }

    public String getStatusText() { return statusText; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    public Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(Timestamp updateTime) { this.updateTime = updateTime; }
}
