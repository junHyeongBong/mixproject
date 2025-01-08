package com.untitled.server.untitled.global.handler;

import com.untitled.server.untitled.global.common.enums.ErrorInfo;
import com.untitled.server.untitled.global.common.response.BaseResponse;
import com.untitled.server.untitled.global.exception.CustomException;
import com.untitled.server.untitled.global.util.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice   //어플리케이션 전역에서 발생하는 예외를 잡아 처리 할수있게함
public class UntitledExceptionHandler { //여러 예외 처리하고 사용자 정의 응답 객체 (BaseResponse)로 반환

    @Autowired
    private MessageSource messageSource;    //다국어 메시지 지원을 위한 클래스 LocaleContextHolder.getLocale()을 사용해 요청 언어 설정에 따라 메시지 가져옴

    // CustomException 핸들러
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public final BaseResponse customExceptionHandler(CustomException e, HttpServletRequest request) {
        LogUtil.warn(UntitledExceptionHandler.class, e, e.getMessage());

        try {
            String message = messageSource.getMessage(e.getMessageCode(), e.getArgs(), LocaleContextHolder.getLocale());
            if (e.getArgs() != null) {
                for(int i=0; i<e.getArgs().length; i++) {
                    message = message.replace("{" + i + "}", e.getArgs()[i]);
                }
            }
            return BaseResponse.ERROR(e.getCode(), message, e.getMessage());
        } catch (NoSuchMessageException e1) {
            return BaseResponse.ERROR(e.getCode(), e.getMessage(), e.getMessage());
        }
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseBody
    public final BaseResponse authorizationDeniedExceptionHandle(Exception e, HttpServletRequest request) {
        LogUtil.error(UntitledExceptionHandler.class, e, e.getMessage());
        return BaseResponse.ERROR(messageSource, ErrorInfo.SYSTEM_AUTH_ERROR, e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public final BaseResponse handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        LogUtil.warn(UntitledExceptionHandler.class, e, "Validation error : " + e.getMessage());

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Validation failed for fields: ");

        for(FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            errorMessage.append(fieldError.getField())
                    .append(" (")
                    .append(localizedErrorMessage)
                    .append("), ");
        }

        if(errorMessage.length() > 0) {
            errorMessage.setLength(errorMessage.length() - 2);
        }

        return BaseResponse.ERROR(messageSource, ErrorInfo.INVALID_PARAM, errorMessage.toString());
    }

    //범용적인 Exception 처리
    public final BaseResponse globalExceptionHandle(Exception e, HttpServletRequest request) {
        if(e instanceof CustomException) return customExceptionHandler((CustomException) e, request);

        LogUtil.error(UntitledExceptionHandler.class, e, "Unexpected error occurered: " + e.getMessage());
        return BaseResponse.ERROR(messageSource, ErrorInfo.SYSTEM_ERROR, e.getMessage());
    }

}
