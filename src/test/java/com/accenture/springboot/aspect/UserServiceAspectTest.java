package com.accenture.springboot.aspect;

import com.accenture.springboot.service.UserService;
import com.accenture.springboot.dto.UsuarioDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceAspectTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String QUEUE_NAME = "userservice.logs";

    @Test
    public void testAOPInterceptsCreate() {
        UsuarioDto user = new UsuarioDto(null, "Test User");
        
        // Invocamos el método del servicio
        userService.create(user);

        // Verificamos que se haya enviado el mensaje a la cola
        jmsTemplate.setReceiveTimeout(5000);
        String receivedMessage = (String) jmsTemplate.receiveAndConvert(QUEUE_NAME);

        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage).contains("Metodo invocado: create");
        assertThat(receivedMessage).contains("Test User");
    }

    @Test
    public void testAOPInterceptsEliminar() {
        UUID id = UUID.randomUUID();
        
        // Invocamos el método del servicio
        userService.eliminar(id);

        // Verificamos que se haya enviado el mensaje a la cola
        jmsTemplate.setReceiveTimeout(5000);
        String receivedMessage = (String) jmsTemplate.receiveAndConvert(QUEUE_NAME);

        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage).contains("Metodo invocado: eliminar");
        assertThat(receivedMessage).contains(id.toString());
    }
}
