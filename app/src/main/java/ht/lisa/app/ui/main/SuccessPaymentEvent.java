package ht.lisa.app.ui.main;

import ht.lisa.app.model.Message;

public class SuccessPaymentEvent {
    private Message message;

    public SuccessPaymentEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
