package com.rvbraga.pontodigital;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;

import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.rvbraga.pontodigital.models.Usuario;
import com.rvbraga.pontodigital.service.MarcacaoService;
import com.rvbraga.pontodigital.service.PontoDigitalService;

public class BaterPontoPanel extends JPanel implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PontoDigitalService servico;
    private JLabel lblNome, lblMatricula, lblOrgao, lblSetor;
    private Fid digitalCapturada;
    private Reader m_reader;
    @Autowired
    private MarcacaoService marcacaoService;

	public BaterPontoPanel(PontoDigitalService servico) {
        this.servico = servico;
        
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

       // JPanel infoPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JPanel infoPanel = new JPanel(null);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));

        lblNome = new JLabel("Nome: ");
        
        lblMatricula = new JLabel("Matrícula: ");
        lblOrgao = new JLabel("Órgão: ");
        lblSetor = new JLabel("Setor: ");
        

        infoPanel.add(lblNome);
        lblNome.setBounds(20, 20, 300, 50);
        infoPanel.add(lblMatricula);
        lblMatricula.setBounds(20, 50, 100, 50);
        infoPanel.add(lblOrgao);
        lblOrgao.setBounds(20, 80, 100, 50);
        infoPanel.add(lblSetor);
        lblSetor.setBounds(20, 110, 100, 50);

        JButton btnCapturar = new JButton("Ler Digital");
        btnCapturar.setActionCommand("capture");
        btnCapturar.addActionListener(this);
        add(infoPanel, BorderLayout.CENTER);
       
        add(btnCapturar, BorderLayout.SOUTH);
    }

    private void capturarDigital() throws Exception {
    	
        Usuario usuario = servico.buscarUsuarioPorDigital(digitalCapturada);       
        if(usuario==null) throw new Exception("Usuário não encontrado");
        try {
        	 servico.salvarMarcacao(usuario.getId());
        	 lblNome.setText("Nome: " + usuario.getNome());
             lblMatricula.setText("Matrícula: " + usuario.getMatricula());
             lblOrgao.setText("Órgão: " + usuario.getOrgao());
             lblSetor.setText("Setor: " + usuario.getSetor()); 
        	 JOptionPane.showMessageDialog(this, "Marcacao do usuario: "+usuario.getNome()+" efetuada!");
        }catch(Exception e) {        	
        	JOptionPane.showMessageDialog(this, e.getMessage());
        }                 
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
	    	 if(e.getActionCommand().equals("capture")) {
	    		 Capture.Run(m_reader, false, new ActionListener() {
	    			 @Override
	    			 public void actionPerformed(ActionEvent e) {
	    				 if ("digital_capturada".equals(e.getActionCommand())) {
	    					 Fid fid = ((Reader.CaptureResult)e.getSource()).image;	    					
	    		                digitalCapturada = fid;
	    		                try {
									capturarDigital();
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(BaterPontoPanel.this,e1.getMessage());
								}   		                
	    		                
	    		            }
	    			 }
	    		 });
	    	 }        
	} 
   
}
