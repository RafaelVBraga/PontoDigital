package com.rvbraga.pontodigital;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.rvbraga.pontodigital.util.CaptureThread;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Capture extends JPanel implements ActionListener {
  private static final long serialVersionUID = 2L;
  
  private static final String ACT_BACK = "Voltar";
  
  private JDialog m_dlgParent;
  
  private CaptureThread m_capture;
  
  private Reader m_reader;
  private JLabel mensagem;
  
  private ImagePanel m_image;
  
  private boolean m_bStreaming;
  private ActionListener capturaListener; // Callback para enviar a digital capturada
  
  private Capture(Reader reader, boolean bStreaming, ActionListener capturaListener) {
    this.m_reader = reader;
    this.m_bStreaming = bStreaming;
    this.capturaListener = capturaListener;
    this.m_capture = new CaptureThread(this.m_reader, this.m_bStreaming, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT);
    int vgap = 5;
    BoxLayout layout = new BoxLayout(this, 1);
    setLayout(layout);
    this.m_image = new ImagePanel();
    Dimension dm = new Dimension(300, 50);
    this.m_image.setPreferredSize(dm);
    this.mensagem = new JLabel("Capturando...");
    add(this.mensagem);
    add(this.m_image);
    add(Box.createVerticalStrut(5));
    JButton btnBack = new JButton("Voltar");
    btnBack.setActionCommand("back");
    btnBack.addActionListener(this);
    add(btnBack);
    add(Box.createVerticalStrut(5));
  }
  
  private void StartCaptureThread() {
    this.m_capture = new CaptureThread(this.m_reader, this.m_bStreaming, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT);
    this.m_capture.start(this);
  }
  
  private void StopCaptureThread() {
    if (null != this.m_capture)
      this.m_capture.cancel(); 
  }
  
  private void WaitForCaptureThread() {
    if (null != this.m_capture)
		try {
			this.m_capture.join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Back")) {
      StopCaptureThread();
    } else if (e.getActionCommand().equals("capture_thread_captured")) {
      CaptureThread.CaptureEvent evt = (CaptureThread.CaptureEvent)e;
      boolean bCanceled = false;
      if (null != evt.capture_result) {
        if (null != evt.capture_result.image && Reader.CaptureQuality.GOOD == evt.capture_result.quality) {
          //this.m_image.showImage(evt.capture_result.image);
          
          
          if (capturaListener != null) {
              capturaListener.actionPerformed(new ActionEvent(evt.capture_result, ActionEvent.ACTION_PERFORMED, "digital_capturada"));
              this.m_dlgParent.dispose();
          }
        } else if (Reader.CaptureQuality.CANCELED == evt.capture_result.quality) {
          bCanceled = true;
        } else {
          MessageBox.BadQuality(evt.capture_result.quality);
        } 
      } else if (null != evt.exception) {
        MessageBox.DpError("Capture", evt.exception);
        bCanceled = true;
      } else if (null != evt.reader_status) {
        MessageBox.BadStatus(evt.reader_status);
        bCanceled = true;
      } 
      if (!bCanceled) {
        if (!this.m_bStreaming) {
          WaitForCaptureThread();
          StartCaptureThread();
        } 
      } else {
        this.m_dlgParent.setVisible(false);
      } 
    } 
  }
  
  private void doModal(JDialog dlgParent) {
    try {
      this.m_reader.Open(Reader.Priority.COOPERATIVE);
    } catch (UareUException e) {
      MessageBox.DpError("Reader.Open()", e);
    } 
    boolean bOk = true;
    if (this.m_bStreaming) {
      Reader.Capabilities rc = this.m_reader.GetCapabilities();
      if (!rc.can_stream) {
        MessageBox.Warning("Este leitor não suporta streaming");
        bOk = false;
      } 
    } 
    if (bOk) {
      StartCaptureThread();
      this.m_dlgParent = dlgParent;
      this.m_dlgParent.setContentPane(this);
      this.m_dlgParent.pack();
      this.m_dlgParent.setLocationRelativeTo(null);
      this.m_dlgParent.toFront();
      this.m_dlgParent.setVisible(true);
      this.m_dlgParent.dispose();
      StopCaptureThread();
      WaitForCaptureThread();
    } 
    try {
      this.m_reader.Close();
    } catch (UareUException e) {
      MessageBox.DpError("Reader.Close()", e);
    } 
  }
  
  public static void Run(Reader reader, boolean bStreaming, ActionListener capturaListener) {
    JDialog dlg = new JDialog((JDialog)null, "Posicione a digital no leitor!", true);
    Capture capture = new Capture(reader, bStreaming,capturaListener);
    capture.doModal(dlg);
  }
}
