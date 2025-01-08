package com.untitled.server.untitled.global.config;

import com.untitled.server.untitled.global.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //WebMvcConfigurer 사용용도
    //1. 인터셉터 등록 - request 하기 전후로 처리할 작업 있을때 커스텀 인터셉터 구성용도 ex) global 인터셉터를 등록하여 매번 로깅이나 보안 검사 할수있음
    //2. 뷰리졸버 - 컨트롤러에서 반환된 뷰이름이 실제 오브젝트 변환하는 과정 정의할수있음
    //3. 리소스 핸들링 - 리소스 위치 설정
    //4. 메세지 변환 - HTTP 메세지 컨버터를 추가하여 JSON, XML 과 같은 형식의 데이터를 읽고 쓸수 있는 메세지 변환기를 등록할수있음
    //5. Path Matching, Content Negotitation
    //6. CORS Configuration - 다른 도메인에 의해 접근되어야 할 때 필요한 CORS 규칙을 정의할수있다.
//    5. ex )
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        //true일시 /api/resource = api/resource/ false일시 서로 다른 경로로 인식
//        configurer.setUseTrailingSlashMatch(true);
//    }
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(false)  //확장자를 통해 JSON, XML 형식을 판단하지않음 /api/resource.json or /api/resource.xml
//                  .favorParameter(true)       //클라이언트가 url 쿼리 파라미터로 요청 형식을 지정할수있음 /api/resource?mediaType=xml 로 요청하면 xml 응답
//                  .parameterName("mediaType") // /api/resource?mediaType=xml 에서 mediaType (xml)값에 따라 응답 형식 결정
//                  .ignoreAcceptHeader(true)   // 요청헤더에 Accept: application/json 정보가 있더라도 무시하고 파라미터나 기본 설정에 따라 응답 형식을 결정
//                  .useRegisteredExtensionsOnly(false) //확장자 매칭에서 등록되지 않은 확장자도 허용, xml , json 외에도 다른 확장자를 사용할수있음
//                  .defaultContentType(MediaType.APPLICATION_JSON) //요청에 응답 형식을 명시하지않은 경우 기본으로 application/json 형식을 사용
//                  .mediaType("xml", MediaType.APPLICATION_XML);   //xml을 MediaType.APPLICATION_XML에 매핑 /api/resource?mediaType=xml로 요청 시 XML 응답을 반환.
//    }
//    필요에 따라 ignoreAcceptHeader(false)로 변경해 Accept 헤더 기반 협상도 허용.
//    확장자를 활용해야 한다면 favorPathExtension(true)를 활성화.
//    다양한 응답 형식을 지원하려면 추가 mediaType을 등록.

    private LoggingInterceptor loggingInterceptor;

    public WebConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4027" //로컬테스트 front
//                        ,"https://www.naver.com"
                )
                .exposedHeaders("Content-Type", "Content-Disposition", "Date", "Authorization", "Meta-Date")    //Meta-Date는 Custom Header
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}
