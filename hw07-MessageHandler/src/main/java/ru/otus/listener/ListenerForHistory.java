package ru.otus.listener;

import ru.otus.Message;
import ru.otus.history.Memento;
import ru.otus.history.MessageHistory;

// Реализация _todo: 4. Сделать Listener для ведения истории: старое сообщение - новое
public class ListenerForHistory implements Listener {
    private MessageHistory messageHistory;

    public ListenerForHistory(MessageHistory messageHistory) {
        this.messageHistory = messageHistory;
    }

    // Сохраняем историю: старое сообщение - новое
    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        messageHistory.push(new Memento(newMsg, oldMsg));
    }

}
