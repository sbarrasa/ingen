package com.accenture.powerbank.user.service;

import com.accenture.powerbank.user.api.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer {

    private static final Logger logger = LoggerFactory.getLogger(JmsProducer.class);

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public JmsProducer(JmsTemplate jmsTemplate, @Value("${user.updates.queue}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void sendUserEvent(UserEvent event) {
        logger.info("Sending JMS message: {} for user {}", event.action(), event.user().name());
        jmsTemplate.convertAndSend(destination, event);
    }
}
