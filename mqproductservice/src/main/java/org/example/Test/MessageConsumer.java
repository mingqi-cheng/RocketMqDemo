package org.example.Test;

import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final MessageQueue messageQueue;

    public MessageConsumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void consumeMessages() {
        while (true) {
            String message = messageQueue.receiveMessage();
            if (message != null) {
                System.out.println("Received message: " + message);
            }
        }
    }
}
