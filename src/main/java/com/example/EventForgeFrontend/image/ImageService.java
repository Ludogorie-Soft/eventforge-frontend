package com.example.EventForgeFrontend.image;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.exception.ImageException;
import jakarta.annotation.Nullable;
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
import java.util.*;

@Service
public class ImageService {
    private static final String FOLDER_PATH_ORGANISATION = "src/main/resources/static/images/organisation";
    private static final String FOLDER_PATH_EVENTS = "src/main/resources/static/images/events";


    public String uploadImageToFileSystem(MultipartFile file, ImageType imageType) {

            String fileName = file.getOriginalFilename();

            String sanitizedFileName = null;
            if (fileName != null) {
                sanitizedFileName = sanitizeFileName(fileName);

            }
            Path uploadDirectory = null;
            if (imageType.equals(ImageType.LOGO) || imageType.equals(ImageType.COVER)) {
                uploadDirectory = Paths.get(FOLDER_PATH_ORGANISATION);
            }
           else {
                uploadDirectory = Paths.get(FOLDER_PATH_EVENTS);
            }
            assert sanitizedFileName != null;
            Path filePath = uploadDirectory.resolve(sanitizedFileName);
            String imageUrlAbsolutePath = filePath.toAbsolutePath().toString();

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
            return imageUrlAbsolutePath;

    }

    public String updatePicture(MultipartFile file , ImageType type ) {
        if(file == null || file.isEmpty()){
            return null;
        }
            ImageValidator.isImageValid(file);
        return uploadImageToFileSystem(file, type);
    }
    public String uploadPicture(MultipartFile file, ImageType type) {
        ImageValidator.isImageEmpty(file , type);
        ImageValidator.isImageValid(file);
        return uploadImageToFileSystem(file, type);
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


    @Nullable
    String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    protected void deleteImageFile(String filePath) {
        Path fileToDelete = Paths.get(filePath);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new ImageException("Грешка при опит за изтриването на файла.");
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
    public static void encodeCommonEventResponseListImages(List<CommonEventResponse> events){
        if(events!=null && !events.isEmpty()){
            for(CommonEventResponse event : events){
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }
    public static void encodeCommonEventResponsePageImages(Page<CommonEventResponse> events){
        if(events!=null && !events.isEmpty()){
            for(CommonEventResponse event : events){
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }
}
