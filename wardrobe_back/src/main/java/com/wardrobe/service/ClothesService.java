package com.wardrobe.service;

import com.wardrobe.dao.ClothesDao;
import com.wardrobe.model.Clothes;
import com.wardrobe.model.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服装业务逻辑层
 */
public class ClothesService {
    private ClothesDao clothesDao = new ClothesDao();

    /**
     * 查询所有服装
     */
    public Result getAllClothes() {
        try {
            List<Clothes> list = clothesDao.getAllClothes();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 条件查询
     */
    public Result queryClothes(String typeName, String style, String name) {
        try {
            List<Clothes> list = clothesDao.getClothesByCondition(typeName, style, name);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询服装详情
     */
    public Result getClothesDetail(Integer id) {
        try {
            Clothes clothes = clothesDao.getClothesById(id);
            if (clothes == null) {
                return Result.error("服装不存在");
            }
            return Result.success(clothes);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 添加服装
     */
    public Result addClothes(Clothes clothes) {
        if (clothes.getName() == null || clothes.getName().trim().isEmpty()) {
            return Result.error("服装名称不能为空");
        }
        if (clothes.getPrice() == null || clothes.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("价格必须大于0");
        }
        try {
            clothesDao.addClothes(clothes);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 更新服装
     */
    public Result updateClothes(Clothes clothes) {
        if (clothes.getId() == null) {
            return Result.error("服装ID不能为空");
        }
        try {
            clothesDao.updateClothes(clothes);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除服装
     */
    public Result deleteClothes(Integer id) {
        if (id == null) {
            return Result.error("服装ID不能为空");
        }
        try {
            clothesDao.deleteClothes(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}
