package com.intabia.spring.boot.starter.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.minio")
public class MinIOProperties {
    /**
     * URL
     */
    private String url = "http:127.0.0.1:9000";
    /**
     * Логин
     */
    private String accessKey = "access_key";
    /**
     * Пароль
     */
    private String secretKey = "secret_key";
    /**
     * Регион
     */
    private String region = "eu-west-1";
}