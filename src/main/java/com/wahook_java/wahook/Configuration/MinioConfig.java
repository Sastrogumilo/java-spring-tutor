package com.wahook_java.wahook.Configuration;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accesskey}")
    private String accesskey;

    @Value("${minio.secretkey}")
    private String secretkey;

    @Bean
    public MinioClient minioClient() {
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder().build();
            return new MinioClient.Builder()
                    .endpoint(this.url)
                    .credentials(this.accesskey, this.secretkey)
                    .httpClient(httpClient)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinioClient", e);
        }
    }
}