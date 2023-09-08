package com.shopee.clone.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.folder}")
    private String folder;

    @Bean
    public Cloudinary cloudinary() {
        String cloudinaryUrl = "cloudinary://" + apiKey + ":" + apiSecret + "@" + cloudName;
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        return cloudinary;
    }

    public String getFolder() {
        return folder;
    }
}
