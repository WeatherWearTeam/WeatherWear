package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();

        s3Client.putObject(putObjectRequest, Paths.get(Objects.requireNonNull(file.getOriginalFilename())));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    public void deleteFileByUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String extractKeyFromUrl(String fileUrl) {
        String pattern = String.format("https://%s.s3.%s.amazonaws.com/(.*)", bucketName, region);
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileUrl);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("Invalid S3 URL: " + fileUrl);
        }
    }
}
