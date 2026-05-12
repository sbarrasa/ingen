package com.accenture.springboot.user.service;

import com.accenture.springboot.user.domain.UserDocument;
import com.accenture.springboot.user.api.UserDto;
import com.accenture.springboot.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public UserDto getById(Integer id) {
        return toDto(findUser(id));
    }

    public UserDto update(Integer id, UserDto user) {
        UserDocument updatedUser = new UserDocument(
                id,
                user.name(),
                user.registrationDate()
        );

        return Optional.of(updatedUser)
                .map(userRepository::save)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(Integer id) {
        userRepository.delete(findUser(id));
    }

    private UserDocument findUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
