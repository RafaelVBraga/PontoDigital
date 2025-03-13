package com.rvbraga.dtos;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import com.rvbraga.models.Marcacao;

import lombok.Data;

@Data
public class MarcacaoDto {
	
    private Long id;
	
	private LocalDate dia;
	private String diaSemana;
	private LocalTime primeira;
	private LocalTime segunda;
	private String observacao;
	
	static public MarcacaoDto toDto(Marcacao marc) {
		
		MarcacaoDto marcacao = new MarcacaoDto();
		marcacao.dia = marc.getDia();
		marcacao.id = marc.getId();
		marcacao.primeira = marc.getPrimeira() == null ? null : marc.getPrimeira();
		marcacao.segunda = marc.getSegunda() == null ? null : marc.getSegunda();
		marcacao.diaSemana = marc.getDia().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("PT", "BR")) ;
		if (marcacao.primeira != null && marcacao.segunda != null) {
			Duration duracao = Duration.between(marcacao.primeira, marcacao.segunda);

			if (duracao.toHours() < 8) {
				marcacao.observacao = "Jornada Incompleta";
			} else
				marcacao.observacao = "";
		} else
			marcacao.observacao = "";
		return marcacao;	
	}
	

}
