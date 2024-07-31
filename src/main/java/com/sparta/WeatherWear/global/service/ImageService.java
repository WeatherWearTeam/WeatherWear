package com.sparta.WeatherWear.global.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@Service
public class ImageService {

    @Value("${image.upload.dir}") // 이미지 저장 디렉토리를 설정 파일에서 주입 받음
    private String uploadDir;

    /* 이미지를 WEBP로 변환해서 업로드하는 기능 */
    public String uploadImagefile(String dir, String filename, MultipartFile file) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(dir + filename + ".tmp");
        File tempFile = new File(filePath.toString());

        if (!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
        file.transferTo(tempFile);

        // 원본 파일 정보
        System.out.println("Original file size: " + Files.size(tempFile.toPath()) + " bytes");
        System.out.println("Original file format: " + getFileFormat(tempFile));

        // 웹피 형식으로 변환
        File webPFile = convertToWebP(tempFile, filename);
        tempFile.delete();

        // 변환된 웹피 파일 정보
        System.out.println("Converted file size: " + Files.size(webPFile.toPath()) + " bytes");
        System.out.println("Converted file format: webp");
        return dir + webPFile.getName();
    }


    private String getFileFormat(File file) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                return reader.getFormatName();
            }
        }
        return "unknown";
    }


    private File convertToWebP(File imageFile, String filename) throws IOException {
        ImmutableImage image = ImmutableImage.loader().fromFile(imageFile);
        File outputFile = new File(Paths.get(imageFile.getParent(), filename).toString() + ".webp");
        image.output(WebpWriter.DEFAULT, outputFile);
        return outputFile;
    }

    public void deleteImage(String imagePath) throws IOException {
        Path absolutePath = Paths.get(uploadDir, imagePath).normalize();
        File file = absolutePath.toFile();
        if (file.exists()) {
            Files.delete(absolutePath);
            System.out.println("Deleted file: " + absolutePath.toString());
        } else {
            System.out.println("File not found: " + absolutePath.toString());
        }
    }


/*jpeg로 변환하는 방법*/
//    public String uploadImagefileToJPG(String filename, MultipartFile file,int height, int width, double quality) throws IOException {
//        Path filePath = Paths.get(uploadDir).resolve(filename + ".jpg"); // 저장할 경로
//        File tempFile = new File(filePath.toString() + ".tmp"); // 임시 파일 경로
//        File finalFile = new File(filePath.toString()); // 최종 파일 경로
//
//        // 디렉토리가 존재하지 않으면 생성
//        if (!Files.exists(filePath.getParent())) {
//            Files.createDirectories(filePath.getParent());
//        }
//
//        file.transferTo(tempFile); // 파일 저장
//
//        // 원본 파일 정보
//        System.out.println("Original file size: " + Files.size(tempFile.toPath()) + " bytes");
//        System.out.println("Original file format: " + getFileFormat(tempFile));
//
//        /* 확장자를 확인해서 변환 */
//        if ("webp".equalsIgnoreCase(getFileExtension(file))) {
//            System.out.println("webp 변환 시작");
//            BufferedImage image = convertWebPToBufferedImage(tempFile);
//            // PNG 파일로 저장
//            File pngFile = new File(filePath.toString() + ".png");
//            saveImageAsPNG(image, pngFile);
//            tempFile.delete();
//            tempFile = pngFile;
//        }
//
//        // 이미지 리사이즈 및 화질 조절
//        BufferedImage bufferedImage = Thumbnails.of(tempFile)
//                .size(width, height) // 대상 크기
//                .outputQuality(quality) // 화질
//                .asBufferedImage();
//
//        // JPEG 형식으로 저장 (임시 파일)
//        saveImageAsJPEG(convertToRGB(bufferedImage), finalFile);
//        tempFile.delete();
//        // 임시 파일을 원본 파일로 덮어쓰기
//        Files.move(finalFile.toPath(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//
//        System.out.println("Resized file size: " + Files.size(finalFile.toPath()) + " bytes");
//        System.out.println("Resized file format: jpg"); // 항상 JPEG
//
//        // 이미지 URL 생성
//        return "/images/" + filePath.getFileName();
//    }
//
//    private String getFileExtension(MultipartFile file) {
//        String originalFilename = file.getOriginalFilename();
//        if (originalFilename != null) {
//            int dotIndex = originalFilename.lastIndexOf('.');
//            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
//                return originalFilename.substring(dotIndex + 1).toLowerCase();
//            }
//        }
//        return "";
//    }
//
//    // 이미지를 jpeg 형식으로 변환합니다.
//    private void saveImageAsJPEG(BufferedImage image, File outputFile) throws IOException {
//        // 이미지 출력 스트림 생성
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
//            // JPEG 이미지 라이터를 가져옴
//            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next(); // JPEG 형식으로 설정
//            writer.setOutput(ios);
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
//            writer.dispose();
//        }
//    }
//
//    private void saveImageAsPNG(BufferedImage image, File outputFile) throws IOException {
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
//            ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
//            writer.setOutput(ios);
//            writer.write(image);
//            writer.dispose();
//        }
//    }
//
//    private BufferedImage convertToRGB(BufferedImage image) {
//        // 이미지 색상 공간을 RGB로 변환
//        if (image.getType() != BufferedImage.TYPE_INT_RGB) {
//            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//            ColorConvertOp convertToRGB = new ColorConvertOp(image.getColorModel().getColorSpace(), rgbImage.getColorModel().getColorSpace(), null);
//            convertToRGB.filter(image, rgbImage);
//            return rgbImage;
//        }
//        return image;
//    }

//    private BufferedImage convertWebPToBufferedImage(File webpFile) throws IOException {
//        try (ImageInputStream iis = ImageIO.createImageInputStream(webpFile)) {
//            ImageReader reader = ImageIO.getImageReadersByFormatName("webp").next();
//            reader.setInput(iis);
//            return reader.read(0);
//        }
//    }

}
