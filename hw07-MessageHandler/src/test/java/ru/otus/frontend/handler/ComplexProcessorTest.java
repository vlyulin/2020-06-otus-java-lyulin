package ru.otus.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.Message;
import ru.otus.history.MessageHistory;
import ru.otus.listener.Listener;
import ru.otus.listener.ListenerForHistory;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorChangeFields;
import ru.otus.processor.ProxyLoggerProcessor;
import ru.otus.processor.exceptions.ProxyLoggerException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем последовательность вызова процессоров")
    void handleProcessorsTest() {
        //given
        var message = new Message.Builder().field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(eq(message))).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(eq(message))).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });

        //when
        var result = complexProcessor.handle(message);

        //then
        verify(processor1, times(1)).process(eq(message));
        verify(processor2, times(1)).process(eq(message));
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        //given
        var message = new Message.Builder().field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(eq(message))).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(eq(message))).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        //then
        verify(processor1, times(1)).process(eq(message));
        verify(processor2, never()).process(eq(message));
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        //given
        var message = new Message.Builder().field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {
        });

        complexProcessor.addListener(listener);

        //when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        //then
        verify(listener, times(1)).onUpdated(eq(message), eq(message));
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

    @Test
    @DisplayName("Тестируем замену значений полей")
    void interchangeFiledValuesTest() {
        // given
        var message = new Message.Builder().field11("field11").field13("field13").build();
        ProcessorChangeFields processorChangeFields = new ProcessorChangeFields("field13", "field11");

        // when
        Message msg = processorChangeFields.process(message);

        // then
        assertThat(msg.getField11()).isEqualTo("field13");
        assertThat(msg.getField13()).isEqualTo("field11");
    }

    @Test
    @DisplayName("Тестирование генерации исключения на четную секунду")
    void thrownExceptionOnEvenSecond() {
        //given
        var message = new Message.Builder().field11("field11").field13("field13").build();
        ProxyLoggerProcessor proxyLoggerProcessor = new ProxyLoggerProcessor(
                new LoggerProcessor(
                        new ProcessorChangeFields("field11", "field13")
                )
        );

        // then
        assertThatExceptionOfType(ProxyLoggerException.class).isThrownBy(() -> proxyLoggerProcessor.process(message));
        // TODO: Как проверить, что исключение было на четную секунду, я не знаю
    }

    @Test
    @DisplayName("Тестирование Listener")
    void listenerTest() {
        //given
        var message = new Message.Builder().field11("field11").field13("field13").build();
        // var changesMessage = new Message.Builder().field11("field13").field13("field11").build();
        ProcessorChangeFields processorChangeFields = new ProcessorChangeFields("field13", "field11");

        List<Processor> processors = List.of(
                new ProcessorChangeFields("field13", "field11")
        );

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println(ex.getMessage());
        });

        MessageHistory messageHistory = new MessageHistory();
        var listenerForHistory = new ListenerForHistory(messageHistory);
        complexProcessor.addListener(listenerForHistory);

        // when
        complexProcessor.handle(message);
        // MessageHistory messageHistory = listenerForHistory.getMessageHistory();

        // then
        assertThat(messageHistory.pop()).isNotNull();
    }
}