/* Archivo documentado: Punto de entrada del backend Spring Boot. Arranca la API REST y registra los beans principales de la aplicación. */
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la API REST para vigilancia docente.
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// Punto de entrada de la API Spring Boot.
		SpringApplication.run(DemoApplication.class, args);
	}

}
