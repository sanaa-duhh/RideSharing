package com.sanaa.RideShare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "rideshare";
    }

    @Override
    public com.mongodb.client.MongoClient mongoClient() {
        System.out.println("=== MONGO URI FROM CONFIG: " + mongoUri + " ===");
        return com.mongodb.client.MongoClients.create(mongoUri);
    }
}