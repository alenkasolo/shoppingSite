package com.project.shopping_site.AWSServices;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {
    String uploadFile(MultipartFile multipartFile, String name);
}
