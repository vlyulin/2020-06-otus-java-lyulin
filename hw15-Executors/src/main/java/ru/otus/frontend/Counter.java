package ru.otus.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.abs;

public class Counter {

    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    Monitor monitor;
    // Приоритет выполнения
    private final int priority;
    // Начальное значение
    private int initialValue;
    // Максимальное значение до которого выполняется подсчет
    private int maxValue;
    // значение, на которое увеличивается/уменьшается текущее значение
    private int increment;
    private int currentValue;
    private int direction;

    Counter(Monitor monitor, int priority, int initialValue, int maxValue, int increment) {

        if (increment == 0) {
            throw new RuntimeException("increment == 0");
        }
        // Положительный порядок перебора
        if (Integer.signum(increment) == 1 && initialValue > maxValue) {
            throw new RuntimeException("Increment > 0 & InitialValue > MaxValue");
        }
        // Отрицательный порядок перебора
        if (Integer.signum(increment) == -1 && initialValue < maxValue) {
            throw new RuntimeException("Increment < 0 & InitialValue < MaxValue");
        }
        this.monitor = monitor;
        this.priority = priority;
        this.currentValue = initialValue;
        this.initialValue = initialValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.direction = Integer.signum(increment);
    }

    public void go() {
        do {
            synchronized (monitor) {
                try {
                    while (!monitor.isMyQueue(this.priority)) {
                        monitor.wait();
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + currentValue);

                    if (
                            (direction == 1 && (currentValue + increment) > maxValue)
                                    || (direction == -1 && (currentValue + increment) < maxValue)
                    ) {
                        // Переключаем направление в обратную сторону
                        increment *= -1;
                    }

                    currentValue += increment;

                    // Передаем очередь следующему
                    monitor.setNextExecutorPriority();
                    monitor.notifyAll();
                } catch (InterruptedException e) {
                    logger.info("interrupted.");
                }
            }
        } while (
            // усложненное условие только ради распечатки последнего номера
                (direction == 1 && currentValue > (initialValue - abs(increment)))
                        || (direction == -1 && currentValue < (initialValue + abs(increment)))
        );
    }
}
