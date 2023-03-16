package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message msgNew = copyMessage(msg);
        messageMap.put(msgNew.getId(), msgNew);
    }

    private Message copyMessage(Message msg) {
        ObjectForMessage field13 = getCopyOfField13(msg);
        Message msgNew = new Message.Builder(msg.getId())
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(field13)
                .build();
        return msgNew;
    }

    private ObjectForMessage getCopyOfField13(Message msg) {
        ObjectForMessage field13 = new ObjectForMessage();
        if (msg.getField13() != null) {
            List<String> newData = new ArrayList<>(msg.getField13().getData());
            field13.setData(newData);
        }

        return field13;
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        Message message = messageMap.get(id);
        return Optional.of(message);
    }
}
