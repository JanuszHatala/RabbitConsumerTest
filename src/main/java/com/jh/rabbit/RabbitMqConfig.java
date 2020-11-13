package com.jh.rabbit;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String FANOUT_QUEUE_1 = "fanout.queue1";
    public static final String FANOUT_QUEUE_2 = "fanout.queue2";
    public static final String FANOUT_EXCHANGE_0 = "fanout.exchange0";
    public static final String TOPIC_QUEUE_1 = "topic.Queue1";
    public static final String TOPIC_QUEUE_2 = "topic.Queue2-errors";
    public static final String TOPIC_EXCHANGE_0 = "topic.exchange0";
    public static final String BINDING_PATTERN_IMPORTANT = "*.important.*";
    public static final String BINDING_PATTERN_ERROR = "#.error";

    public static final String TOPIC_QUEUE_CALENDAR_FILE_UPLOADED = "topic.services.calendar-service.file-uploaded";
    public static final String TOPIC_EXCHANGE_FILE_SERVICE = "topic.services.file-service";
    public static final String BINDING_PATTERN_FILE_UPLOADED_TO_MEETING = "file.uploaded.to-meeting";

    // https://docs.spring.io/spring-amqp/reference/html/#binding

    @Value("${rabbitmq.queue}")
    private String queueName;
    @Value("${rabbitmq.queue2}")
    private String queue2Name;
    @Value("${rabbitmq.fanoutqueue}")
    private String fanoutQueue;
    @Value("${rabbitmq.topicqueue}")
    private String topicQueue;

    @Bean
    Queue queue() {
        // Creating a queue
        return new Queue(queueName, Boolean.FALSE);
    }

    @Bean
    Queue queue2Durable() {
        // Creating a queue
        return new Queue(queue2Name, Boolean.TRUE);
    }

    @Bean
    Queue fanoutQueue() {
        return new Queue(fanoutQueue, Boolean.TRUE);
    }

    @Bean
    Queue topicQueue() {
        return new Queue(topicQueue, Boolean.FALSE);
    }

    // https://docs.spring.io/spring-amqp/reference/html/#choosing-factory

    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue());
        container.setMessageListener(new RabbitMqListener());
        return container;
    }

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue2Name);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    // FANOUT

    @Bean
    public Declarables fanoutBindings() {
        Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1, true);
        Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2, true);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE_0);

        return new Declarables(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
                BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
    }

    @Bean
    MessageListenerContainer messageListenerContainerFanout(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(fanoutQueue());
        container.setMessageListener(new RabbitMqListener());
        return container;
    }

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(TOPIC_QUEUE_1, false);
        Queue topicQueue2 = new Queue(TOPIC_QUEUE_2, false);

        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE_0);

        return new Declarables(
                topicQueue1,
                topicQueue2,
                topicExchange,
                BindingBuilder
                        .bind(topicQueue1)
                        .to(topicExchange).with(BINDING_PATTERN_IMPORTANT),
                BindingBuilder
                        .bind(topicQueue2)
                        .to(topicExchange).with(BINDING_PATTERN_ERROR));
    }

    // File Uploaded to Calendar Meeting

    @Bean
    Queue topicFileUploadedToMeeting() {
        return new Queue(TOPIC_QUEUE_CALENDAR_FILE_UPLOADED, Boolean.TRUE);
    }

    @Bean
    public Declarables topicBindingsFileUploaded() {

        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE_FILE_SERVICE);

        return new Declarables(
                topicFileUploadedToMeeting(),
                topicExchange,
                BindingBuilder
                        .bind(topicFileUploadedToMeeting())
                        .to(topicExchange).with(BINDING_PATTERN_FILE_UPLOADED_TO_MEETING));
    }
}
