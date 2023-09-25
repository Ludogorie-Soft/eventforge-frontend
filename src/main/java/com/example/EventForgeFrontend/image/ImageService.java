package com.example.EventForgeFrontend.image;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.exception.ImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${image.directory.path}")
    private String imageDirectoryPath;


    private String getImageAbsolutePath() {
        Path uploadDirectory = Paths.get(imageDirectoryPath);
        String sanitizedFileName = sanitizeFileName(randomUUID().toString());

        Path filePath = uploadDirectory.resolve(sanitizedFileName);
        return filePath.toAbsolutePath().toString();
    }


    public void uploadImage(MultipartFile file , String imageUrl) {
        Path uploadDirectory = Paths.get(imageDirectoryPath);

        Path filePath = uploadDirectory.resolve(imageUrl);

        try {
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }
            if (Files.exists(uploadDirectory)) {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new ImageException("Грешка със запазването на файла. Моля уверете се , че подавате валиден файл и не е празен.");
        }
    }

    public static String encodeImage(String url) {
        String base64Image = "";
        File file = new File(url);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte[] imageData = new byte[(int) file.length()];
            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = imageInFile.read(imageData, totalBytesRead, imageData.length - totalBytesRead)) != -1) {
                totalBytesRead += bytesRead;
                if (totalBytesRead == imageData.length) {
                    break;
                }
            }
            base64Image = Base64.getEncoder().encodeToString(Arrays.copyOf(imageData, totalBytesRead));

        } catch (FileNotFoundException e) {
            throw new ImageException("Изображението не е намерено");
        } catch (IOException e) {
            throw new ImageException("Грешка с прочитането на изображението");
        }
        return base64Image;
    }

    private String sanitizeFileName(String fileName) {
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.";

        String sanitizedFileName = fileName.replace("..", "");

        StringBuilder sb = new StringBuilder();
        for (char c : sanitizedFileName.toCharArray()) {
            if (allowedCharacters.indexOf(c) != -1) {
                sb.append(c);
            }
        }
        sanitizedFileName = sb.toString();

        int maxFileNameLength = 255;
        if (sanitizedFileName.length() > maxFileNameLength) {
            throw new ImageException("Името на файла надвишава максималната дължина.");
        }

        return sanitizedFileName;
    }


    public String updatePicture(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }
        ImageValidator.isImageValid(file);
        ImageValidator.isSizeLessThan5mb(file);

        return getImageAbsolutePath();
    }

    public String uploadPicture(MultipartFile file, ImageType type) {
        ImageValidator.isImageEmpty(file, type);
        ImageValidator.isImageValid(file);
        ImageValidator.isSizeLessThan5mb(file);
        return getImageAbsolutePath();
    }

    public void encodeCommonEventResponseListImages(List<CommonEventResponse> events) {
        if (events != null && !events.isEmpty()) {
            for (CommonEventResponse event : events) {
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }

    public void encodeCommonEventResponsePageImages(Page<CommonEventResponse> events) {
        if (events != null && !events.isEmpty()) {
            for (CommonEventResponse event : events) {
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }

    public String extractFilenameFromPath(String fullPath) {
        // Define a regex pattern to match the filename
        Pattern pattern = Pattern.compile("([^\\\\/]+)$"); // This pattern works for Windows and Unix-style paths

        // Use a Matcher to find the filename
        Matcher matcher = pattern.matcher(fullPath);

        if (matcher.find()) {
            return matcher.group(1); // The matched filename
        } else {
            return null; // No filename found
        }
    }
}
