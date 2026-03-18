package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación MPA para vigilancia docente.
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// Punto de entrada de la aplicacion web MPA.
		SpringApplication.run(DemoApplication.class, args);
	}

}
