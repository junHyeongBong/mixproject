package com.untitled.server.untitled.domain.api.auth.exception;

import com.untitled.server.untitled.global.common.enums.ErrorInfo;
import com.untitled.server.untitled.global.exception.CustomException;
import org.springframework.context.MessageSource;

public class MemberAuthException extends CustomException {

    public MemberAuthException(MessageSource messageSource) {
        super(messageSource);
    }

    public MemberAuthException(Throwable e) {
        super(e);
    }

    public MemberAuthException(int code, String messageCode, String systemMessage) {
        super(code, messageCode, systemMessage);
    }

    public MemberAuthException(String message) {
        super(message);
    }

    public MemberAuthException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public MemberAuthException(ErrorInfo errorInfo, String systemMessage) {
        super(errorInfo, systemMessage);
    }

    public MemberAuthException(ErrorInfo errorInfo, String... message) {
        super(errorInfo, message);
    }

    public MemberAuthException(Throwable cause, ErrorInfo errorInfo) {
        super(cause, errorInfo);
    }

}
