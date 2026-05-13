package com.accenture.springboot.user.service;

import com.accenture.springboot.user.api.UserDto;
import com.accenture.springboot.user.api.UserEvent;
import com.accenture.springboot.user.domain.UserDocument;
import com.accenture.springboot.user.domain.UserRepository;
import com.accenture.springboot.user.jms.JmsProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JmsProducer jmsProducer;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, jmsProducer);
    }

    @Test
    void getAll_ShouldReturnListOfDtos() {
        UserDocument doc = new UserDocument(1, "John Doe", LocalDate.now());
        when(userRepository.findAll()).thenReturn(List.of(doc));

        List<UserDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).name());
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        UserDocument doc = new UserDocument(1, "John Doe", LocalDate.now());
        when(userRepository.findById(1)).thenReturn(Optional.of(doc));

        UserDto result = userService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.id());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(1));
    }

    @Test
    void create_WhenValid_ShouldSaveAndSendEvent() {
        UserDto dto = new UserDto(1, "John Doe", LocalDate.now());
        UserDocument doc = new UserDocument(1, "John Doe", dto.registrationDate());
        
        when(userRepository.existsById(1)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(doc);

        UserDto result = userService.create(dto);

        assertNotNull(result);
        verify(userRepository).save(any());
        verify(jmsProducer).sendUserEvent(argThat(event -> 
            event.action().equals(UserEvent.ACTION_CREATE) && event.user().id().equals(1)
        ));
    }

    @Test
    void create_WhenAlreadyExists_ShouldThrowException() {
        UserDto dto = new UserDto(1, "John Doe", LocalDate.now());
        when(userRepository.existsById(1)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_WhenExists_ShouldSaveAndSendEvent() {
        UserDto dto = new UserDto(1, "Updated Name", LocalDate.now());
        UserDocument existing = new UserDocument(1, "Old Name", LocalDate.now());
        UserDocument updated = new UserDocument(1, "Updated Name", dto.registrationDate());

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenReturn(updated);

        UserDto result = userService.update(1, dto);

        assertEquals("Updated Name", result.name());
        verify(jmsProducer).sendUserEvent(argThat(event -> 
            event.action().equals(UserEvent.ACTION_UPDATE) && event.user().name().equals("Updated Name")
        ));
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        UserDto dto = new UserDto(1, "Updated Name", LocalDate.now());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(1, dto));
    }

    @Test
    void delete_WhenExists_ShouldDeleteAndSendEvent() {
        UserDocument existing = new UserDocument(1, "John Doe", LocalDate.now());
        when(userRepository.findById(1)).thenReturn(Optional.of(existing));

        userService.delete(1);

        verify(userRepository).delete(existing);
        verify(jmsProducer).sendUserEvent(argThat(event -> 
            event.action().equals(UserEvent.ACTION_DELETE) && event.user().id().equals(1)
        ));
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(1));
        verify(userRepository, never()).delete(any());
    }
}
