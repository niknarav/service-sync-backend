package com.example.servicesyncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ServiceSyncUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSyncUserServiceApplication.class, args);
    }

}
