package com.accenture.springboot.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDto(
        Integer id,
        @NotBlank String name,
        @NotNull LocalDate registrationDate
) {

    @JsonCreator
    public UserDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") @NotBlank String name,
            @JsonProperty("registrationDate") @NotNull LocalDate registrationDate
    ) {
        this.id = id;
        this.name = name;
        this.registrationDate = registrationDate;
    }
}
