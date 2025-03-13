package com.rvbraga.pontodigital.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Usuario {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nome;
	    
	    private String matricula;
	    
	    private String orgao;
	    
	    private String setor;
	    
	    @ManyToMany
	    @JoinTable(
	        name = "usuario_marcacao",
	        joinColumns = @JoinColumn(name = "usuario_id"),
	        inverseJoinColumns = @JoinColumn(name = "marcacao_id")
	    )
	    private List<Marcacao> marcacoes;

	    @Lob // Indica que o campo armazenará um valor grande 
	    @Column(columnDefinition = "LONGBLOB")
	    private byte[] digital; 
	    public Usuario() {
	    	super();
	    }    
	    
	    public byte[]getDigital(){
	    	return digital;
	    }
	    
	    public void setDigital(byte[] digital) {
	    	this.digital = digital;
	    }
	    
	    
	    
	    

	   
	    
	   
}
