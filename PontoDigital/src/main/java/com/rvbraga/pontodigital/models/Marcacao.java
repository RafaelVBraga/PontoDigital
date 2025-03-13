package com.rvbraga.pontodigital.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class Marcacao {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate dia;
	private LocalTime primeira;
	private LocalTime segunda;
	
	@ManyToMany(mappedBy = "marcacoes")
	List<Usuario> usuarios;

}
