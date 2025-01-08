package com.untitled.server.untitled.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//DB와 관련된 설정을 관리하기 위해 작성된 설정클래스
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")  //@ConfigurationProperties를 통해 외부(application.yml or appliation.properties)에서 데이터베이스와 관련된 설정값을 읽어와 객체 매핑
                                                        // spring.datasource로 시작되는 모든 설정값을 읽어와 이 클래스의 속성에 자동으로 바인딩
public class DataConfig {

    @Data
    public static class App {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private Hibernate hibernate;
    }
    @Data
    public static class Hibernate {
        private String ddlAuto; //update, validate, create-drop 등
        private String dialect;
        private Naming naming;

        //Hibernate의 설정값을 Map으로 형태 반환
        //Hibernate의 설정을 동적으로 Entity Managager나 다른 설정에 전달하기 위해 사용
        public static Map<String, Object> propertiesToMap(Hibernate hibernateProperties) {
            Map<String, Object> properties = new HashMap<>();

            if(hibernateProperties.ddlAuto != null) {   //ddl auto 값이 있으면 매핑
                properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto());
            }

            //Naming 전략이 있으면 매핑
            DataConfig.Naming hibernateNaming = hibernateProperties.getNaming();
            if(hibernateNaming != null) {
                if(hibernateNaming.getImplicitStrategy() != null) {
                    properties.put("hibernate.implicit_naming_strategy", hibernateNaming.getImplicitStrategy());
                }
                if(hibernateNaming.getPhysicalStrategy() != null) {
                    properties.put("hibernate.physical_naming_strategy", hibernateNaming.getPhysicalStrategy());
                }
            }

            return properties;
        }
    }

    @Data
    public static class Naming {
        private String implicitStrategy;    //Hibernate의 암시적 네이밍 전략
        private String physicalStrategy;    //Hibernate의 물리적 네이밍 전략
    }

}
