package com.accenture.powerbank.user.api;

import com.accenture.powerbank.user.service.UserAlreadyExistsException;
import com.accenture.powerbank.user.service.UserNotFoundException;
import com.accenture.powerbank.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    void getAll_ShouldReturnList() throws Exception {
        UserDto user = new UserDto(1, "John Doe", LocalDate.now());
        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void getById_WhenExists_ShouldReturnUser() throws Exception {
        UserDto user = new UserDto(1, "John Doe", LocalDate.now());
        when(userService.getById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getById_WhenNotExists_ShouldReturn404() throws Exception {
        when(userService.getById(1)).thenThrow(new UserNotFoundException(1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User 1 not found"));
    }

    @Test
    void create_WhenValid_ShouldReturn201() throws Exception {
        UserDto user = new UserDto(1, "John Doe", LocalDate.now());
        when(userService.create(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void create_WhenInvalid_ShouldReturn400() throws Exception {
        UserDto invalidUser = new UserDto(1, "", null); // Name blank, date null

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenAlreadyExists_ShouldReturn409() throws Exception {
        UserDto user = new UserDto(1, "John Doe", LocalDate.now());
        when(userService.create(any())).thenThrow(new UserAlreadyExistsException(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    void update_WhenValid_ShouldReturn200() throws Exception {
        UserDto user = new UserDto(1, "John Doe", LocalDate.now());
        when(userService.update(eq(1), any())).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void delete_WhenExists_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
