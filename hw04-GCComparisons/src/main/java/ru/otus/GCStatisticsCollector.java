package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;

public class GCStatisticsCollector {

    private static HashMap<String, Long> statistics = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int loopCounter = 1000; // Количество повторений создания элементов
        // Для оценки времени работы GC
        int size = 5 * 1000 * 1000; // Количество создаваемых элементов в List
        // Для memory leak
//        int size = 5 * 10000; // Количество создаваемых элементов в List

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
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    statistics.compute(gcName, (k, v) -> v == null ? duration : v + duration);
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
