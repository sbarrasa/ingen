package com.accenture.springboot.controller;

import com.accenture.springboot.dto.UserDto;
import com.accenture.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto user) {
        UserDto created = userService.create(user);
        URI location = URI.create(String.format("/users/%s", created.id()));
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Integer id,
            @Valid @RequestBody UserDto user
    ) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
