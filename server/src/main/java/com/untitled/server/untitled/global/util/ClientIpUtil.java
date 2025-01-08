package com.untitled.server.untitled.global.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientIpUtil {

    //클라이언트 ip 가져오기
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;

        try {
             ip = request.getHeader("x-forwarded-for");   //프록시나 로드밸런서 사용하는 환경에서 클라이언트의 원래 ip 주소 나타남

            if (ip == null) {
                ip = request.getHeader("Proxy-Client-IP");  //일부 프록시 서버에서 사용하는 헤더
                log.debug("> Proxy-Client-IP: " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("WL-Proxy-Client-IP");   //WebLogin 서버에서 사용하는 헤더
                log.debug("> WL-Proxy-Client-IP: " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                log.debug("> HTTP_CLIENT_IP: " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR"); //프록시 체인에서 클라리언트의 원래 ip를 기록하는데 사용, 프록시를 여러개 사용하는경우 여러ip가 쉼표로 구분되어 나타날수있음
                log.debug("> HTTP_X_FORWARDED_FOR: " + ip);
            }
            if (ip == null) {
                ip = request.getRemoteAddr();   //위 헤더에서 ip를 찾을수없는경우 요청을 보낸 클라이언트의 ip를 직접 가져옴
                log.debug("> REMOTE_ADDR: " + ip);
            }

            log.debug("ClientInfoUtil.getClientIp() ===== client ip ====>  " + ip);

        } catch (NullPointerException e) {
            log.debug(e.getMessage());
        }

        return ip;
    }
}
