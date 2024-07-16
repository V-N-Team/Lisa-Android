package ht.lisa.app.ui.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ht.lisa.app.model.Message;

public class GetMessageIdSetEvent {
    Set<String> messageSet = new HashSet<>();

    public GetMessageIdSetEvent(List<Message> messageSet) {
        for (Message message : messageSet) {
            this.messageSet.add(message.getMsgId());
        }
    }

    public Set<String> getMessageSetFromEvent() {
        return messageSet;
    }
}
