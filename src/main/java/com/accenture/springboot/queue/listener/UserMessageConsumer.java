package com.accenture.springboot.queue.listener;

import com.accenture.springboot.user.api.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class UserMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageConsumer.class);

    @JmsListener(destination = "user.updates.queue")
    public void consumeUserEvent(UserEvent event) {
        logger.info("ORCHESTRATOR RECEIVED: Action={}, User={}", event.action(), event.user());
        // In a real scenario, this would trigger other microservices or workflows
    }
}
