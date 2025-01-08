package com.untitled.server.untitled.global.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration  //설정클래스
@EnableConfigurationProperties(DataConfig.class)    //DataConfig 클래스에 정의된 프로퍼티를 Spring 환경 속성으로 매핑하여 사용 가능하게 만듦
@EnableJpaRepositories(basePackages = {"com.untitled.server.untitled"}, //JPA repository 설정, basePackages : JPA repository가 위치한 패키지 경로
        entityManagerFactoryRef = AppDataConfig.ENTITY_MANAGER_BEAN_NAME,   //EntityManagerFactoryRef 빈이름을 AppDataConfig.ENTITY_MANAGER_BEAN_NAME 으로 설정 
        transactionManagerRef = AppDataConfig.TRANSACTION_MANAGER_BEAN_NAME) //transactionManagerRef 빈이름을 AppDataConfig.TRANSACTION_MANAGER_BEAN_NAME 으로 설정 
public class AppDataConfig {

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "appDBTransactionManager";
    public static final String ENTITY_MANAGER_BEAN_NAME = "appDBEntityManager";
    private static final String DATASOURCE_BEAN_NAME = "appDataSource";
    private static final String DATASOURCE_PROPERTIES_PREFIX = "spring.datasource.app";
    private static final String DATASOURCE_PROPERTIES = "appDataSourceProperties";
    private static final String HIBERNATE_PROPERTIES = "appHibernateProperties";

    @Primary    //우선순위 Bean
    @Bean(name = ENTITY_MANAGER_BEAN_NAME)
    public LocalContainerEntityManagerFactoryBean entityManager(
                                                    EntityManagerFactoryBuilder builder, //JPA EntityManagerFactory를 생성하는데 사용
                                                    @Qualifier(DATASOURCE_BEAN_NAME) DataSource dataSource, //
                                                    @Qualifier(HIBERNATE_PROPERTIES) DataConfig.Hibernate hibernateProperties) {
        // JPA 엔티티 매니저를 구성
        // 데이터 소스와 Hibernate설정하여 EntityManagerFactory 빈 생성
        return builder.dataSource(dataSource).packages("com.untitled.server.untitled")
                .persistenceUnit(ENTITY_MANAGER_BEAN_NAME)  //JPA 영속성 유닛 이름 설정
                .properties(DataConfig.Hibernate.propertiesToMap(hibernateProperties)).build();
    }

    @Bean(name = HIBERNATE_PROPERTIES)
    @ConfigurationProperties(prefix = DATASOURCE_PROPERTIES_PREFIX + ".hibernate")  //ConfigurationProperties을 사용하여 프로퍼티파일에 정의된 설정값을 자동으로 매핑
    public DataConfig.Hibernate hibernateProperties() {
        return new DataConfig.Hibernate();
    }

    @Bean(name = DATASOURCE_PROPERTIES)
    @ConfigurationProperties(DATASOURCE_PROPERTIES_PREFIX)
    public DataSourceProperties dataSourceProperties() {    //데이터소스 설정 값
        return new DataSourceProperties();
    }

    //데이터 소스 설정
    //데이터 소스 객체(HikariCP)생성
    @Primary
    @Bean(name = DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = DATASOURCE_PROPERTIES_PREFIX + ".hikari")
    public DataSource dataSource(@Qualifier(DATASOURCE_PROPERTIES) DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    //트랜잭션 매니저 설정
    //JPA를 사용하는 트랜잭션 관리자를 생성
    @Primary
    @Bean(name = TRANSACTION_MANAGER_BEAN_NAME)
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_BEAN_NAME)EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory); //JPA기반 트랜잭션 관리 제공
    }

    @Bean
    @Profile("local")   //테스트에서만 runScript에 필요한 sql 스크립트이기에 'local' profile에서만 bean을 등록한다.
    public DataSourceInitializer dataSourceInitializer(@Qualifier(DATASOURCE_BEAN_NAME) DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("app-data.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
