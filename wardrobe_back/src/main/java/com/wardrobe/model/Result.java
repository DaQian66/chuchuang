package com.wardrobe.model;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 统一响应结果类
 * 所有接口返回 JSON 格式的标准响应
 */
public class Result {
    private int code;     // 200=成功, 400=失败
    private String msg;   // 提示信息
    private Object data;  // 返回数据

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Result() {}

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(String msg) {
        return new Result(200, msg, null);
    }

    public static Result success(Object data) {
        return new Result(200, "成功", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result error(String msg) {
        return new Result(400, msg, null);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }

    // 转换为 JSON 字符串
    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
