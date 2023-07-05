package com.example.EventForgeFrontend.image;

import com.example.EventForgeFrontend.exception.ImageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {
    public static final String IMAGE_PATTERN = "(.*\\.)(jpg|jpeg|png|gif)$";

    public static void isImageValid(MultipartFile image){

        String filename = image.getOriginalFilename();
        Pattern pattern = Pattern.compile(IMAGE_PATTERN);
        Matcher matcher = pattern.matcher(filename);
        if(!matcher.matches()){
            throw new ImageException("Файлът трябва да поддържа (JPG, JPEG, PNG, или GIF) формати.");
        }
    }

    public static void isImageEmpty(MultipartFile image){
        if(image==null || image.isEmpty()){
            throw new ImageException("Полетата за лого на организация и корица са задължителни , моля прикачете снимки.");
        }
    }
}
