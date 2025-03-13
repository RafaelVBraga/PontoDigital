package com.rvbraga.pontodigital.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rvbraga.pontodigital.models.Marcacao;

public interface MarcacaoRepository extends JpaRepository<Marcacao, Long> {
	
	Optional<Marcacao> findByUsuariosIdAndDia(Long id,LocalDate dia);
	Optional<Marcacao> findByDiaAndUsuariosId(LocalDate dia,Long id);
	List<Marcacao> findByUsuariosId(Long id);
	List<Marcacao> findByUsuariosIdAndDiaBetweenOrderByDiaAsc(Long id, LocalDate dataInicio, LocalDate dataFim);
}
