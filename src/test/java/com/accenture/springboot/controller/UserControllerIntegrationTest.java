package com.accenture.springboot.controller;

import com.accenture.springboot.document.UserDocument;
import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createAndGetUser_fullFlow() throws Exception {
        UserDto user = new UserDto(1, "Alice", LocalDate.of(2024, 1, 15));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void update_existingUser_modifiesData() throws Exception {
        userRepository.save(new UserDocument(2, "Bob", LocalDate.of(2024, 2, 1)));

        UserDto updated = new UserDto(2, "Bob Updated", LocalDate.of(2024, 2, 1));

        mockMvc.perform(put("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob Updated"));
    }

    @Test
    void delete_existingUser_removesUser() throws Exception {
        userRepository.save(new UserDocument(3, "Charlie", LocalDate.of(2024, 3, 1)));

        mockMvc.perform(delete("/users/3"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_nonExistingUser_returns404() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_duplicateId_returns409() throws Exception {
        userRepository.save(new UserDocument(4, "Dave", LocalDate.of(2024, 4, 1)));

        UserDto duplicate = new UserDto(4, "Dave", LocalDate.of(2024, 4, 1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
