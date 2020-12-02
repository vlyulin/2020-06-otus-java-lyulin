package ru.otus.history;

import java.util.Stack;

public class MessageHistory {
    private Stack<Memento> messageHistory = new Stack<>();

    public void push(Memento memento) {
        messageHistory.push(memento);
    }

    public Memento pop() {
        return messageHistory.pop();
    }
}
