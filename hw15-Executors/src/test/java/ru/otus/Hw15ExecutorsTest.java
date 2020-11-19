package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Hw15ExecutorsTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private List<String> referenceOutputForHW = Arrays.asList(
            "Thread priority 0: 0",
            "Thread priority 1: 0",
            "Thread priority 0: 1",
            "Thread priority 1: 1",
            "Thread priority 0: 2",
            "Thread priority 1: 2",
            "Thread priority 0: 3",
            "Thread priority 1: 3",
            "Thread priority 0: 4",
            "Thread priority 1: 4",
            "Thread priority 0: 5",
            "Thread priority 1: 5",
            "Thread priority 0: 6",
            "Thread priority 1: 6",
            "Thread priority 0: 7",
            "Thread priority 1: 7",
            "Thread priority 0: 8",
            "Thread priority 1: 8",
            "Thread priority 0: 9",
            "Thread priority 1: 9",
            "Thread priority 0: 10",
            "Thread priority 1: 10",
            "Thread priority 0: 9",
            "Thread priority 1: 9",
            "Thread priority 0: 8",
            "Thread priority 1: 8",
            "Thread priority 0: 7",
            "Thread priority 1: 7",
            "Thread priority 0: 6",
            "Thread priority 1: 6",
            "Thread priority 0: 5",
            "Thread priority 1: 5",
            "Thread priority 0: 4",
            "Thread priority 1: 4",
            "Thread priority 0: 3",
            "Thread priority 1: 3",
            "Thread priority 0: 2",
            "Thread priority 1: 2",
            "Thread priority 0: 1",
            "Thread priority 1: 1",
            "Thread priority 0: 0",
            "Thread priority 1: 0"
    );

    private List<String> referenceForThreeThreads = Arrays.asList(
            "Thread priority 0: -1",
            "Thread priority 1: -1",
            "Thread priority 2: -1",
            "Thread priority 0: 0",
            "Thread priority 1: 0",
            "Thread priority 2: 0",
            "Thread priority 0: 1",
            "Thread priority 1: 1",
            "Thread priority 2: 1",
            "Thread priority 0: 0",
            "Thread priority 1: 0",
            "Thread priority 2: 0",
            "Thread priority 0: -1",
            "Thread priority 1: -1",
            "Thread priority 2: -1"
    );

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testHomeWorkTask() throws InterruptedException, IOException {
        Hw15Executors hw15Executors = new Hw15Executors();
        hw15Executors.start();

        List<String> strings = getStringsArray(outputStreamCaptor);
        assertThat(strings).containsExactlyElementsOf(referenceOutputForHW);
    }

    @Test
    public void testThreeThreads() throws InterruptedException {
        Hw15Executors hw15Executors = new Hw15Executors(3, -1, 1, 1);
        hw15Executors.start();

        List<String> strings = getStringsArray(outputStreamCaptor);
        assertThat(strings).containsExactlyElementsOf(referenceForThreeThreads);
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