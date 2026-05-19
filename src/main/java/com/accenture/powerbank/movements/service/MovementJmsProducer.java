package com.accenture.powerbank.movements.service;

import com.accenture.powerbank.movements.domain.Movement;
import com.accenture.powerbank.queue.events.MovementRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovementJmsProducer {

    private static final Logger logger = LoggerFactory.getLogger(MovementJmsProducer.class);

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public MovementJmsProducer(
            JmsTemplate jmsTemplate,
            @Value("${movement.requests.queue}") String destination
    ) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void sendMovementRequested(Movement movement) {
        MovementRequestedEvent event = new MovementRequestedEvent(
                UUID.randomUUID().toString(),
                MovementRequestedEvent.EVENT_TYPE,
                movement.getId(),
                movement.getAccountId(),
                movement.getProductId(),
                movement.getMovementType(),
                movement.getAmount(),
                movement.getCurrency(),
                movement.getCreatedAt()
        );

        logger.info(
                "Sending JMS message: {} for movement {}",
                event.eventType(),
                event.movementId()
        );
        jmsTemplate.convertAndSend(destination, event);
    }
}
