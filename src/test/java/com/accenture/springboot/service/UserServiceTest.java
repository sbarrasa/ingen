package com.accenture.springboot.service;

import com.accenture.springboot.document.UserDocument;
import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.dto.UserEvent;
import com.accenture.springboot.jms.JmsProducer;
import com.accenture.springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JmsProducer jmsProducer;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserDocument userDocument;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1, "Test User", LocalDate.now());
        userDocument = new UserDocument(1, "Test User", LocalDate.now());
    }

    @Test
    void create_ShouldSaveUserAndSendJmsEvent() {
        when(userRepository.existsById(1)).thenReturn(false);
        when(userRepository.save(any(UserDocument.class))).thenReturn(userDocument);

        UserDto result = userService.create(userDto);

        assertEquals(userDto.id(), result.id());
        verify(userRepository, times(1)).save(any(UserDocument.class));
        
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);
        verify(jmsProducer, times(1)).sendUserEvent(eventCaptor.capture());
        
        UserEvent capturedEvent = eventCaptor.getValue();
        assertEquals(UserEvent.ACTION_CREATE, capturedEvent.action());
        assertEquals(userDto.id(), capturedEvent.user().id());
    }

    @Test
    void update_ShouldUpdateUserAndSendJmsEvent() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userDocument));
        when(userRepository.save(any(UserDocument.class))).thenReturn(userDocument);

        UserDto result = userService.update(1, userDto);

        assertEquals(userDto.id(), result.id());
        verify(userRepository, times(1)).save(any(UserDocument.class));
        
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);
        verify(jmsProducer, times(1)).sendUserEvent(eventCaptor.capture());
        
        UserEvent capturedEvent = eventCaptor.getValue();
        assertEquals(UserEvent.ACTION_UPDATE, capturedEvent.action());
    }

    @Test
    void delete_ShouldDeleteUserAndSendJmsEvent() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userDocument));

        userService.delete(1);

        verify(userRepository, times(1)).delete(userDocument);
        
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);
        verify(jmsProducer, times(1)).sendUserEvent(eventCaptor.capture());
        
        UserEvent capturedEvent = eventCaptor.getValue();
        assertEquals(UserEvent.ACTION_DELETE, capturedEvent.action());
    }
}
