package ru.otus.processor;

import ru.otus.Message;
import ru.otus.processor.exceptions.ProxyLoggerException;

import java.time.LocalDateTime;
import java.util.concurrent.*;


// Реализация _todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду
public class ProxyLoggerProcessor implements Processor {

    private LoggerProcessor loggerProcessor;

    public ProxyLoggerProcessor(LoggerProcessor loggerProcessor) {
        this.loggerProcessor = loggerProcessor;
    }

    @Override
    public Message process(Message message) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture < ? > handle =
                scheduler.scheduleWithFixedDelay( new Runnable() {
                    public void run () {
                            LocalDateTime now = LocalDateTime.now();
                            int second = now.getSecond();
                            if(second % 2 == 0) {
                                throw new ProxyLoggerException("Raised on " + second + " second. "
                                        + message.toString());
                            }
                    }
                } , 0 , 1 , TimeUnit.SECONDS );

        try {
            handle.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            if(!handle.isCancelled()) handle.cancel(true);
            throw new ProxyLoggerException(e.getMessage());
        }

        return message;
    }
}
