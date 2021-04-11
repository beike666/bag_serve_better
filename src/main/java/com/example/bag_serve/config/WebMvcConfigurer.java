package com.example.bag_serve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-03-30 13:51
 **/
@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("*").allowedHeaders("*")
                .maxAge(3600);
    }
}
