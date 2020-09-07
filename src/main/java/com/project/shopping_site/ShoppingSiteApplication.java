package com.project.shopping_site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ShoppingSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingSiteApplication.class, args);
    }

}
