package com.bank.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main doesnt sit at root so this annotation is required
@SpringBootApplication(scanBasePackages = "com.bank")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
