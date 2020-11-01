package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

public class Monitor {
    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
    private final Lock lock;
    private final int maxThreads;
    private int currentPriority = 0;

    public Monitor(Lock lock, int maxThreads) {
        this.lock = lock;
        this.maxThreads = maxThreads;
    }

    public Lock getLock() {
        return lock;
    }

    public boolean isMyQueue(int priority) {
        return priority == currentPriority;
    }

    public void setNextExecutorPriority() {
        currentPriority++;
        if(currentPriority >= maxThreads) currentPriority = 0;
    }

    public int getCurrentPriority() {
        return currentPriority;
    }
}
