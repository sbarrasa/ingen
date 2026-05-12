package com.accenture.springboot.service;

import com.accenture.springboot.document.UserDocument;
import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.exception.UserAlreadyExistsException;
import com.accenture.springboot.exception.UserNotFoundException;
import com.accenture.springboot.dto.UserEvent;
import com.accenture.springboot.jms.JmsProducer;
import com.accenture.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JmsProducer jmsProducer;

    public UserService(UserRepository userRepository, JmsProducer jmsProducer) {
        this.userRepository = userRepository;
        this.jmsProducer = jmsProducer;
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto getById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDto create(UserDto user) {
        Integer id = user.id();

        if (id == null) {
            throw new IllegalArgumentException("User id is required");
        }

        if (userRepository.existsById(id)) {
            throw new UserAlreadyExistsException(id);
        }

        UserDto createdUser = toDto(userRepository.save(toDocument(user)));
        jmsProducer.sendUserEvent(new UserEvent(UserEvent.ACTION_CREATE, createdUser));
        return createdUser;
    }

    public UserDto update(Integer id, UserDto user) {
        return userRepository.findById(id)
                .map(existingUser -> new UserDocument(
                        existingUser.id(),
                        user.name(),
                        user.registrationDate()
                ))
                .map(userRepository::save)
                .map(this::toDto)
                .map(updatedUser -> {
                    jmsProducer.sendUserEvent(new UserEvent(UserEvent.ACTION_UPDATE, updatedUser));
                    return updatedUser;
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(Integer id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> {
                            userRepository.delete(user);
                            jmsProducer.sendUserEvent(new UserEvent(UserEvent.ACTION_DELETE, toDto(user)));
                        },
                        () -> {
                            throw new UserNotFoundException(id);
                        }
                );
    }

    private UserDto toDto(UserDocument document) {
        return new UserDto(
                document.id(),
                document.name(),
                document.registrationDate()
        );
    }

    private UserDocument toDocument(UserDto dto) {
        return new UserDocument(
                dto.id(),
                dto.name(),
                dto.registrationDate()
        );
    }
}
