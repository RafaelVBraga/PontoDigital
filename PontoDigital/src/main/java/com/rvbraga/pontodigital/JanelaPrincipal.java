package com.rvbraga.pontodigital;



import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rvbraga.pontodigital.service.PontoDigitalService;

@Component
public class JanelaPrincipal extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private final PontoDigitalService servico;
   
    private CadastroPanel cadastroPanel;
    private BaterPontoPanel baterPontoPanel;
    @Autowired
    public JanelaPrincipal(PontoDigitalService servico) {
        
    	this.servico = servico;
    
        setTitle("Sistema de Ponto Digital");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        cadastroPanel = new CadastroPanel(servico);
        baterPontoPanel = new BaterPontoPanel(servico);

        tabbedPane.addTab("Cadastro", cadastroPanel);
        tabbedPane.addTab("Bater Ponto", baterPontoPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void exibir() {
        setVisible(true);
    }
}
