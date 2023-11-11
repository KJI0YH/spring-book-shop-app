package com.example.mybookshopapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.book-covers}")
    private String uploadBookCoversPath;
    
    @Value("${upload.author-covers}")
    private String uploadAuthorCoversPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/book-covers/**").addResourceLocations("file:" + uploadBookCoversPath + "/");
        registry.addResourceHandler("/author-covers/**").addResourceLocations("file:" + uploadAuthorCoversPath + "/");
    }
}
