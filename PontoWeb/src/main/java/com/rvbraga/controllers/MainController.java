package com.rvbraga.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rvbraga.dtos.MarcacaoDto;
import com.rvbraga.models.Marcacao;
import com.rvbraga.services.PontoService; 

@Controller 
public class MainController {
	
	@Autowired
	PontoService servico;
	
	
	@GetMapping("/home") 
	public String home() {
		return "home.xhtml";
	}
	
	@GetMapping("/relatorios")
	public String relatorios(Model model) {
		
		
		
		return "relatorios.xhtml";
	}
	@GetMapping("/visualizacao")
	public String visualizar(Model model) {
		
		List<Marcacao> marcs = servico.buscarMarcacoesUsuarioMes(10L);
		List<MarcacaoDto> marcacoes = new ArrayList<MarcacaoDto>();
		for(Marcacao marcacao: marcs)
			marcacoes.add(MarcacaoDto.toDto(marcacao));
		
		model.addAttribute("marcacoes", marcacoes);
		return "visualizacao.xhtml";
	}
	@GetMapping("/edicao")
	public String editar(Model model) {
		
		
		
		return "edicao.xhtml";
	}

}
