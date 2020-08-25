package com.myapp.gallary.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileServices {

    public String storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);
}
