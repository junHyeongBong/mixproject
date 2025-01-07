package com.untitled.server.untitled.global.common.enums;

import lombok.Getter;

@Getter
public enum ErrorInfo {

    SYSTEM_ERROR(400100, "MSG_400100", "시스템 에러입니다."),
    SYSTEM_AUTH_ERROR(400101, "MSG_400101", "권한 에러입니다.");

    private int code;
    private String message;
    private String messageCode;

    private ErrorInfo(int code, String message, String messageCode) {
        this.code = code;
        this.message = message;
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return code + " " + message + " " + messageCode;
    }

    public String replaceMessage(String... paramArr) {
        String msg = message;
        for (int i = 0; i < paramArr.length; i++) {
            msg = msg.replace("{" + i + "}", paramArr[i]);
        }
        return msg;
    }

}
