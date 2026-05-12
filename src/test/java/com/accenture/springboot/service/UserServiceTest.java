package com.accenture.springboot.service;

import com.accenture.springboot.document.UserDocument;
import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.exception.UserAlreadyExistsException;
import com.accenture.springboot.exception.UserNotFoundException;
import com.accenture.springboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final UserDocument document = new UserDocument(1, "Alice", LocalDate.of(2024, 1, 15));
    private final UserDto dto = new UserDto(1, "Alice", LocalDate.of(2024, 1, 15));

    @Test
    void getAll_returnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(document));

        List<UserDto> result = userService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).name()).isEqualTo("Alice");
    }

    @Test
    void getById_existingId_returnsUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(document));

        UserDto result = userService.getById(1);

        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("Alice");
    }

    @Test
    void getById_nonExistingId_throwsUserNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_newUser_returnsCreatedUser() {
        when(userRepository.existsById(1)).thenReturn(false);
        when(userRepository.save(any(UserDocument.class))).thenReturn(document);

        UserDto result = userService.create(dto);

        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("Alice");
    }

    @Test
    void create_existingId_throwsUserAlreadyExistsException() {
        when(userRepository.existsById(1)).thenReturn(true);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("1");
    }

    @Test
    void create_nullId_throwsIllegalArgumentException() {
        UserDto nullIdDto = new UserDto(null, "Bob", LocalDate.now());

        assertThatThrownBy(() -> userService.create(nullIdDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id is required");
    }

    @Test
    void update_existingUser_returnsUpdatedUser() {
        UserDto update = new UserDto(1, "Alice Updated", LocalDate.of(2024, 1, 15));
        UserDocument updatedDoc = new UserDocument(1, "Alice Updated", LocalDate.of(2024, 1, 15));

        when(userRepository.findById(1)).thenReturn(Optional.of(document));
        when(userRepository.save(any(UserDocument.class))).thenReturn(updatedDoc);

        UserDto result = userService.update(1, update);

        assertThat(result.name()).isEqualTo("Alice Updated");
    }

    @Test
    void update_nonExistingUser_throwsUserNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99, dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void delete_existingUser_deletesUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(document));

        userService.delete(1);

        verify(userRepository).delete(document);
    }

    @Test
    void delete_nonExistingUser_throwsUserNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(99))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }
}
