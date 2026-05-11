package com.accenture.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    public static final String DEFAULT_NAME = "Guest";

    @GetMapping
    public String hello(@RequestParam(defaultValue = DEFAULT_NAME) String name) {
        return "Hello " + (name == null || name.isBlank() ? DEFAULT_NAME : name);
    }
}
