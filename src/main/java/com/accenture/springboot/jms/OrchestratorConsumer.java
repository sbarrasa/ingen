package com.accenture.springboot.jms;

import com.accenture.springboot.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrchestratorConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrchestratorConsumer.class);

    @JmsListener(destination = "${user.updates.queue}")
    public void consumeUserEvent(UserEvent event) {
        logger.info("ORCHESTRATOR RECEIVED: Action={}, User={}", event.action(), event.user());
        // In a real scenario, this would trigger other microservices or workflows
    }
}
