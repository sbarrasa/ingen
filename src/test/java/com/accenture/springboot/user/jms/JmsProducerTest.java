package com.accenture.springboot.user.jms;

import com.accenture.springboot.user.api.UserDto;
import com.accenture.springboot.user.api.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JmsProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    private JmsProducer jmsProducer;
    private final String destination = "test.queue";

    @BeforeEach
    void setUp() {
        jmsProducer = new JmsProducer(jmsTemplate, destination);
    }

    @Test
    void sendUserEvent_ShouldCallJmsTemplate() {
        UserDto userDto = new UserDto(1, "Test User", LocalDate.now());
        UserEvent event = new UserEvent(UserEvent.ACTION_CREATE, userDto);

        jmsProducer.sendUserEvent(event);

        verify(jmsTemplate, times(1)).convertAndSend(eq(destination), eq(event));
    }
}
