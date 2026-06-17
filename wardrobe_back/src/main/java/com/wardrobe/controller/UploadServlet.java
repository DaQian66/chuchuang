package com.wardrobe.controller;

import com.wardrobe.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 图片上传 Servlet
 * 只允许 jpg/jpeg/png 格式，使用时间戳+UUID重命名
 */
@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class UploadServlet extends HttpServlet {
    private static final List<String> ALLOWED_EXTENSIONS =
            Arrays.asList(".jpg", ".jpeg", ".png");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Part part = req.getPart("image");
            if (part == null || part.getSize() == 0) {
                resp.getWriter().write(Result.error("请选择图片文件").toJson());
                return;
            }

            String originalName = part.getSubmittedFileName();
            if (originalName == null || originalName.isEmpty()) {
                resp.getWriter().write(Result.error("文件名无效").toJson());
                return;
            }

            // 获取扩展名
            String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                resp.getWriter().write(Result.error("不支持的图片格式，仅支持 jpg/jpeg/png").toJson());
                return;
            }

            if (part.getSize() > 5 * 1024 * 1024) {
                resp.getWriter().write(Result.error("图片大小不能超过5MB").toJson());
                return;
            }

            // 生成新文件名
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + ext;

            // 获取上传目录
            String uploadDir = getServletContext().getRealPath("/images");
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File destFile = new File(dir, fileName);

            // 手动复制文件内容
            try (InputStream is = part.getInputStream()) {
                Files.copy(is, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 返回访问路径（强转 Object 确保走 success(Object) 而非 success(String msg)）
            String imageUrl = "/wardrobe_back/images/" + fileName;
            resp.getWriter().write(Result.success((Object) imageUrl).toJson());

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(Result.error("上传失败：" + e.getMessage()).toJson());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().write(Result.error("请使用POST请求").toJson());
    }
}
