package com.untitled.server.untitled.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    public static Logger getLogger(Class c) {
        if(c != null) {
            return LoggerFactory.getLogger(c);
        }
        return LoggerFactory.getLogger(LogUtil.class);
    }

    private static String messageCreate(String... msg) {
        StringBuilder sb = new StringBuilder();
        if(msg != null) {
            for(String m : msg) {
                // \n 으로 단락 구분 시 grep 으로 로그 조회 시 관련 로그에 포함되지 않아 제거하였습니다.
                // sb.append(m + "\n");
                sb.append(m).append(" ");
            }
        }
        return sb.toString();
    }

    public static void debug(Class c, Object o) {
        getLogger(c).debug("[Param] {}", o);
    }

    public static void debug(Class c, String... msg) {
        getLogger(c).debug(messageCreate(msg));
    }

    public static void info(Class c, String... msg) {
        getLogger(c).info(messageCreate(msg));
    }

    public static void warn(Class c, String... msg) {
        getLogger(c).warn(messageCreate(msg));
    }

    public static void warn(Class c, Exception e, String... msg) {
        String message = messageCreate(msg);
        if(e != null) {
            getLogger(c).warn(String.format("[Warning Message:%s]", message), e);
        }else {
            getLogger(c).warn(String.format("[Warning Message:%s]", message));
        }
    }

    public static void error(Class c, Exception e, String... msg) {
        String message = messageCreate(msg);
        if(e != null) {
            getLogger(c).error(String.format("[Error Message:%s]", message), e);
        }else {
            getLogger(c).error(String.format("[Error Message:%s]", message));
        }
    }
}
