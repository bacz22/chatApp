package com.bac.chatApp.service.impl;

import com.bac.chatApp.service.ICloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService implements ICloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String upload(MultipartFile file) throws IOException {
        var result =  cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
           "folder", "upload",
                "user_filename", true,
                "resource_type", "auto"
        ));
        return result.get("secure_url").toString();
    }
}
