package com.jh.rabbit;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RabbitMqListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        log.info("\nMessage received: {},\n-----body: {}", message, new String(message.getBody()));
    }

//    @Override
//    public void containerAckMode(AcknowledgeMode mode) {
//
//    }
//
//    @Override
//    public void onMessageBatch(List<Message> messages) {
//
//    }
}
