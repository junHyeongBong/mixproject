package com.untitled.server.untitled.global.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.untitled.server.untitled.global.common.enums.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class BaseResponse<T> {

    public static final int BASE_ERROR_CODE = 400;
    public static final int BASE_SUCCESS_CODE = 200;
    public static final String REQUEST_SUCCESS = "OK";

    private Integer code;
    private String message;
    private String systemMessage;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime responseTime;
    private T resData;

    public BaseResponse() {
        this.responseTime = LocalDateTime.now();
    }

    public BaseResponse(int code, String message, String systemMessage) {
        this.code = code;
        this.message = message;
        this.systemMessage = systemMessage;
        this.responseTime = LocalDateTime.now();
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.resData = data;
        this.responseTime = LocalDateTime.now();
    }

    public static <T> BaseResponse<T> OK() {
        BaseResponse response = new BaseResponse();
        response.setCode(BASE_SUCCESS_CODE);
        response.setMessage(null);

        return response;
    }

    public static <T> BaseResponse<T> OK(T data) {
        BaseResponse response = new BaseResponse();
        response.setCode(BASE_SUCCESS_CODE);
        response.setMessage(REQUEST_SUCCESS);
        if(data != null) response.setResData(data);

        return response;
    }

    public static <T> BaseResponse<T> ERROR(String message) {
        BaseResponse response = new BaseResponse();
        response.setCode(BASE_ERROR_CODE);
        response.setMessage(message);
        response.setResponseTime(LocalDateTime.now());

        return response;
    }

    public static <T> BaseResponse<T> ERROR(Exception e) {
        BaseResponse response = new BaseResponse();
        response.setCode(BASE_ERROR_CODE);
        response.setMessage(e.getMessage());
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    public static <T> BaseResponse<T> ERROR(MessageSource messageSource, ErrorInfo errorInfo) {
        BaseResponse response = BaseResponse.builder()
                .code(errorInfo.getCode())
                .message(messageSource.getMessage(errorInfo.getMessageCode(), null, LocaleContextHolder.getLocale()))
                .systemMessage(errorInfo.getMessage())
                .responseTime(LocalDateTime.now())
                .build();
        return response;
    }

    public static <T> BaseResponse<T> ERROR(MessageSource messageSource, ErrorInfo errorInfo, String errorMsg) {
        BaseResponse response = BaseResponse.builder()
                .code(errorInfo.getCode())
                .message(messageSource.getMessage(errorInfo.getMessageCode(), null, LocaleContextHolder.getLocale()))
                .systemMessage(errorMsg)
                .responseTime(LocalDateTime.now())
                .build();
        return response;
    }

    public static <T> BaseResponse<T> ERROR(Integer code, String message, String systemMessage) {
        return new BaseResponse(code, message, systemMessage);
    }

}
