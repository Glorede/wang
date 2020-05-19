package com.majiangcoummunity.majiangcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.majiangcoummunity.majiangcommunity.mapper")
public class MajiangcommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajiangcommunityApplication.class, args);
    }

}
