package com.rvbraga.pontodigital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rvbraga.pontodigital.models.Usuario;

import jakarta.transaction.Transactional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	

	@Query("SELECT u FROM Usuario u WHERE u.digital = digital")
	Optional<Usuario> findByDigital(byte[] digital);

	
    @Transactional
    @Query(value = "INSERT INTO usuario (nome, matricula, orgao, setor, digital) VALUES (:nome, :matricula, :orgao, :setor, :digital)", nativeQuery = true)
    Long saveCustom(String nome, String matricula, String orgao, String setor, byte[] digital);
	
	@Query(value = "SELECT id, nome, matricula, orgao, setor, digital FROM usuario WHERE nome = :nome LIMIT 1", nativeQuery = true)
	Optional<Usuario> findByNome(@Param("nome") String nome);
	
	@Query(value = "SELECT id, nome, matricula, orgao, setor, encode(digital, 'base64') as digital FROM usuario", nativeQuery = true)
	List<Object[]> findAllUsuariosBase64();
	
	Usuario findByMatricula(String matricula);


}