package ru.otus.frontend;

import ru.otus.frontend.handler.ComplexProcessor;
import ru.otus.frontend.history.MessageHistory;
import ru.otus.frontend.listener.ListenerForHistory;
import ru.otus.frontend.processor.LoggerProcessor;
import ru.otus.frontend.processor.ProcessorChangeFields;
import ru.otus.frontend.processor.ProxyLoggerProcessor;

import java.util.List;

import static java.lang.System.exit;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13
       2. Сделать процессор, который поменяет местами значения field11 и field13
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду
       4. Сделать Listener для ведения истории: старое сообщение - новое
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элементов "to do" создать new ComplexProcessor и обработать сообщение
         */
        var processors = List.of(
                new ProcessorChangeFields("field13", "field11"),
                new ProxyLoggerProcessor(
                        new LoggerProcessor(
                                new ProcessorChangeFields("field13", "field11"))));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println(ex.getMessage());
        });
        var listenerForHistory = new ListenerForHistory(new MessageHistory());
        complexProcessor.addListener(listenerForHistory);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13("field13")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerForHistory);
        exit(1);
    }
}
