package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group2}", topic = "${rocketmq.consumer.topic2}",
        selectorExpression = "${rocketmq.consumer.tags}", messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.ORDERLY)

@Slf4j
public class Consumer2 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}