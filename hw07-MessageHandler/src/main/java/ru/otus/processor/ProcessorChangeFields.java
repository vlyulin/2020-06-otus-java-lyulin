package ru.otus.processor;

import ru.otus.Message;
import ru.otus.processor.exceptions.ChangeFieldProcessorException;
import ru.otus.processor.exceptions.MethodInvokeProcessorException;
import ru.otus.processor.exceptions.MethodNotFoundProcessorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessorChangeFields implements Processor {

    private String fieldNameTo;
    private String fieldNameFrom;

    public ProcessorChangeFields(String fieldNameTo, String fieldNameFrom) {
        if(fieldNameTo == null || fieldNameTo.isEmpty() || fieldNameTo.isBlank()) {
            throw new ChangeFieldProcessorException("fieldNameTo is empty.");
        }
        if(fieldNameFrom == null || fieldNameFrom.isEmpty() || fieldNameFrom.isBlank()) {
            throw new ChangeFieldProcessorException("fieldNameFrom is empty.");
        }
        this.fieldNameTo = fieldNameTo;
        this.fieldNameFrom = fieldNameFrom;
    }

    private String getMethodNameByFieldName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private String getValue(Message message, String fieldName) {
        try {
            String methodName = getMethodNameByFieldName(fieldName);
            Method getValueMethod =
                    Message.class.getMethod(getMethodNameByFieldName(fieldName));
            return (String) getValueMethod.invoke(message);

        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundProcessorException(
                    getMethodNameByFieldName(fieldName) + " method not found.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodInvokeProcessorException("Error invoke method " +
                    getMethodNameByFieldName(fieldName) +
                    "Error: " + e.getMessage());
        }
    }

    private void setValue(Message.Builder builder, String fieldName, String value) {
        try {

            Method setValueMethod =
                    Message.Builder.class.getMethod(fieldName, String.class);
            setValueMethod.invoke(builder, value);

        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundProcessorException(
                    "Message.Builder." + fieldName + " method not found.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodInvokeProcessorException("Error invoke method Message.Builder." + fieldName +
                    "; Error: " + e.getMessage());
        }
    }

    @Override
    public Message process(Message message) {
        String fieldFrom = getValue(message, fieldNameFrom);
        String fieldTo = getValue(message, fieldNameTo);
        String interim = new String(fieldTo);
        Message.Builder builder = message.toBuilder();
        setValue(builder,fieldNameTo,fieldFrom);
        setValue(builder,fieldNameFrom,fieldTo);
        return builder.build();
    }
}
