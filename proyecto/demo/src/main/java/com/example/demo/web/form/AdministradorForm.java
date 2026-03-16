package com.example.demo.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorForm {
    private Long id;
    @NotBlank
    private String nombre;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String passwordHash;
    private Boolean activo = true;
    @NotBlank
    private String cargo;
}
