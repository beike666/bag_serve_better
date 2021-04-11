package com.example.bag_serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.bag_serve.mapper")
@SpringBootApplication
public class BagServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BagServeApplication.class, args);
    }

}
