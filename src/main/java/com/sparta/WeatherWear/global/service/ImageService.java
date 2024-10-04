package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
작성자 : 이승현
* S3에 파일을 업로드 하거나 삭제하는 역할을 수행합니다.
*/
@Service
public class ImageService {

    private static final String UPLOAD_DIR = "/var/www/uploads/";

    /* 파일 업로드 */
    public String uploadFile(File file) throws IOException {
        // 파일을 서버의 디렉터리에 저장
        String key = generateUniqueFileName();
        File destinationFile = new File(UPLOAD_DIR + key);

        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 반환 URL (Nginx의 서버 도메인과 경로를 맞춰야 함)
        return String.format("https://dltmdgus9661.iptime.org/uploads/%s", key);
    }

    /* 서버에 없는 유니크 이름을 생성하는 기능 */
    private String generateUniqueFileName() {
        String key;
        File file;
        do {
            key = UUID.randomUUID().toString() + ".webp";
            file = new File(UPLOAD_DIR + key);
        } while (file.exists());
        return key;
    }

    /* 파일 삭제 기능 */
    public void deleteFileByUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        File file = new File(UPLOAD_DIR + key);
        if (file.exists()) {
            file.delete();
        } else {
            throw new IllegalArgumentException("File not found: " + fileUrl);
        }
    }

    /* URL로부터 파일 이름을 추출하는 기능 */
    private String extractKeyFromUrl(String fileUrl) {
        String pattern = "https://dltmdgus9661.iptime.org/uploads/(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileUrl);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("Invalid URL: " + fileUrl);
        }
    }
}
