/* Archivo documentado: Manejador global de errores de la API REST. Traduce excepciones de negocio a respuestas HTTP con mensajes consistentes para el frontend. */
package com.example.demo.web.api.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.RelacionInvalidaException;
import com.example.demo.web.api.controller.AnaliticaApiController;
import com.example.demo.web.api.controller.AuthApiController;
import com.example.demo.web.api.controller.OperacionApiController;
import com.example.demo.web.api.controller.ReporteApiController;
import com.example.demo.web.api.controller.UsuarioApiController;
import com.example.demo.web.api.controller.ZonaTurnoApiController;
import com.example.demo.web.api.dto.ApiDtos.ApiErrorDto;

@RestControllerAdvice(basePackageClasses = {
        ZonaTurnoApiController.class,
        OperacionApiController.class,
        AnaliticaApiController.class,
        AuthApiController.class,
        UsuarioApiController.class,
        ReporteApiController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiErrorDto> handleNotFound(RecursoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(RelacionInvalidaException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidRelation(RelacionInvalidaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ApiErrorDto> handleDuplicate(RecursoDuplicadoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorDto(exception.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorDto> handleBadCredentials(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrorDto("Credenciales inválidas."));
    }
}
