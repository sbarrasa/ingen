package com.accenture.springboot.controller;

import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.exception.UserAlreadyExistsException;
import com.accenture.springboot.exception.UserNotFoundException;
import com.accenture.springboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto dto = new UserDto(1, "Alice", LocalDate.of(2024, 1, 15));

    @Test
    void getAll_returnsUsersList() throws Exception {
        when(userService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void getById_existingId_returnsUser() throws Exception {
        when(userService.getById(1)).thenReturn(dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getById_nonExistingId_returns404() throws Exception {
        when(userService.getById(99)).thenThrow(new UserNotFoundException(99));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("99")));
    }

    @Test
    void create_validUser_returns201WithLocation() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_duplicateId_returns409() throws Exception {
        when(userService.create(any(UserDto.class))).thenThrow(new UserAlreadyExistsException(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("1")));
    }

    @Test
    void create_invalidBody_returns400() throws Exception {
        UserDto invalid = new UserDto(1, "", LocalDate.of(2024, 1, 15));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_validUser_returnsUpdatedUser() throws Exception {
        UserDto updated = new UserDto(1, "Alice Updated", LocalDate.of(2024, 1, 15));
        when(userService.update(eq(1), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void update_nonExistingUser_returns404() throws Exception {
        when(userService.update(eq(99), any(UserDto.class))).thenThrow(new UserNotFoundException(99));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_nonExistingId_returns404() throws Exception {
        doThrow(new UserNotFoundException(99)).when(userService).delete(99);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }
}
