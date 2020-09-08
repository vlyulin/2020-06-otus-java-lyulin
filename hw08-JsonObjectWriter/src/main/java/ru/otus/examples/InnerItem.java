package ru.otus.examples;

import java.util.HashSet;
import java.util.Set;

public class InnerItem {
    Set<Integer> shortTypeSet = new HashSet<>(){{
        add(300);
        add(400);
        add(500);
    }};
}
