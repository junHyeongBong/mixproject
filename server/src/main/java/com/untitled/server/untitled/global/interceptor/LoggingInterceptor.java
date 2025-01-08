package com.untitled.server.untitled.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untitled.server.untitled.global.util.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Slf4j  //Lombok사용하여 로그객체 log 생성
@RequiredArgsConstructor    //final 또는 @NonNull 필드에 대해 생성자 자동 생성
@Component  //Spring 컨테이너에서 이 클래스를 빈으로 관리
public class LoggingInterceptor implements HandlerInterceptor { //Spring MVC에서 제공하는 인터페이스로, 요청/응답 처리를 가로챌수있는 메서드 제공

    private final ObjectMapper objectMapper;    //Jackson 라이브러리의 클래스, JSON 데이터를 객체로 변환하거나 객체를 JSON으로 직렬화 할때 사용

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { //preHandle HTTP요청 처리전에 호출
        String userAgent = request.getHeader("User-Agent"); //클라이언트의 브라우저 또는 사용자 에이전트 정보
        String scheme = request.getScheme();        //요청 프로토콜 http, https...
        String serverName = request.getServerName();    //server의 호스트이름
        Integer serverPort = request.getServerPort();   //요청을 처리한 서버의 포트번호
        String contextPath = request.getContextPath();  //어플리케이션의 컨텍스트 경로
        String servletPath = request.getServletPath();  //서블릿에 대한 매핑경로

        StringBuilder sb = new StringBuilder();
        if(!ObjectUtils.isEmpty(userAgent)) sb.append(String.format("[%s:%s]", "User-Agent", userAgent));
        if(!ObjectUtils.isEmpty(scheme)) sb.append(String.format("[%s:%s]", "Scheme", scheme));
        if(!ObjectUtils.isEmpty(serverName)) sb.append(String.format("[%s:%s]", "ServerName", serverName));
        if(!ObjectUtils.isEmpty(serverPort)) sb.append(String.format("[%s:%s]", "serverPort", serverPort));
        if(!ObjectUtils.isEmpty(contextPath)) sb.append(String.format("[%s:%s]", "contextPath", contextPath));
        if(!ObjectUtils.isEmpty(servletPath)) sb.append(String.format("[%s]", "servletPath", servletPath));

        LogUtil.info(this.getClass(), "IN", sb.toString()); //IN이라는 구분자로 로깅

        return HandlerInterceptor.super.preHandle(request, response, handler);  //true반환 요청처리 계속 진행, 부모클래스 기본 도작 유지
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  //요청이 처리된후 응답본문을 클라이언트로 전송되기전에 호출

        //예외처리 추가 : 요청/응답 데이터가 없거나 잘못된 경우를 대비하여 예외처리 추가
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);
        if(cachingResponseWrapper.getContentType() != null && cachingResponseWrapper.getContentType().contains("application/json")) {
            String responseBody = new String(cachingResponseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.debug("[{}] ResponseBody : {}", request.getMethod(), responseBody);
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
