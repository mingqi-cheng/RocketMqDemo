package org.example.Test;

import org.example.Test.MessageConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumerListener {

    private final MessageConsumer messageConsumer;

    public MessageConsumerListener(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Scheduled(fixedRate = 1000) // 每隔1秒执行一次
    public void consumeMessagesPeriodically() {
        messageConsumer.consumeMessages();
    }
}
