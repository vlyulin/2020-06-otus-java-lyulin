package ru.otus.dbserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DbServerMain {
    private static ApplicationContext context;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        context = SpringApplication.run(DbServerMain.class, args);
    }
}
