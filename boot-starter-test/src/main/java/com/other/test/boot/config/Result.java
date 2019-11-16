package com.other.test.boot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result<T> {
    @ApiModelProperty("响应编号")
    private Integer code;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("响应正文")
    private T data;
    @ApiModelProperty("请求URL")
    private String url;
    @ApiModelProperty("请求时间")
    private String timestamp;

    public static <T> Result<T> success() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getMethod() + " " + request.getRequestURL();

        // "yyyy-mm-dd hh:mm:ss"
        return new Result<>(200, "success", null, url, String.valueOf(System.currentTimeMillis()));
    }

    public static <T> Result<T> success(T data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getMethod() + " " + request.getRequestURL();
        return new Result<>(200, "success", data, url, String.valueOf(System.currentTimeMillis()));
    }

    public static <T> Result<T> success(String msg, T data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getMethod() + " " + request.getRequestURL();
        return new Result<>(200, msg, data, url, String.valueOf(System.currentTimeMillis()));
    }

    public static <T> Result<T> error(String msg, T data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getMethod() + " " + request.getRequestURL();
        return new Result<>(200, msg, data, url, String.valueOf(System.currentTimeMillis()));
    }

    public static <T> Result<T> error(Integer code, String msg, T data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getMethod() + " " + request.getRequestURL();
        return new Result<>(code, msg, data, url, String.valueOf(System.currentTimeMillis()));
    }
}
