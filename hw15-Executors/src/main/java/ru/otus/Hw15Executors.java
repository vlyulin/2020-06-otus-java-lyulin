package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// https://www.baeldung.com/java-testing-system-out-println
public class Hw15Executors {

    private static final Logger logger = LoggerFactory.getLogger(Hw15Executors.class);
    private static final int MAX_THREADS = 2;
    private static final int INITIAL_VALUE = 0;
    private static final int MAX_VALUE = 10;
    private static final int INCREMENT = 1;

    private int threadsNumber;
    private int initialValue;
    private int maxValue;
    private int increment;
    private final Lock lock = new ReentrantLock();

    private List<Thread> threads = new ArrayList<>(MAX_THREADS);

    public static void main(String[] args) throws InterruptedException {
        if(args.length == 0) {
            new Hw15Executors().start();
        }
        else {
            if(args.length != 4) {
                throw new RuntimeException("Wrong argument number.");
            }
            new Hw15Executors(
                    Integer.getInteger(args[0]),
                    Integer.getInteger(args[1]),
                    Integer.getInteger(args[2]),
                    Integer.getInteger(args[3])
            ).start();
        }
    }

    public Hw15Executors() {
        this.threadsNumber = MAX_THREADS;
        this.initialValue = INITIAL_VALUE;
        this.maxValue = MAX_VALUE;
        this.increment = INCREMENT;
    }

    public Hw15Executors(int threadsNumber, int initialValue, int maxValue, int increment) {
        this.threadsNumber = threadsNumber;
        this.initialValue = initialValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }

    public void start() throws InterruptedException {
        Monitor monitor = new Monitor(threadsNumber);
        for (int priority = 0; priority < threadsNumber; priority++) {
            Counter counter = new Counter(monitor, priority, initialValue, maxValue, increment);
            Thread thread = new Thread(() -> counter.go());
            thread.setName("Thread priority " + priority);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
