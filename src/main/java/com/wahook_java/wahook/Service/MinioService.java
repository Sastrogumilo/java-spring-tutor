package com.wahook_java.wahook.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.InputStream;

@Service
public class MinioService {

    private MinioClient minioClient;

    @Value("${minio.url}")
    private String url;

    private String bucket = "wahook";

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public Object uploadFile(String filename, InputStream content, String bucket, String contentType){

        if(bucket == null || bucket == "") bucket = this.bucket;
        if(contentType == null || contentType == "") contentType = "application/octet-stream";

        try {

            this.minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .stream(content, content.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url + bucket + "/" + filename;
    }

}
