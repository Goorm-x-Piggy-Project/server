package com.piggymetrics.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDB 및 Spring Data MongoTemplate 설정.
 */
@Configuration
public class MongoConfig {

    private final MongoDatabaseFactory mongoDatabaseFactory;

    public MongoConfig(MongoDatabaseFactory mongoDatabaseFactory) {
        this.mongoDatabaseFactory = mongoDatabaseFactory;
    }

    /**
     * MongoTemplate 빈 등록.
     * Spring Data MongoDB의 핵심 클래스.
     *
     * @return MongoTemplate 인스턴스
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}
