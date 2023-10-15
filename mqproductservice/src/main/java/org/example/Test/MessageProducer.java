package org.example.Test;

import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private final MessageQueue messageQueue;

    public MessageProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void sendMessage(String message) {
        messageQueue.sendMessage(message);
    }
}
