/* Archivo documentado: Excepción de dominio usada por la API para reportar errores de negocio de forma clara y controlada. */
package com.example.demo.exception;

public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String message) {
        super(message);
    }
}
