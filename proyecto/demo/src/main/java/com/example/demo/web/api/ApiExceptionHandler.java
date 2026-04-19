package com.example.demo.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.RelacionInvalidaException;

@RestControllerAdvice(basePackageClasses = {
        ZonaTurnoApiController.class,
        OperacionApiController.class,
        AnaliticaApiController.class,
        UsuarioApiController.class
})
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(RecursoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(RelacionInvalidaException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidRelation(RelacionInvalidaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ApiErrorDTO> handleDuplicate(RecursoDuplicadoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorDTO(exception.getMessage()));
    }
}
