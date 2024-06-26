package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/posts/**")
                .addResourceLocations("classpath:/static/posts/");

        registry.addResourceHandler("/profile-picture/**")
                .addResourceLocations("classpath:/static/profileImg/");

        registry.addResourceHandler("/cover-img/**")
                .addResourceLocations("classpath:/static/coverImg/");
    }
}

