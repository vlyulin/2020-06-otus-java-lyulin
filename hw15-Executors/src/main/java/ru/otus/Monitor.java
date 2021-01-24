package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monitor {
    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
    private final int maxThreads;
    private int currentPriority = 0;

    public Monitor(int maxThreads) {
        this.maxThreads = maxThreads;
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
