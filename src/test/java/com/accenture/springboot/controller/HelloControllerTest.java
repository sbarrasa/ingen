package com.accenture.springboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloWithNameReturnsGreeting() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Lisandro"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Lisandro"));
    }

    @Test
    void helloWithoutNameReturnsDefaultGreeting() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello "+ HelloController.DEFAULT_NAME));
    }
}
