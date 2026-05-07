package com.accenture.springboot.service;

import com.accenture.springboot.dto.UsuarioDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

	private final Map<UUID, UsuarioDto> usuarios = new HashMap<>();

	public List<UsuarioDto> getAll() {
		return usuarios.keySet().stream()
				.map(usuarios::get)
				.toList();
	}

	public UsuarioDto getById(final UUID id) {
		return usuarios.get(id);
	}

	public UsuarioDto create(final UsuarioDto usuario) {
		final UUID id = UUID.randomUUID();
		// create a new UsuarioDto that includes the generated id
		final UsuarioDto created = new UsuarioDto(id, usuario.nombre());
		usuarios.put(id, created);
		return created;
	}


	public void eliminar(UUID id) {
		usuarios.remove(id);
	}

}