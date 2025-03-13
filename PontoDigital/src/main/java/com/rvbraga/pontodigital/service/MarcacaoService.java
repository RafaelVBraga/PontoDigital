package com.rvbraga.pontodigital.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvbraga.pontodigital.models.Marcacao;
import com.rvbraga.pontodigital.models.Usuario;
import com.rvbraga.pontodigital.repository.MarcacaoRepository;
import com.rvbraga.pontodigital.repository.UsuarioRepository;

@Service
public class MarcacaoService {
	
	@Autowired
	MarcacaoRepository marcacaoRepo;
	@Autowired
	UsuarioRepository usuarioRepo;
	
	
	public Optional<Marcacao> findybyUsuarioAndDia(Long id, LocalDate dia) {
		return marcacaoRepo.findByUsuariosIdAndDia(id, dia);
	}
	public Optional<Marcacao> findbyDiaAndUsuario(LocalDate dia,Long id) {
		return marcacaoRepo.findByDiaAndUsuariosId(dia, id);
	}
	public List<Marcacao> findByUsuario(Long id){
		return marcacaoRepo.findByUsuariosId(id);
	}
	public List<Marcacao> findByUsuarioMes(Long id){
		LocalDate hoje = LocalDate.now();
		LocalDate dataInicial = LocalDate.of(hoje.getYear(), hoje.getMonth(), 1);
		LocalDate dataFinal = LocalDate.of(hoje.getYear(), hoje.getMonth(),hoje.lengthOfMonth());
		return marcacaoRepo.findByUsuariosIdAndDiaBetweenOrderByDiaAsc(id, dataInicial, dataFinal);
	}
	
	public Marcacao save(Marcacao marcacao) {
		return marcacaoRepo.save(marcacao);
	}
	public void criarMarcacaoMesUsuario(Usuario usuario) {
		
		LocalDate mes = LocalDate.now();
		List<Marcacao> marcacoes = new ArrayList<Marcacao>();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		for(Integer dia = 1; dia <= mes.lengthOfMonth(); dia++) {
			Marcacao marcacao = new Marcacao();
			marcacao.setDia(LocalDate.of(mes.getYear(),mes.getMonth(),dia));
			marcacao.setUsuarios(usuarios);
			marcacoes.add(marcacao);			
		}
		
		marcacaoRepo.saveAllAndFlush(marcacoes);
		usuario.setMarcacoes(marcacoes);
		usuarioRepo.save(usuario);
		
		
	}
	
	

}
