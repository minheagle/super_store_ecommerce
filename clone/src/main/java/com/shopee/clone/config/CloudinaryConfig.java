package com.shopee.clone.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.product.folder}")
    private String productFolder;

    @Value("${cloudinary.avatar.folder}")
    private String avatarFolder;

    @Value("${cloudinary.comment.folder}")
    private String commentFolder;

    @Bean
    public Cloudinary cloudinary() {
        String cloudinaryUrl = "cloudinary://" + apiKey + ":" + apiSecret + "@" + cloudName;
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        return cloudinary;
    }

    public String getProductFolder() {
        return productFolder;
    }

    public String getAvatarFolder() {
        return avatarFolder;
    }

    public String getCommentFolder() {
        return commentFolder;
    }
}
