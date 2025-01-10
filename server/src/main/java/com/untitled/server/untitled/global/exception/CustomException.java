package com.untitled.server.untitled.global.exception;

import com.untitled.server.untitled.global.common.enums.ErrorInfo;
import org.springframework.context.MessageSource;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -5417898143812461709L;

    private int code;
    private String messageCode;
    private String message;
    private String [] args = null;

    public CustomException(MessageSource messageSource) {
        this.code = ErrorInfo.SYSTEM_ERROR.getCode();
        this.messageCode = ErrorInfo.SYSTEM_ERROR.getMessageCode();
        this.message = ErrorInfo.SYSTEM_ERROR.getMessage();
    }

    public CustomException(Throwable e) {
        this.code = 400;
        this.messageCode = ErrorInfo.SYSTEM_ERROR.getMessageCode();
        this.message = e.getMessage();
    }

    public CustomException(int code, String messageCode, String systemMessage) {
        super(systemMessage);
        this.code = code;
        this.messageCode = messageCode;
        this.message = systemMessage;
    }

    public CustomException(String message) {
        super(message);
        this.code = ErrorInfo.SYSTEM_ERROR.getCode();
        this.messageCode = ErrorInfo.SYSTEM_ERROR.getMessageCode();
        this.message = message;
    }

    public CustomException(ErrorInfo errorInfo) {
        super();
        this.code = errorInfo.getCode();
        this.messageCode = errorInfo.getMessageCode();
        this.message = errorInfo.getMessage();
    }

    public CustomException(ErrorInfo errorInfo, String systemMessage) {
        super(systemMessage);
        this.code = errorInfo.getCode();
        this.messageCode = errorInfo.getMessageCode();
        this.message = systemMessage;
    }

    public CustomException(ErrorInfo errorInfo, String... message) {
        super(errorInfo.replaceMessage(message));
        this.code = errorInfo.getCode();
        this.messageCode = errorInfo.getMessageCode();
        this.args = message;
        this.message = errorInfo.replaceMessage(message);
    }

    public CustomException(Throwable cause, ErrorInfo errorInfo) {
        super(cause);
        this.code = errorInfo.getCode();
        this.messageCode = errorInfo.getMessageCode();
        this.message = errorInfo.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String getMessage() {
        return this.message;
    }

    public ErrorInfo getErrorInfo() {
        return ErrorInfo.SYSTEM_ERROR;
    }


}
