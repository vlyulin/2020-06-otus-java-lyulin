package ru.otus.history;

import ru.otus.Message;

public class Memento {
    private final Message newMessageState;
    private final Message oldMessageState;

    public Memento(Message newMessageState, Message oldMessageState) {
        this.newMessageState = newMessageState;
        this.oldMessageState = oldMessageState;
    }

    public Message getNewMessageState() {
        return newMessageState;
    }

    public Message getOldMessageState() {
        return oldMessageState;
    }
}
