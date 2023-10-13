package org.example.controller;

import net.minidev.json.JSONObject;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.example.message.ResponseMsg;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/mq")
public class MqMessageController {
    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.sync-tag}")
    private String syncTag;
    @Value(value = "${rocketmq.producer.topic2}:${rocketmq.producer.sync-tag}")
    private String syncTag2;
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/broadcast/{id}")
    public ResponseMsg get(@PathVariable int id){
        // 构建消息
        String messageStr = "广播: " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送同步消息
        SendResult sendResult = rocketMQTemplate.syncSend(syncTag, message);
        ResponseMsg msg = new ResponseMsg();
        // 解析发送结果
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            msg.setSuccessData(sendResult.getMsgId() + " : " + sendResult.getSendStatus());
        }
        return msg;


    }
    @GetMapping("/p2p/{id}")
    public ResponseMsg gets(@PathVariable int id){
        // 构建消息
        String messageStr = "p2p : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送同步消息
        SendResult sendResult = rocketMQTemplate.syncSend(syncTag2, message);
        ResponseMsg msg = new ResponseMsg();
        // 解析发送结果
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            msg.setSuccessData(sendResult.getMsgId() + " : " + sendResult.getSendStatus());
        }
        return msg;


    }
}
