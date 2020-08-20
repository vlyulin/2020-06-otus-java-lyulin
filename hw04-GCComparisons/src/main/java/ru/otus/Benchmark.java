package ru.otus;

public class Benchmark {

    private final int loopCounter;
    private volatile int size = 0;

    public Benchmark(int loopCounter, int size) {
        this.loopCounter = loopCounter;
        this.size = size;
    }

    public void run() throws InterruptedException {
        for (int idx = 0; idx < loopCounter; idx++) {
            MemorableClass memorableClass = new MemorableClass(size);
            memorableClass.populate();
            // удаляем почти половину
//            memorableClass.list.subList(size/2, memorableClass.list.size()).clear();
            Thread.sleep(5);
        }
    }
}
