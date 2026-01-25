package com.bac.chatApp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudinaryService {
    String upload(MultipartFile file) throws IOException;
}
