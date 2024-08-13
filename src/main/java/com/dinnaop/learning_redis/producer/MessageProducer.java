package com.dinnaop.learning_redis.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dinnaop.learning_redis.model.Book;

@Service
public class MessageProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.hash.routingKey.name}")
    private String hashRoutingKeyName;

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Book book) throws Exception {
        try {
            rabbitTemplate.convertAndSend(exchangeName, hashRoutingKeyName, book);
        } catch (AmqpException e) {
            throw new Exception(e.getMessage());
        }
    }
}
