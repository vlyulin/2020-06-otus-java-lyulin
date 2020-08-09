package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GCStatisticsCollector {

    public static final String END_OF_MINOR_GC = "end of minor GC";
    public static final int LOOP_COUNTER = 1000;
    public static final int LIST_SIZE = 5 * 1000 * 1000;
    private static HashMap<String, Long> statistics = new HashMap<>();
    private static List<Long> minorGCDuration = new ArrayList<>();

    // Запуск:

    // UseSerialGC
    // java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar gradleGCComparisons-0.1.jar
    // использование стандартного логирования
    // java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -jar gradleGCComparisons-0.1.jar

    // UseG1GC
    // java -XX:+UseG1GC -Xms512m -Xmx512m -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar gradleGCComparisons-0.1.jar
    // использование стандартного логирования
    // java -XX:+UseG1GC -Xms512m -Xmx512m -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -jar gradleGCComparisons-0.1.jar
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int loopCounter = LOOP_COUNTER; // Количество повторений создания элементов
        // Для оценки времени работы GC
        int size = LIST_SIZE; // Количество создаваемых элементов в List
        // Для memory leak
        // int size = 5 * 10000; // Количество создаваемых элементов в List

        Benchmark benchmark = new Benchmark(loopCounter, size);
        benchmark.run();

        final long appDuration = (System.currentTimeMillis() - beginTime) / 1000;
        resultOutput(appDuration, statistics);
    }

    private static void resultOutput(long appDuration, HashMap<String, Long> statistics) {
        System.out.println("Application uptime: " + appDuration + " sec.");
        System.out.println("Garbage collector statistics:");
        statistics.forEach((k, v) -> {
            double percentage = ((double) v / 1000.0 / (double) appDuration) * 100.0;
            System.out.println("gc name: " + k + "; " + (v / 1000.0)
                    + " sec.; percentage of total running time: " + percentage + "%");
        });

        Long totalStopTheWorldDuration = minorGCDuration.stream().mapToLong(Long::longValue).sum();
        int  numberOfElements = minorGCDuration.size();
        System.out.println("Average duration for StopTheWorld, sec: "
                + (totalStopTheWorldDuration / 1000.0 /numberOfElements));
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcAction = info.getGcAction();
                    long duration = info.getGcInfo().getDuration();

                    statistics.compute(gcAction, (k, v) -> v == null ? duration : v + duration);
                    // Collect StopTheWorld durations
                    if(END_OF_MINOR_GC.equals(gcAction)) { minorGCDuration.add(duration); }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
