package com.rvbraga.pontodigital;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.rvbraga.pontodigital.models.Usuario;
import com.rvbraga.pontodigital.service.PontoDigitalService;

@SpringBootApplication
@ComponentScan(basePackages = "com.rvbraga.pontodigital.service")
public class PontoDigitalAppOriginal extends JFrame implements CommandLineRunner{
	/**
	 * 
	 */
	@Autowired
	private PontoDigitalService servico;
	
	private static final long serialVersionUID = 1L;
	private JTextField nomeField, matriculaField, orgaoField, setorField;
    private JLabel imagemLabel;
   
    
    public PontoDigitalAppOriginal() { 	
          
        
        setTitle("Cadastro de Usuário");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de Formulário
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));

        nomeField = new JTextField();        
        matriculaField = new JTextField();
        orgaoField = new JTextField();
        setorField = new JTextField();

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Matrícula:"));
        formPanel.add(matriculaField);
        formPanel.add(new JLabel("Órgão:"));
        formPanel.add(orgaoField);
        formPanel.add(new JLabel("Setor:"));
        formPanel.add(setorField);

        // Painel de Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnCancelar);

        // Painel de Captura
        JPanel capturePanel = new JPanel(new BorderLayout());
        capturePanel.setBorder(BorderFactory.createTitledBorder("Captura de Imagem"));

        JButton btnCapturar = new JButton("Capturar");
        imagemLabel = new JLabel();
        imagemLabel.setHorizontalAlignment(JLabel.CENTER);
        imagemLabel.setPreferredSize(new Dimension(200, 200));
        imagemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        capturePanel.add(btnCapturar, BorderLayout.NORTH);
        capturePanel.add(imagemLabel, BorderLayout.CENTER);

        // Painel Principal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.add(formPanel);
        mainPanel.add(capturePanel);

        // Adicionando os componentes na Janela
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Eventos dos Botões
        btnSalvar.addActionListener(e -> salvarUsuario());
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> dispose());

        btnCapturar.addActionListener(e -> capturarImagem());

        setVisible(true);
    }
    private void salvarUsuario() {
    	Usuario usuario = new Usuario();
    
        usuario.setNome(nomeField.getText());
        usuario.setMatricula(matriculaField.getText());;
        usuario.setOrgao(orgaoField.getText());
        usuario.setSetor(setorField.getText());

        servico.save(usuario);
        JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
    }
    private void limparCampos() {
        nomeField.setText("");
        matriculaField.setText("");
        orgaoField.setText("");
        setorField.setText("");
    }

    private void capturarImagem() {
        // Simula a captura de imagem (substitua com código real de captura)
        ImageIcon imagem = new ImageIcon("caminho/para/imagem.png");
        imagemLabel.setIcon(imagem);
    }
    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> new PontoDigitalAppOriginal().setVisible(true));
    }
	
    public static void main(String[] args) {
        // Inicializa o Spring Boot
    	SpringApplication.run(PontoDigitalAppOriginal.class, args);    

      
}
}
