package com.rvbraga.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rvbraga.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {	
	Usuario findByMatricula(String matricula);
	List<Usuario> findBySetor(String setor);
	List<Usuario> findByOrgao(String orgao);

}