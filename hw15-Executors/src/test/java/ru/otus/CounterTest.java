package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    public static final int PRIORITY = 0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private List<String> referenceDirectCountingOutput = Arrays.asList(
            "main: 1",
            "main: 2",
            "main: 3",
            "main: 2",
            "main: 1"
    );

    private List<String> referenceReverseCountingOutput = Arrays.asList(
            "main: -1",
            "main: -2",
            "main: -3",
            "main: -2",
            "main: -1"
    );

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testWrongZeroIncrementParameter() {
        Monitor monitor = new Monitor(1);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new Counter(monitor, PRIORITY, 1, 3, 0);
        });
        assertTrue(thrown.getMessage().contains("increment == 0"));
    }

    @Test
    void testWrongNegativeIncrementParameter() {
        Monitor monitor = new Monitor(1);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new Counter(monitor, PRIORITY, 1, 3, -1);
        });
        assertTrue(thrown.getMessage().contains("Increment < 0 & InitialValue < MaxValue"));
    }

    @Test
    void testWrongPositiveIncrementParameter() {
        Monitor monitor = new Monitor(1);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new Counter(monitor, PRIORITY, -1, -3, 1);
        });
        assertTrue(thrown.getMessage().contains("Increment > 0 & InitialValue > MaxValue"));
    }

    @Test
    void testDirectCounting() {
        Monitor monitor = new Monitor(1);
        Counter counter = new Counter(monitor, PRIORITY, 1, 3, 1);
        counter.go();

        List<String> strings = getStringsArray(outputStreamCaptor);
        assertThat(strings).containsExactlyElementsOf(referenceDirectCountingOutput);
    }

    @Test
    void testReverseCounting() {
        Monitor monitor = new Monitor(1);
        Counter counter = new Counter(monitor, PRIORITY, -1, -3, -1);
        counter.go();

        List<String> strings = getStringsArray(outputStreamCaptor);
        assertThat(strings).containsExactlyElementsOf(referenceReverseCountingOutput);
    }

    private List<String> getStringsArray(final ByteArrayOutputStream byteArrayOutputStream) {
        List<String> strings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(byteArrayOutputStream.toByteArray())))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }
}