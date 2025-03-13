package com.rvbraga.pontodigital.service;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.rvbraga.pontodigital.util.CaptureThread;

@Service
public class FingerprintService {
    private Reader reader;
    private CaptureThread captureThread;    
    private byte[] digitalCapturada;

    public FingerprintService() {
        try {
            ReaderCollection readers = UareUGlobal.GetReaderCollection();
            readers.GetReaders();

            if (readers.size() > 0) {
                this.reader = readers.get(0);
                System.out.println("✅ Leitor encontrado: " + reader.GetDescription().name);
            } else {
                System.err.println("❌ Nenhum leitor encontrado.");
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }
    }

    public void capturarDigital(ActionListener listener) {
        if (reader == null) {
            System.err.println("❌ Leitor de digitais não disponível.");
            return;
        }

        captureThread = new CaptureThread(reader, false, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT);
        captureThread.start(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CaptureThread.CaptureEvent evt = (CaptureThread.CaptureEvent) e;
                if (evt.capture_result != null && evt.capture_result.image != null) {
                    System.out.println("📸 Digital capturada!");
                    digitalCapturada = evt.capture_result.image.getData();
                    listener.actionPerformed(e); // Atualiza a interface
                } else {
                    System.err.println("❌ Falha na captura.");
                }
            }
        });
        
        
    }
    private void StartCaptureThread(ActionListener listener) {
        this.captureThread = new CaptureThread(reader, false, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT);
        this.captureThread.start(listener);
      }
      
      private void StopCaptureThread() {
        if (null != this.captureThread)
          this.captureThread.cancel(); 
      }
      
      private void WaitForCaptureThread() {
        if (null != this.captureThread)
    		try {
    			this.captureThread.join(1000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
      }

    public byte[] getDigitalCapturada() {
        return digitalCapturada;
    }

    public byte[] getDigitalBase64() {
        return digitalCapturada != null ? digitalCapturada: null;
    }
}
