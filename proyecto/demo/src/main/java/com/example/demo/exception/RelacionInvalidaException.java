/* Archivo documentado: Excepción de dominio usada por la API para reportar errores de negocio de forma clara y controlada. */
package com.example.demo.exception;

public class RelacionInvalidaException extends RuntimeException {

    public RelacionInvalidaException(String message) {
        super(message);
    }
}
