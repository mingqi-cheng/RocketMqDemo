package org.example.Test;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@Component
public class MessageQueue {

    private final BlockingQueue<String> queue=new LinkedBlockingQueue<>();
    public void sendMessage(String message){

        try {

            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String receiveMessage(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
