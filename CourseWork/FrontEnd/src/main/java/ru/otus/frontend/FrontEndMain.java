package ru.otus.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"ru.otus.frontend", "ru.otus.messagesystem", "ru.otus.netsystem"})
public class FrontEndMain {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        SpringApplication.run(FrontEndMain.class, args);
    }
}
