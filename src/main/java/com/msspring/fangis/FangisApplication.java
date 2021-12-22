package com.msspring.fangis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;



@SpringBootApplication
@EnableScheduling
public class FangisApplication {

    public static void main(String[] args) {
        SpringApplication.run(FangisApplication.class, args);
    }
    
}
