package ru.otus;

import com.google.common.base.Joiner;

public class HelloOtus {

    private int year = 2020;
    private int month = 6;
    private String trainingCompany = "otus";
    private String courseName = "java";
    private String student = "lyulin";

    public static void main(String... main) {
        HelloOtus helloOtus = new HelloOtus();
        System.out.println(helloOtus);
    }

    // Get string like 2020-06-otus-java-lyulin by using Guava possibilities
    @Override
    public String toString() {
        Joiner joiner = Joiner.on("-").skipNulls();
        return joiner.join(
                year,
                String.format("%02d", month),
                trainingCompany,
                courseName,
                student
        );
    }
}
