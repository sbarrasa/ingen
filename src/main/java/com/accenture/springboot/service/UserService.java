package com.accenture.springboot.service;

import com.accenture.springboot.document.UserDocument;
import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.exception.UserAlreadyExistsException;
import com.accenture.springboot.exception.UserNotFoundException;
import com.accenture.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        return toDto(userRepository.save(toDocument(user)));
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
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(Integer id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        userRepository::delete,
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
