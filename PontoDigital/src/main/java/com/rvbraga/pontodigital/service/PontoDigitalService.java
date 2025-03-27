package com.rvbraga.pontodigital.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.rvbraga.pontodigital.MessageBox;
import com.rvbraga.pontodigital.models.Marcacao;
import com.rvbraga.pontodigital.models.Usuario;
import com.rvbraga.pontodigital.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class PontoDigitalService {
	@Autowired
	UsuarioRepository usuarioRepo;
	
	@Autowired
	MarcacaoService marcacaoService;
	
	public String obterMensagem() {
        return "Spring Boot rodando dentro de um Java Desktop!";
    }
	public Usuario save(Usuario usuario) {
		return usuarioRepo.save(usuario);
		
	}
	
	public Usuario findByMatricula(String matricula) {
		return usuarioRepo.findByMatricula(matricula);
	}
	
	public Optional<Usuario> findById(Long id) {
		return usuarioRepo.findById(id);
	}
	public Usuario buscarUsuarioPorNome(String nome) {
		return usuarioRepo.findByNome(nome).orElse(null);
	}
	public Optional<Usuario> findByDigital(byte[] digital) {
		return usuarioRepo.findByDigital(digital);
	}
	public Usuario buscarUsuarioPorDigital(Fid digitalCapturada) {
	    Usuario usuario = null;
	    
	    List<Usuario> usuarios = usuarioRepo.findAll();
	    System.out.println(usuarios.size());
	    
	    if(usuarios!= null)
	    	for(Usuario usuarioIt : usuarios) {
	    		System.out.println(usuarioIt.getNome());
	    		if(usuarioIt.getDigital()!=null)
	    			if(comparar(usuarioIt.getDigital(),digitalCapturada)) {
	    				usuario = usuarioIt;
	    			break;
	    		}
	    }	    	
	    return usuario;
	}
	
	

	    @Transactional
	    public Marcacao salvarMarcacao(Long usuarioId) throws Exception {
	    	
	    	
	        
	        Usuario usuario = usuarioRepo.findById(usuarioId).get();
	        
	        List<Marcacao> marcacoes = marcacaoService.findByUsuarioMes(usuarioId);
	        
	        if(marcacoes.size()==0) marcacaoService.criarMarcacaoMesUsuario(usuario);
	        
	        Marcacao marcacao =  marcacaoService.findybyUsuarioAndDia(usuario.getId(), LocalDate.now()).orElse(null);   	        		
	        	
	        	
	        	if(marcacao.getPrimeira()==null) {
	        		marcacao.setPrimeira(LocalTime.now());         		 
	        	}
	        	else { 
	        		if(marcacao.getSegunda()==null){
	        		marcacao.setSegunda(LocalTime.now());        		
	        		}
	        		else {
	        			 throw new Exception("Usuário já possui todas as marcações do dia!");
	        			}       		
	        		}	         
	        
	        marcacaoService.save(marcacao);
	        return marcacao;
	    }
	
	public void criarMarcacaoMesUsuario(Usuario usuario) {
			marcacaoService.criarMarcacaoMesUsuario(usuario);
	}
	
	
	public List<Usuario> findaAll(){
		return usuarioRepo.findAll();
	}
	
	private Boolean comparar(byte[] digitalBanco, Fid digitalCapturada) {
    	Engine engine = UareUGlobal.GetEngine();
    	
    	
    	try {
    		
        	Fmd digitalCapturadaFmd = engine.CreateFmd(digitalCapturada, Fmd.Format.ANSI_378_2004);
        	System.out.println("Conversão da captura passou");
        	Fmd digitalBancoFmd = UareUGlobal.GetImporter().ImportFmd(digitalBanco, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
    		System.out.println("Conversão do banco passou");
            int falsematch_rate = engine.Compare(digitalBancoFmd, 0, digitalCapturadaFmd, 0);
            int target_falsematch_rate = 21474;
            if (falsematch_rate < target_falsematch_rate) {
            	return true;

            } else {
            	return false;

            } 
          } catch (UareUException e) {
            MessageBox.DpError("Engine.CreateFmd()", e);
            System.out.println(e.getMessage());
            return false;
          } 
    }
	
}
