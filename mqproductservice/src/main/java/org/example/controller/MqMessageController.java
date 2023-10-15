package org.example.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.checkerframework.checker.units.qual.A;
import org.example.Test.MessageProducer;

import org.example.listener.SendCallbackListener;
import org.example.message.ResponseMsg;
import org.example.pojo.User;
import org.example.server.UserService;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController

@RequestMapping("/mq")
public class MqMessageController {

    @Autowired
    private UserService userService;
    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.sync-tag}")
    private String syncTag;
    @Value(value = "${rocketmq.producer.topic2}:${rocketmq.producer.sync-tag}")
    private String syncTag2;
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private MessageProducer producer;
    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.async-tag}")
    private String asyncag;
    @GetMapping("/test")
    Mono<ResponseMsg> text(@RequestParam("message") String message) {
        producer.sendMessage(message);
        return Mono.just(new ResponseMsg().setSuccessData(66666));
    }
    @GetMapping("/broadcast/{id}")
    public Mono<ResponseMsg> get(@PathVariable int id){
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
        return Mono.just(msg);


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
    @GetMapping("/asyn/{id}")
    public Mono<User> asynGets(@PathVariable int id){


        // 构建消息
        String messageStr = "order id : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送异步消息
        rocketMQTemplate.asyncSend(asyncag, message, new SendCallbackListener(id));
        ResponseMsg msg = new ResponseMsg();
        msg.setSuccessData(null);

        return userService.getById(id);

    }
    @GetMapping("/list")
    public Flux<User> findAll() {

        return userService.findAll();
    }

    @GetMapping("/hello")
    public String hello() {
        long start = System.currentTimeMillis();
        String helloStr = getHelloStr();
        System.out.println("普通接口耗时：" + (System.currentTimeMillis() - start));
        return helloStr;
    }

    @GetMapping("/helloWebFlux")
    public Mono<String> hello0() {
        long start = System.currentTimeMillis();
        Mono<String> hello0 = Mono.fromSupplier(this::getHelloStr);
        System.out.println("WebFlux 接口耗时：" + (System.currentTimeMillis() - start));
        return hello0;
    }

    private String getHelloStr() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }


}
