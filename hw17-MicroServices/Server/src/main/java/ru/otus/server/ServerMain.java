package ru.otus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.netsystem.Listener;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.otus.server", "ru.otus.messagesystem"})
public class ServerMain {
    private static ApplicationContext context;

    public static void main(String[] args) throws IOException {
        context = SpringApplication.run(ServerMain.class, args);
        Listener listener = context.getBean(Listener.class);
        listener.start();
    }
}
