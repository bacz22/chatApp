package com.bac.chatApp.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary configKey(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dusx4ymdy");
        config.put("api_key", "458343336469922");
        config.put("api_secret", "dWGPXlmWLzOB88F-6A6Zb3ik5_k");
        return new Cloudinary(config);
    }
}
