/* Archivo documentado: Excepción de dominio usada por la API para reportar errores de negocio de forma clara y controlada. */
package com.example.demo.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
