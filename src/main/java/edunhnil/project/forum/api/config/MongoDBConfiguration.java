package edunhnil.project.forum.api.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoDBConfiguration {

    @Bean("authenticationMongoDB")
    @ConfigurationProperties("spring.data.authentication.mongodb")
    public MongoProperties getAuthentication() {
        return new MongoProperties();
    }

    @Bean("authenticationMongoTemplate")
    public MongoTemplate getAuthenticationMongoTemplate() {
        return new MongoTemplate(authenticationMongoFactory(getAuthentication()));
    }

    @Bean
    public MongoDatabaseFactory authenticationMongoFactory(MongoProperties mongo) {
        return new SimpleMongoClientDatabaseFactory(mongo.getUri());
    }
}
