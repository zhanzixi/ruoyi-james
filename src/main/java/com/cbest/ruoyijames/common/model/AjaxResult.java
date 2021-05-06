package com.cbest.ruoyijames.common.model;

import cn.hutool.http.HttpStatus;
import lombok.Builder;
import lombok.Data;

/**
 * @author James
 * @date 2021/4/21 15:07
 */
@Builder
@Data
public class AjaxResult<T> {
    private Integer status;
    private String message;
    private String error;
    private T data;

    private static <T> AjaxResult<T> newInstance(Integer status, String message, String error, T data) {
        return (AjaxResult<T>) AjaxResult.builder()
                .status(status)
                .message(message)
                .error(error)
                .data(data).build();
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        return newInstance(HttpStatus.HTTP_OK, message, null, data);
    }

    public static <T> AjaxResult<T> success(T data) {
        return success(null, data);
    }

    public static <T> AjaxResult<T> success() {
        return success(null);
    }

    public static <T> AjaxResult<T> error(int code, String error) {
        return newInstance(code, null, error, null);
    }

    public static <T> AjaxResult<T> error(String error) {
        return error(HttpStatus.HTTP_INTERNAL_ERROR, error);
    }

}
