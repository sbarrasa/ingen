package com.accenture.springboot.controller;

import com.accenture.springboot.dto.UsuarioDto;
import com.accenture.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

	private final UserService userService;

	@Autowired
	public UsuarioControlador(UserService usuarioServicio) {
		this.userService = usuarioServicio;
	}

	@GetMapping
	public List<UsuarioDto> getAll(@RequestParam(required = false) String filtro) {
		return userService.getAll();
	}

	@PostMapping
	public ResponseEntity<UsuarioDto> create(@Valid @RequestBody UsuarioDto usuario) {
		UsuarioDto created = userService.create(usuario);
		URI location = URI.create(String.format("/usuarios/%s", created.id()));
		return ResponseEntity.created(location).body(created);
	}

	@GetMapping("/{usuarioId}")
	public UsuarioDto getById(@PathVariable UUID usuarioId) {
		return userService.getById(usuarioId);
	}
}