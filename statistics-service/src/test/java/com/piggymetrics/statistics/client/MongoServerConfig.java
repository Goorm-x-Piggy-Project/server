package com.piggymetrics.statistics.client;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class MongoServerConfig {

    @Bean
    public MongoServer mongoServer() {
        // In-memory MongoDB 서버 생성 및 포트 명시
        MongoServer server = new MongoServer(new MemoryBackend());
        server.bind("localhost", 27017); // 명시적으로 포트 27017 사용
        return server;
    }

    @Bean
    @Primary
    public MongoClient mongoClient() {
        // MongoDB 서버 연결
        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress("localhost",27017)))
                ).build());
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "test-database");
    }
}
