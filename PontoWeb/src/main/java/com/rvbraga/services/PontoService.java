package com.rvbraga.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvbraga.models.Marcacao;
import com.rvbraga.models.Usuario;
import com.rvbraga.repositories.MarcacaoRepository;
import com.rvbraga.repositories.UsuarioRepository;

@Service
public class PontoService {
	@Autowired
	private MarcacaoRepository marcacaoRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	public Usuario buscarUsuarioPorMatricula(String matricula) {
		return usuarioRepo.findByMatricula(matricula);
	}
	
	public List<Marcacao> buscarMarcacoesUsuarioMes(Long id){
		LocalDate hoje = LocalDate.now();
		LocalDate dataInicial = LocalDate.of(hoje.getYear(), hoje.getMonth(), 1);
		LocalDate dataFinal = LocalDate.of(hoje.getYear(), hoje.getMonth(),hoje.lengthOfMonth());
		
		return marcacaoRepo.findByUsuariosIdAndDiaBetweenOrderByDiaAsc(id, dataInicial, dataFinal);
	}

}
