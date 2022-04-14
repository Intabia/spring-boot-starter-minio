package com.intabia.spring.boot.starter.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация сервиса для взаимодействия с MinIO Client
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIOConfiguration {

    private final MinIOProperties minIOProperties;

    /**
     * Настройка компонентаMinIOService
     */
    @Bean
    @SneakyThrows
    public MinIOService minIOService() {
        return new MinIOService(new MinioClient(
                minIOProperties.getUrl(),
                minIOProperties.getAccessKey(),
                minIOProperties.getSecretKey(),
                minIOProperties.getRegion()
        ));
    }
}