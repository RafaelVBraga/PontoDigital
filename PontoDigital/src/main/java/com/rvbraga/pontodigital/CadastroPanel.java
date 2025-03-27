 package com.rvbraga.pontodigital;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.Reader.CaptureResult;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.rvbraga.pontodigital.models.Usuario;
import com.rvbraga.pontodigital.service.PontoDigitalService;

public class CadastroPanel extends JPanel implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nomeField, matriculaField, orgaoField, setorField, pesquisaMatricula;
    private JLabel imagemLabel, nomeLabel, matriculaLabel,orgaoLabel,setorLabel, digitalCapturadaLabel, pesquisaMatriculaLabel;
    private PontoDigitalService servico;  
    private JPanel painelPesquisa; 
    private JButton pesquisarButton, carregarUsuario;

	private Reader m_reader;
	private JCheckBox digitalField;
	
	private CaptureResult digitalCapturadaCR;
	private JPanel capturePanel;
	private ImagePanel imagemPanel;
	
	private Usuario usuario;
	
	
	

    public CadastroPanel(PontoDigitalService servico) {
        this.servico = servico;
        usuario = new Usuario();
        setLayout(new BorderLayout());
        
        try {
            ReaderCollection readers = UareUGlobal.GetReaderCollection();
            readers.GetReaders();

            if (readers.size() > 0) {
                this.m_reader = readers.get(0);
                System.out.println("✅ Leitor encontrado: " + this.m_reader.GetDescription().name);
            } else {
                System.err.println("❌ Nenhum leitor encontrado.");
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }

        JPanel formPanel = new JPanel(null);
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));
        
        painelPesquisa = new JPanel(null);
        painelPesquisa.setBorder(BorderFactory.createTitledBorder("Pesquisa"));
        
        pesquisaMatricula = new JTextField();
        pesquisaMatriculaLabel = new JLabel("Matrícula");
        pesquisarButton = new JButton("Pesquisar");
        
        painelPesquisa.add(pesquisaMatriculaLabel);
        pesquisaMatriculaLabel.setBounds(10,30,50,30);
        
        painelPesquisa.add(pesquisaMatricula);
        pesquisaMatricula.setBounds(10,70,220,30);
       
        painelPesquisa.add(pesquisarButton);
        pesquisarButton.setBounds(10,110,80,30);
        
        nomeField = new JTextField();
        nomeField.setEditable(false);
        nomeLabel = new JLabel("Nome:");
        matriculaField = new JTextField();
        matriculaField.setEditable(true);
        matriculaLabel = new JLabel("Matrícula:");
        orgaoField = new JTextField();
        orgaoField.setEditable(false);
        orgaoLabel = new JLabel("Órgão:");
        setorField = new JTextField();
        setorField.setEditable(false);
        setorLabel = new JLabel("Setor:");
        digitalField = new JCheckBox();
        digitalField.setSelected(false);
        digitalCapturadaLabel = new JLabel("Digital Capturada:");
        carregarUsuario = new JButton("🔍");
        
        imagemPanel = new ImagePanel();
        Dimension dm = new Dimension(200, 200);
        imagemPanel.setPreferredSize(dm);
        
        formPanel.add(carregarUsuario);
        carregarUsuario.setBounds(300, 30, 50, 30);
        formPanel.add(matriculaLabel);
        matriculaLabel.setBounds(10,30,150,30);                               
        formPanel.add(matriculaField);
        matriculaField.setBounds(75,30,220,30);                                    
        formPanel.add(nomeLabel);
        nomeLabel.setBounds(10,70,70,30);
        formPanel.add(nomeField);
        nomeField.setBounds(75,70,220,30);     
        formPanel.add(orgaoLabel);
        orgaoLabel.setBounds(10,110,70,30);
        formPanel.add(orgaoField);
        orgaoField.setBounds(75,110,220,30);
        formPanel.add(setorLabel);
        setorLabel.setBounds(10,150,220,30);
        formPanel.add(setorField);
        setorField.setBounds(75,150,220,30);
        formPanel.add(digitalCapturadaLabel);
        digitalCapturadaLabel.setBounds(10,190,120,30);
        formPanel.add(digitalField);
        digitalField.setBounds(130,190,220,30);
        digitalField.setEnabled(false);
        

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar");
        
        carregarUsuario.addActionListener(e -> carregarUsuarioDoBanco());
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
       

        capturePanel = new JPanel(new BorderLayout());
        capturePanel.setBorder(BorderFactory.createTitledBorder("Captura de Digital"));

        JButton btnCapturar = new JButton("Capturar");
        imagemLabel = new JLabel();
        imagemLabel.setHorizontalAlignment(JLabel.CENTER);
        imagemLabel.setPreferredSize(new Dimension(200, 200));
        imagemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        capturePanel.add(btnCapturar, BorderLayout.SOUTH);
        capturePanel.add(imagemPanel, BorderLayout.CENTER);
        
         
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.add(formPanel);
        mainPanel.add(capturePanel);
        
        add(painelPesquisa,BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnCapturar.setActionCommand("capture");
        btnCapturar.addActionListener(this);
        
        btnSalvar.addActionListener(e -> salvarUsuario());
        btnLimpar.addActionListener(e -> limparCampos());
        
    }
    
    private void carregarUsuarioDoBanco() {
    	System.out.println(matriculaField.getText());
    	if(matriculaField.getText().isBlank()||matriculaField.getText().isEmpty())
    		JOptionPane.showMessageDialog(CadastroPanel.this, "Preencha o campo 'Matrícula'!");
    	else
    		usuario = servico.findByMatricula(matriculaField.getText());
    	if(usuario!=null)
    		preencherFormulario();
    	else
    		JOptionPane.showMessageDialog(CadastroPanel.this,"Usuário não encontrado!");
    	matriculaField.setEditable(false);
    }   
  
	private void salvarUsuario() {    	
        /*
        usuario.setNome(nomeField.getText());
		usuario.setMatricula(matriculaField.getText());		
		usuario.setOrgao(orgaoField.getText());
		usuario.setSetor(setorField.getText());
*/
		Fid imagemFID = digitalCapturadaCR.image;
		try {
			Engine engine = UareUGlobal.GetEngine();
			Fmd digitalFmd = engine.CreateFmd(imagemFID, Fmd.Format.ANSI_378_2004);
			usuario.setDigital(digitalFmd.getData());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


		usuario = servico.save(usuario);
		
		//servico.criarMarcacaoMesUsuario(usuario);
		
		JOptionPane.showMessageDialog(this, "Digital cadastrada com sucesso!");
		
		limparCampos();
		
        
    }
	public void setImagem(Fid imagem) {
	    if (imagem != null) {
	    	
	    	imagemPanel.showImage(imagem);
	    	
	    }
	   
	}

    private void limparCampos() {
        nomeField.setText("");
        matriculaField.setText("");
        orgaoField.setText("");
        setorField.setText("");
        digitalCapturadaCR= null;
        imagemPanel.clearImage();
        digitalField.setSelected(false);
        matriculaField.setEditable(true);
       
    }
    private void preencherFormulario() {
    	 nomeField.setText(usuario.getNome());
         matriculaField.setText(usuario.getMatricula());
         orgaoField.setText(usuario.getOrgao());
         setorField.setText(usuario.getSetor());         
         imagemPanel.clearImage();
         digitalField.setSelected(usuario.getDigital()!=null);
    }    
   
	@Override
	public void actionPerformed(ActionEvent e) {
	    	 if(e.getActionCommand().equals("capture")) {
	    		 Capture.Run(m_reader, false, new ActionListener() {
	    			 @Override
	    			 public void actionPerformed(ActionEvent e) {
							if ("digital_capturada".equals(e.getActionCommand())) {														
								digitalCapturadaCR = ((Reader.CaptureResult) e.getSource());								
								JOptionPane.showMessageDialog(CadastroPanel.this, "Digital capturada com sucesso!");
								setImagem(digitalCapturadaCR.image);
								digitalField.setSelected(true);
							}
	    			 }
	    		 });
	    	 }	        
	}   
	
}
