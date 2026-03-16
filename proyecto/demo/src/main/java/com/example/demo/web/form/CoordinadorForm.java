package com.example.demo.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinadorForm {
    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private Boolean activo = true;
    private String nivel;
}
