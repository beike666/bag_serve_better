package com.example.bag_serve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: scholarship_serve
 * @description
 * @author: BeiKe
 * @create: 2021-02-05 16:32
 **/
@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").
                addResourceLocations(
                        "file:"+System.getProperty("user.dir")+
                                System.getProperty("file.separator")+
                                "file"+System.getProperty("file.separator")
                        );
    }
}
