package com.jh.rabbit;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Subscriber {

    @Autowired
    Queue queue;
    @Autowired
    Queue queue2Durable;
    @Autowired
    Queue fanoutQueue;
    @Autowired
    Queue topicQueue;
    @Autowired
    Queue topicFileUploadedToMeeting;

    // Dynamically reading the queue name using SpEL from the "queue" object
    @RabbitListener(queues = "#{queue.getName()}")
    private void receive(final String message) {
        log.info("\nReceived the following message from the queue = " + message);
    }

    // Dynamically reading the queue name using SpEL from the "queue" object
    @RabbitListener(queues = "#{queue2Durable.getName()}")
    private void receiveDurable(final String message) {
        log.info("\nReceived the following message from the queue2Durable = " + message);
    }

    @RabbitListener(queues = "#{fanoutQueue.getName()}")
    public void receiveFanout1(final String message) {
        log.info("\nReceived  message from {} = {}", fanoutQueue.getName(), message);
    }

    @RabbitListener(queues = "#{topicQueue.getName()}")
    public void receiveTopic1(final String message) {
        log.info("\nReceived  message from {} = {}", topicQueue.getName(), message);
    }

    @RabbitListener(queues = "#{topicFileUploadedToMeeting.getName()}")
    public void receiveTopicFileUploadedToMeeting(final String message) {
        log.info("\nFile uploaded to Meeting {} > {}", topicFileUploadedToMeeting.getName(), message);
    }
}
