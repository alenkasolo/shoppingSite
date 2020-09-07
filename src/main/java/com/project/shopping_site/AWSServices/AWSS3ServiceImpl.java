package com.project.shopping_site.AWSServices;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
@Service
public class AWSS3ServiceImpl implements AWSS3Service{
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);
    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Autowired
    public AWSS3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String name) {
        String uniqueFileName = null;
        try {
            final File file = convertMultipartFileToFile(multipartFile, name);
            uniqueFileName = uploadFileToS3Bucket(bucketName, file);
            file.delete();
        } catch (final AmazonServiceException exception) {
            LOGGER.info("upload file is failed");
            LOGGER.error("ERROR = {} while uploading file", exception.getMessage());
        }
        return uniqueFileName;
    }
    private File convertMultipartFileToFile(final MultipartFile multipartFile, String name) {
        final File file = new File(name);
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ioException) {
            LOGGER.error("error converting multi-part file to file = {}", ioException.getMessage());
        }
        return file;
    }
    private String uploadFileToS3Bucket(final String bucketName, final File file) {
        final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
        LOGGER.info("uploading file with name = {}", uniqueFileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        amazonS3.putObject(putObjectRequest);
        return uniqueFileName;
    }

}


