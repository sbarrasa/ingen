package com.accenture.springboot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UsuarioDto(UUID id, @NotBlank String nombre) {

	@JsonCreator
	public UsuarioDto(@JsonProperty("id") UUID id,
					  @JsonProperty("nombre") @NotBlank String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

}