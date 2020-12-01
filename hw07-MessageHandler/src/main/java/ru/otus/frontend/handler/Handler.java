package ru.otus.frontend.handler;

import ru.otus.frontend.Message;
import ru.otus.frontend.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
