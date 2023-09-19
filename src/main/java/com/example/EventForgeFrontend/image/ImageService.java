package com.example.EventForgeFrontend.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${digital.ocean.bucket.name}")
    private String digitalOceanBucketName;
    @Value("${space.bucket.origin.url}")
    private String spaceBucketOriginUrl;
    @Autowired
    private AmazonS3 amazonS3Client;


    public String uploadImage(final MultipartFile file ,String randomUuid) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            String contentType = file.getContentType();
            metadata.setContentType(contentType);
            metadata.setHeader("x-amz-acl", "public-read"); // publicly accessible, comment this to not publicly accessible
            PutObjectResult result = amazonS3Client.putObject(digitalOceanBucketName,randomUuid , file.getInputStream(), metadata);
            // Get the file's input stream
            InputStream inputStream = file.getInputStream();

            // Close the input stream
            inputStream.close();

            return randomUuid;
        } catch (IOException e) {
            // Handle any exceptions
            log.warn(e.getMessage());
        }
        return null;
    }

    public String encodeImage(String objectKey) {
        try {
            // Fetch the image from your cloud storage using the AmazonS3 client
            S3Object s3Object = amazonS3Client.getObject(digitalOceanBucketName, objectKey);

            // Read the image bytes and encode them as a base64 string
            byte[] imageBytes = IOUtils.toByteArray(s3Object.getObjectContent());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Close the S3Object to release resources
            s3Object.close();

            return base64Image;
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
            return null;
        }
    }

    public String updatePicture(MultipartFile file ) {

        if(file == null || file.isEmpty()){
            return null;
        }
            ImageValidator.isImageValid(file);
            ImageValidator.isSizeLessThan5mb(file);

        return randomUUID().toString();
    }
    public String uploadPicture(MultipartFile file, ImageType type) {
        ImageValidator.isImageEmpty(file , type);
        ImageValidator.isImageValid(file);
        ImageValidator.isSizeLessThan5mb(file);
        return randomUUID().toString();
    }

    public void encodeCommonEventResponseListImages(List<CommonEventResponse> events){
        if(events!=null && !events.isEmpty()){
            for(CommonEventResponse event : events){
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }
    public void encodeCommonEventResponsePageImages(Page<CommonEventResponse> events){
        if(events!=null && !events.isEmpty()){
            for(CommonEventResponse event : events){
                event.setImageUrl(encodeImage(event.getImageUrl()));
            }
        }
    }
}
