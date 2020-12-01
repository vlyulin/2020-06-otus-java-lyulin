package ru.otus.frontend.listener;

import ru.otus.frontend.Message;

public class ListenerPrinter implements Listener {

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        var logString = String.format("oldMsg:%s, newMsg:%s", oldMsg, newMsg);
        System.out.println(logString);
    }
}
