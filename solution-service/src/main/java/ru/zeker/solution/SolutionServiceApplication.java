package ru.zeker.solution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SolutionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolutionServiceApplication.class, args);
    }

}
