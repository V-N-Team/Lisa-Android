package ht.lisa.app.ui.main;

import ht.lisa.app.model.Message;

public class GetMessageEvent {
    Message message;

    public GetMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessageFromEvent() {
        return message;
    }
}
