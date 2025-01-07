package com.untitled.server.untitled.global.config;

import com.untitled.server.untitled.global.common.AppConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {

    public static final String SCHEMA_HEADER = "bearer";
    public static final String SPRINGDOC_TITLE = "Api";
    public static final String SPRINGDOC_DESC = "무제API";

    private Info apiInfo() {
        return new Info()
                .title(SPRINGDOC_TITLE)
                .description(SPRINGDOC_DESC)
                .version(AppConstants.APP_VERSION);
    }

    @Profile({"local"})
    @Bean
    public OpenAPI openAPI() {  //JWT 설정 바꾸기
//        String securityJwtName = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
//        Components components = new Components()
//                .addSecuritySchemes(securityJwtName, new SecurityScheme()
//                        .name(securityJwtName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme(SCHEMA_HEADER)
//                        .bearerFormat(securityJwtName)
//                        .in(SecurityScheme.In.HEADER).name("Authorization")
//                );
//        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(components)
//                .info(apiInfo());

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());

    }


}
