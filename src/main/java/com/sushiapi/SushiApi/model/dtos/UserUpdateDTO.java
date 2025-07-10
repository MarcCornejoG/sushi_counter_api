package com.sushiapi.SushiApi.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String firstName;

    @Size(max = 50, message = "El apellido no puede exceder 50 caracteres")
    private String lastName;

    @Email(message = "Formato de email inválido")
    private String email;
}
