package com.rvbraga.pontodigital;

import javax.swing.*;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rvbraga.pontodigital.service",
								"com.rvbraga.pontodigital"})
public class PontoDigitalApp extends JFrame {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		// Inicializa o Spring Boot
		SpringApplication.run(PontoDigitalApp.class, args);

	}
	
	 @Bean
	    CommandLineRunner iniciarJanela(JanelaPrincipal janelaPrincipal) {
	        return args -> SwingUtilities.invokeLater(janelaPrincipal::exibir);
	    }
	
}
